package com.bookstorems.inventoryservice.repository;

import com.bookstorems.inventoryservice.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Author> findByNameContainingIgnoreCaseAndCountryContainingIgnoreCase(String name, String country);
    Boolean existsByNameContainingIgnoreCaseAndCountryContainingIgnoreCase(String name, String country);

}