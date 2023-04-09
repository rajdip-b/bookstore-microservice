package com.bookstorems.inventoryservice.service.impl;

import com.bookstorems.inventoryservice.dto.BookDTO;
import com.bookstorems.inventoryservice.entity.Author;
import com.bookstorems.inventoryservice.entity.Book;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import com.bookstorems.inventoryservice.exception.BookExistsException;
import com.bookstorems.inventoryservice.exception.BookNotFoundException;
import com.bookstorems.inventoryservice.repository.AuthorRepository;
import com.bookstorems.inventoryservice.repository.BookRepository;
import com.bookstorems.inventoryservice.service.BookService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CacheManager cacheManager;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CacheManager cacheManager) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    public Long createBook(BookDTO bookDTO) throws BookExistsException {
        // Check if a similar book exists or not
        if (bookRepository.existsByTitleContainingIgnoreCaseAndPublisherContainingIgnoreCaseAndLanguageContainingIgnoreCase(
                bookDTO.getTitle(), bookDTO.getPublisher(), bookDTO.getLanguage()))
            throw new BookExistsException();

        // Get the existing author similar to the details provided. If not found, create a new author
        var author = authorRepository.findByNameContainingIgnoreCaseAndCountryContainingIgnoreCase(
                bookDTO.getAuthor().getName(), bookDTO.getAuthor().getCountry())
                .orElseGet(() -> authorRepository.save(new Author(bookDTO.getAuthor())));

        // Create a new book
        var book = new Book(bookDTO, author);
        book = bookRepository.save(book);

        // Add the book to the author's book list
        author.getBooks().add(book);
        log.info("Book created: {} with author: {}", book, author);

        // Add the book to redis cache
        updateBookInCache(book);

        // Return the id of the newly created book
        return book.getId();
    }

    @Override
    @Transactional
    public void updateBook(Long bookId, BookDTO bookDTO) throws BookNotFoundException, BookExistsException {
        // Check if a similar book already exists
        if (bookRepository.existsByTitleContainingIgnoreCaseAndPublisherContainingIgnoreCaseAndLanguageContainingIgnoreCase(
                bookDTO.getTitle(), bookDTO.getPublisher(), bookDTO.getLanguage()))
            throw new BookExistsException();

        // Find the book by its id
        var book = findById(bookId);

        // Update the book
        book.update(bookDTO);

        // Update the book in redis cache
        updateBookInCache(book);

        log.info("Book updated: {}", book);
    }

    @Override
    @Transactional
    public void changeAuthor(Long bookId, Long authorId) throws BookNotFoundException, AuthorNotFoundException {
        // Get the new author
        var author = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);

        // Get the book
        var book = findById(bookId);

        // Remove the book from the old author's book list
        book.getAuthor().getBooks().remove(book);

        // Add the book to the new author's book list
        author.getBooks().add(book);

        // Update the author of the book
        book.setAuthor(author);

        // Update the book in redis cache
        updateBookInCache(book);

        log.info("Book: {} updated with new author: {}", book, author);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) throws BookNotFoundException {
        // Find the book
        var book = findById(bookId);

        // Delete the book
        bookRepository.delete(book);

        // Delete the book from redis cache
        deleteBookFromCache(book);

        log.info("Book deleted: {}", book);
    }

    @Override
    public Page<BookDTO> getAllBooks(String title, int pageIndex, int pageSize) {
        return bookRepository
                .findAllByTitleContainingIgnoreCase(title, PageRequest.of(pageIndex, pageSize))
                .map(Book::toDTO);
    }

    @Override
    public BookDTO getBookById(Long bookId) throws BookNotFoundException {
        // Get the book from redis cache. If the cache is empty, get the book from the database
        var bookDTO = cacheManager.getCache("books").get(bookId, BookDTO.class);

        if (Objects.nonNull(bookDTO))
            return bookDTO;

        var book = findById(bookId);
        updateBookInCache(book);
        return book.toDTO();
    }

    @Override
    public Integer getBookQuantity(Long bookId) throws BookNotFoundException {
        return findById(bookId).getQuantity();
    }

    @Override
    public void removeBookQuantity(Long bookId, Integer quantity) {
        var book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            // Update the book quantity
            book.setQuantity(book.getQuantity() - quantity);
            book = bookRepository.save(book);

            // Update the book in redis cache
            updateBookInCache(book);

            log.info("Book quantity updated: {}", book);
        }
    }

    @Override
    public void addBookQuantity(Long bookId, Integer quantity) {
        var book = bookRepository.findById(bookId).orElse(null);

        if (book != null) {
            // Update the book quantity
            book.setQuantity(book.getQuantity() + quantity);
            book = bookRepository.save(book);

            // Update the book in redis cache
            updateBookInCache(book);

            log.info("Book quantity updated: {}", book);
        }
    }

    private Book findById(Long bookId) throws BookNotFoundException {
        return bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    }

    private void updateBookInCache(Book book) {
        Objects.requireNonNull(cacheManager.getCache("books")).put(book.getId(), book.toDTO());
    }

    private void deleteBookFromCache(Book book) {
        Objects.requireNonNull(cacheManager.getCache("books")).evict(book.getId());
    }
}
