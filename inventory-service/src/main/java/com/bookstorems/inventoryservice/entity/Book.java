package com.bookstorems.inventoryservice.entity;

import com.bookstorems.inventoryservice.dto.BookDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.Objects;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String publisher;
    private String language;
    private Date addedOn;
    private Integer quantity;
    @ManyToOne
    private Author author;
    private static final ModelMapper modelMapper = new ModelMapper();

    public Book(BookDTO bookDTO, Author author) {
        this.title = bookDTO.getTitle();
        this.description = bookDTO.getDescription();
        this.publisher = bookDTO.getPublisher();
        this.language = bookDTO.getLanguage();
        this.addedOn = new Date();
        this.author = author;
        this.quantity = bookDTO.getQuantity();
    }

    public void update(BookDTO bookDTO) {
        this.title = bookDTO.getTitle() != null ? bookDTO.getTitle() : this.title;
        this.description = bookDTO.getDescription() != null ? bookDTO.getDescription() : this.description;
        this.publisher = bookDTO.getPublisher() != null ? bookDTO.getPublisher() : this.publisher;
        this.language = bookDTO.getLanguage() != null ? bookDTO.getLanguage() : this.language;
        this.quantity = bookDTO.getQuantity() != null ? bookDTO.getQuantity() : this.quantity;
    }

    public BookDTO toDTO() {
        return modelMapper.map(this, BookDTO.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
