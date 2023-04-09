package com.bookstorems.inventoryservice.service;

import com.bookstorems.inventoryservice.dto.BookDTO;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import com.bookstorems.inventoryservice.exception.BookExistsException;
import com.bookstorems.inventoryservice.exception.BookNotFoundException;
import org.springframework.data.domain.Page;

public interface BookService {

    Long createBook(BookDTO bookDTO) throws BookExistsException;
    void updateBook(Long bookId, BookDTO bookDTO) throws BookNotFoundException, BookExistsException;
    void changeAuthor(Long bookId, Long authorId) throws BookNotFoundException, AuthorNotFoundException;
    void deleteBook(Long bookId) throws BookNotFoundException;
    Page<BookDTO> getAllBooks(String title, int pageIndex, int pageSize);
    BookDTO getBookById(Long bookId) throws BookNotFoundException;
    Integer getBookQuantity(Long bookId) throws BookNotFoundException;
    void removeBookQuantity(Long bookId, Integer quantity);
    void addBookQuantity(Long bookId, Integer quantity);

}
