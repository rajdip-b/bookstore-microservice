package com.bookstorems.cartservice.repository;

import com.bookstorems.cartservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, Long>  {

    Optional<Cart> findByUserId(Long userId);

}
