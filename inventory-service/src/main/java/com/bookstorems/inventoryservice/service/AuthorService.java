package com.bookstorems.inventoryservice.service;

import com.bookstorems.inventoryservice.dto.AuthorDTO;
import com.bookstorems.inventoryservice.dto.BookDTO;
import com.bookstorems.inventoryservice.exception.AuthorExistsException;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import org.springframework.data.domain.Page;

/**
 * This class is responsible for managing the authors in the inventory.
 */
public interface AuthorService {

    /**
     * Create a new author
     * @param authorDTO the author to be created
     * @return the id of the created author
     * @throws AuthorExistsException if the author already exists
     */
    Long createAuthor(AuthorDTO authorDTO) throws AuthorExistsException;

    /**
     * Update an existing author
     * @param authorId the id of the author to be updated
     * @param authorDTO the author to be updated
     * @throws AuthorNotFoundException if the author does not exist
     * @throws AuthorExistsException if the author already exists
     */
    void updateAuthor(Long authorId, AuthorDTO authorDTO) throws AuthorExistsException;

    /**
     * Delete an existing author
     * @param authorId the id of the author to be deleted
     * @throws AuthorNotFoundException if the author does not exist
     */
    void deleteAuthor(Long authorId) throws AuthorNotFoundException;

    /**
     * Get all authors
     * @param name the name of the author
     * @param pageIndex the page index
     * @param pageSize the page size
     * @return a page of authors
     */
    Page<AuthorDTO> getAllAuthors(String name, int pageIndex, int pageSize);

    /**
     * Get all books of an author
     * @param authorId the id of the author
     * @param title the title of the book
     * @param pageIndex the page index
     * @param pageSize the page size
     * @return a page of books
     * @throws AuthorNotFoundException if the author does not exist
     */
    Page<BookDTO> getAuthorBooks(Long authorId, String title, int pageIndex, int pageSize) throws AuthorNotFoundException;

    /**
     * Get an author by id
     * @param authorId the id of the author
     * @return the author
     * @throws AuthorNotFoundException if the author does not exist
     */
    AuthorDTO getAuthorById(Long authorId) throws AuthorNotFoundException;

}
