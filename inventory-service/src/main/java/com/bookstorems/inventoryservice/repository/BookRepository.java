package com.bookstorems.inventoryservice.repository;

import com.bookstorems.inventoryservice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
    Boolean existsByTitleContainingIgnoreCaseAndPublisherContainingIgnoreCaseAndLanguageContainingIgnoreCase(String title, String publisher, String language);

}