package com.bookstorems.inventoryservice.service.impl;

import com.bookstorems.inventoryservice.dto.AuthorDTO;
import com.bookstorems.inventoryservice.dto.BookDTO;
import com.bookstorems.inventoryservice.entity.Author;
import com.bookstorems.inventoryservice.entity.Book;
import com.bookstorems.inventoryservice.exception.AuthorExistsException;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import com.bookstorems.inventoryservice.repository.AuthorRepository;
import com.bookstorems.inventoryservice.service.AuthorService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    
    private final AuthorRepository authorRepository;
    private final CacheManager cacheManager;
    
    public AuthorServiceImpl(AuthorRepository authorRepository, CacheManager cacheManager) {
        this.authorRepository = authorRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    public Long createAuthor(AuthorDTO authorDTO) throws AuthorExistsException {
        // Check if a similar author already exists
        if (authorRepository.existsByNameContainingIgnoreCaseAndCountryContainingIgnoreCase(authorDTO.getName(), authorDTO.getCountry()))
            throw new AuthorExistsException();

        // Create a new author
        var author = new Author(authorDTO);
        author = authorRepository.save(author);
        log.info("Author created: {}", author);

        // Add the author to redis cache
        updateAuthorInCache(author);

        // Return the newly generated author id
        return author.getId();
    }

    @Override
    @Transactional
    public void updateAuthor(Long authorId, AuthorDTO authorDTO) throws AuthorExistsException {
        // Find the author by the id
        var author = findById(authorId);

        // Update the author
        author.update(authorDTO);

        // Update author in redis cache
        updateAuthorInCache(author);

        log.info("Author updated: {}", author);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long authorId) throws AuthorNotFoundException {
        // Find the author by the id
        var author = findById(authorId);

        // Delete the author. This also deletes all the books the author has
        authorRepository.delete(author);

        // Delete author from redis cache
        deleteAuthorFromCache(author);

        log.info("Author deleted: {}", author);
    }

    @Override
    public Page<AuthorDTO> getAllAuthors(String name, int pageIndex, int pageSize) {
        return authorRepository
                .findAllByNameContainingIgnoreCase(name, PageRequest.of(pageIndex, pageSize))
                .map(Author::toDTO);
    }

    @Override
    public Page<BookDTO> getAuthorBooks(Long authorId, String title, int pageIndex, int pageSize) throws AuthorNotFoundException {
        var author = findById(authorId);
        return new PageImpl<>(
                author.getBooks().stream()
                        .filter(book -> book.getTitle().contains(title))
                        .map(Book::toDTO)
                        .toList(),
                PageRequest.of(pageIndex, pageSize),
                author.getBooks().size()
        );
    }

    @Override
    public AuthorDTO getAuthorById(Long authorId) throws AuthorNotFoundException {
        // Check if the author is in redis cache
        var authorDTO = cacheManager.getCache("authors").get(authorId, AuthorDTO.class);

        // If the author is in redis cache, return it
        if (Objects.nonNull(authorDTO)) return authorDTO;

        // If the author is not in redis cache, find it in the database
        var author = findById(authorId);

        // Save the author in redis cache
        updateAuthorInCache(author);

        // Return the author
        return author.toDTO();
    }

    private Author findById(Long authorId) throws AuthorNotFoundException {
        return authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);
    }

    private void updateAuthorInCache(Author author) {
        Objects.requireNonNull(cacheManager.getCache("authors")).put(author.getId(), author.toDTO());
    }

    private void deleteAuthorFromCache(Author author) {
        Objects.requireNonNull(cacheManager.getCache("authors")).evict(author.getId());
    }
}
