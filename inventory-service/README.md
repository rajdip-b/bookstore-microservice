# Inventory Service

## Overview
The inventory service lies at the core of this application. It is responsible for 
storing all the data about Books and Authors. It stores all its data in a `MySQL` database.
Additionally, it also stores product data in a `Redis` cache.

## Functionalities and APIs
### Global APIs
- Get all the authors. API: `GET /inventory-service/global/author/`
- Get an author by id. API: `GET /inventory-service/global/author/{authorId}`
- Get all books by an author. API: `GET /inventory-service/global/author/{authorId}/books`
- Get all the books. API: `GET /inventory-service/global/book/`
- Get a book by id. API: `GET /inventory-service/global/book/{bookId}`

### Admin APIs
- Create a book. API: `POST /inventory-service/admin/book/`
- Update a book. API: `PUT /inventory-service/admin/book/{bookId}`
- Delete a book. API: `DELETE /inventory-service/admin/book/{bookId}`
- Create an author. API: `POST /inventory-service/admin/author/`
- Update an author. API: `PUT /inventory-service/admin/author/{authorId}`
- Delete an author. API: `DELETE /inventory-service/admin/author/{authorId}`
- Update the author of a book. API: `PUT /inventory-service/admin/book/{bookId}/author/{authorId}`

## Events
1. Listens to book added to cart event from `cart-service` and updates the book's quantity in the inventory.
2. Listens to book removed from cart event from `cart-service` and updates the book's quantity in the inventory.

## Port
The service runs on port `8004`.