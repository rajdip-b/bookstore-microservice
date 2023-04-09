package com.bookstorems.userservice.entity;

import com.bookstorems.userservice.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private Long id;
    private String email;
    private String password;
    private String name;

    public UserDTO toDTO() {
        return new UserDTO(id.toString(), email, name, null, email.equals("admin@bookstore.com") ? "ROLE_ADMIN" : "ROLE_USER");
    }

}
