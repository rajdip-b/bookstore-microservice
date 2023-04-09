# User Service

## Overview
The user service is responsible for storing and managing the users of the application. 
This service stores its data in a `MongoDB` database.

## Functionalities and APIs

### User APIs
- Sign up. API: `POST /user-service/global/user/signup`
- Log in. API: `POST /user-service/global/user/login`
- Get details about yourself. API: `GET /user-service/user/`
- Update details about yourself. API: `PUT /user-service/user/`
- Delete your account. API: `DELETE /user-service/user/`

### Admin APIs
- Update any user's details. API: `PUT /user-service/admin/user/{userId}`
- Delete any user's account. API: `DELETE /user-service/admin/user/{userId}`
- Get details about any user. API: `GET /user-service/admin/user/{userId}`
- Get all users. API: `GET /user-service/admin/user/`

## Events
1. Publishes an event for `cart-service` when a user is deleted

## Port
The service runs on port `8001`