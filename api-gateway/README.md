# API Gateway

## Overview
The API Gateway acts as the entrypoint to this application. Every request made from the 
client is routed through the API Gateway. The API Gateway is responsible for routing and 
authenticating requests on the go.

## Authentication process
All the urls in this appication has the pattern `<API_GATEWAY_HOSTNAME>/<SERVICE_NAME>/<VISIBILITY>/<RESOURCE>`.
For example, `http://localhost:8080/inventory-service/global/book/1` fetches a book by id `1`.
The authentication process is as follows:
- The API Gateway receives a request from the client.
- The API Gateway extracts the visibility from the url.
- If the visibility is among `admin` or `user`, the API Gateway extracts the JWT token from the
`Authorization` header.
- It checks for the validity of the token.
- If the token is valid, it extracts the `userId`, `email` and `role` from the token. 
- Then it passes on these data along with the request header to the downstream service.
- In case the authentication fails, the API Gateway returns a `403 Unauthorized` response.

## Port
The API Gateway runs on port `8765`.