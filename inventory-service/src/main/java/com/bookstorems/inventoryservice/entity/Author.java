package com.bookstorems.inventoryservice.entity;

import com.bookstorems.inventoryservice.dto.AuthorDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    @ToString.Exclude
    private List<Book> books;
    private static final ModelMapper modelMapper = new ModelMapper();

    public Author(AuthorDTO authorDTO) {
        this.name = authorDTO.getName();
        this.country = authorDTO.getCountry();
        this.books = new ArrayList<>();
    }

    public void update(AuthorDTO authorDTO) {
        this.name = authorDTO.getName() != null ? authorDTO.getName() : this.name;
        this.country = authorDTO.getCountry() != null ? authorDTO.getCountry() : this.country;
    }

    public AuthorDTO toDTO() {
        return modelMapper.map(this, AuthorDTO.class);
    }

    public List<Book> getBooks() {
        if (books == null)
            books = new ArrayList<>();
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Author author = (Author) o;
        return getId() != null && Objects.equals(getId(), author.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
