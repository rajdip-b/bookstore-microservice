package com.bookstorems.inventoryservice.service;

import com.bookstorems.inventoryservice.dto.BookDTO;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import com.bookstorems.inventoryservice.exception.BookExistsException;
import com.bookstorems.inventoryservice.exception.BookNotFoundException;
import org.springframework.data.domain.Page;

/**
 * This class is responsible for laying out all the details about
 * storing and managing books in the inventory service.
 */
public interface BookService {

    /**
     * Create a new book
     * @param bookDTO The details about the book to be created
     * @return The id of the new book created
     * @throws BookExistsException If the book already exists
     */
    Long createBook(BookDTO bookDTO) throws BookExistsException;

    /**
     * Update the details of an existing book
     * @param bookId The id of the book to be updated
     * @param bookDTO The new details of the book
     * @throws BookNotFoundException If the book does not exist
     * @throws BookExistsException If the book already exists
     */
    void updateBook(Long bookId, BookDTO bookDTO) throws BookNotFoundException, BookExistsException;

    /**
     * Change the author of a book
     * @param bookId The id of the book to be updated
     * @param authorId The id of the new author
     * @throws BookNotFoundException If the book does not exist
     * @throws AuthorNotFoundException If the author does not exist
     */
    void changeAuthor(Long bookId, Long authorId) throws BookNotFoundException, AuthorNotFoundException;

    /**
     * Delete a book
     * @param bookId The id of the book to be deleted
     * @throws BookNotFoundException If the book does not exist
     */
    void deleteBook(Long bookId) throws BookNotFoundException;

    /**
     * Get all books
     * @param title The title of the book
     * @param pageIndex The page index
     * @param pageSize The page size
     * @return A page of books
     */
    Page<BookDTO> getAllBooks(String title, int pageIndex, int pageSize);

    /**
     * Get a book by its id
     * @param bookId The id of the book
     * @return The details of the book
     * @throws BookNotFoundException If the book does not exist
     */
    BookDTO getBookById(Long bookId) throws BookNotFoundException;

    /**
     * Get the quantity of a book. Called by cart-service
     * @param bookId The id of the book
     * @return The quantity of the book
     * @throws BookNotFoundException If the book does not exist
     */
    Integer getBookQuantity(Long bookId) throws BookNotFoundException;

    /**
     * Remove a quantity of a book. Event consumed by RabbitMQ and is produced
     * by cart-service when a book is added to the cart
     * @param bookId The id of the book
     * @param quantity The quantity to be removed
     * @throws BookNotFoundException If the book does not exist
     */
    void removeBookQuantity(Long bookId, Integer quantity);

    /**
     * Add a quantity of a book. Event consumed by RabbitMQ and is produced
     * by cart-service when a book is removed from the cart
     * @param bookId The id of the book
     * @param quantity The quantity to be added
     * @throws BookNotFoundException If the book does not exist
     */
    void addBookQuantity(Long bookId, Integer quantity);

}
