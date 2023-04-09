# Cart Service

## Overview
The cart service is responsible for storing the details about the cart of a user. The service uses
`MongoDB` as the database to store the cart details.

## Functionalities and APIs
1. Add book to cart. API: `PUT /cart-service/user/cart/add/{bookId}/{quantity}`
2. Remove book from cart. API: `DELETE /cart-service/user/cart/remove/{bookId}`
3. Get cart details. API: `GET /cart-service/user/cart/`
4. Clear the cart. API: `DELETE /cart-service/user/cart/clear`

## Events
1. Produces an event when a book is added to the cart that is subscribed by the `inventory-service`.
2. Produces an event when a book is removed from the cart that is subscribed by the `inventory-service`.
3. Subscribes to an event from `user-service` to clear the cart when a user is deleted.

## Dependencies
1. `inventory-service` - To check if the book is available in the inventory.

## Port
The service runs on port `8002`.