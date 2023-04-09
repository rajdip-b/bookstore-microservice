package com.bookstorems.cartservice.dto;

import java.util.List;

public record CartDTO (Long userId, List<Long> bookIds, List<Integer> quantity) {
}
