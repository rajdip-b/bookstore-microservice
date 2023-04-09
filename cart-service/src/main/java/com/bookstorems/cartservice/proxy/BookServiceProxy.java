package com.bookstorems.cartservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
public interface BookServiceProxy {

    @GetMapping("/global/book/{bookId}/quantity")
    ResponseEntity<?> getBookQuantity(@PathVariable Long bookId);

}
