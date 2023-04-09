package com.bookstorems.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String title;
    private String description;
    private String publisher;
    private String language;
    private Date addedOn;
    private AuthorDTO author;
    private Integer quantity;

}
