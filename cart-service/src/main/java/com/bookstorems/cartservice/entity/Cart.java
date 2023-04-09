package com.bookstorems.cartservice.entity;

import com.bookstorems.cartservice.dto.CartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    private Long userId;
    // <bookId, quantity>
    private Map<Long, Integer> products;

    public Map<Long, Integer> getProducts() {
        if (products == null)
            products = new HashMap<>();
        return products;
    }

    public CartDTO toDTO() {
        var bookIds = products.keySet().stream().toList();
        var quantities = products.values().stream().toList();
        return new CartDTO(userId, bookIds, quantities);
    }

}
