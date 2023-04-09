package com.bookstorems.inventoryservice.service;

import com.bookstorems.inventoryservice.dto.AuthorDTO;
import com.bookstorems.inventoryservice.dto.BookDTO;
import com.bookstorems.inventoryservice.exception.AuthorExistsException;
import com.bookstorems.inventoryservice.exception.AuthorNotFoundException;
import org.springframework.data.domain.Page;

public interface AuthorService {

    Long createAuthor(AuthorDTO authorDTO) throws AuthorExistsException;
    void updateAuthor(Long authorId, AuthorDTO authorDTO) throws AuthorExistsException;
    void deleteAuthor(Long authorId) throws AuthorNotFoundException;
    Page<AuthorDTO> getAllAuthors(String name, int pageIndex, int pageSize);
    Page<BookDTO> getAuthorBooks(Long authorId, String title, int pageIndex, int pageSize) throws AuthorNotFoundException;
    AuthorDTO getAuthorById(Long authorId) throws AuthorNotFoundException;

}
