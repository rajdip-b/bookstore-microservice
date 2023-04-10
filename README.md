# Bookstore Microservice

## Introduction
This application demonstrates the microservice architecture using **Spring Cloud**. It is a basic application where we mimic the working of a bookstore platform, where in users can create their accounts, browse books, add/remove books from cart. Admins would be able to see all data about users and books. They will also be able to modify the books and authors on the platform. 

### List of microservices

- [User Service](./user-service) - Stores information about a user.
- [Inventory Service](./inventory-service) - Stores business logic about books and authors.
- [Cart Service](./cart-service) - Stores information about the cart of a user.
- [Eureka Server](./eureka-server) - Service discovery server.
- [Config Server](./config-server) - Configuration server.
- [API Gateway](./api-gateway) - API gateway.

Each of the microservices have their own README.md file that provides more details about the microservice.
The details include working, api documentation, events, etc.

### Other technologies

- **Spring Cloud Config Server** for storing configurations
- **Spring Cloud Netflix Eureka** for service discovery
- **Spring Cloud Gateway** as the api gateway
- **Resiliance4J** for implementing circuit breaker pattern
- **Micrometer, OpenTelemetry, Zipkin** for observability
- **RabbitMQ** for inter service communication
- **MySQL** for `Product Service`
- **MongoDB** for `Auth Service`, `User Service` and `Cart Service`
- **Redis** to store details about books and authors in memory.
- **Docker** for containerizing the services
- **Docker Compose** to start the all the services and peripherals at once

### Stack
- Java 17
- SpringBoot 3
- Spring Cloud 2
- Microservice Architecture

### Special permission
- `admin` - Can access all the APIs
- `user` - Can access all the APIs except the admin APIs

To get admin privileges, you need to create an account with the email `admin@bookstore.com`.

---
## Getting Started
This project can be run in two profiles:
1. Development
2. Production
For development, you have to individually run all the pre-requisites and then run the services one by one. 
For production, you can use docker-compose to start all the services and peripherals at once.

### Running in `Development` mode
1. We need to set up the pre-requisites before we do anything. For that, we need to fire up a few docker containers.
   - Start a mysql instance
     ```bash
     docker run --name bms-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql
     ```
   - Start a mongodb instance
     ```bash
     docker run --name bms-mongodb -p 27017:27017 -e MONGODB_INIT_ROOT_USERNAME=admin -e MONGODB_INIT_ROOT_PASSWORD=admin -d mongo
     ```
   - Start a redis instance 
     ```bash
     docker run -d --name bms-redis -p 6379:6379 -e REDIS_USER=root -e REDIS_PASSWORD=root redis
     ```
   - Start a zipkin instance
     ```bash
     docker run --name bms-zipkin -p 9411:9411 -d openzipkin/zipkin
     ```
   - Start a rabbitmq instance
     ```bash
     docker run --name bms-rmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest -d rabbitmq
     ```
2. Once done, we need to set an environmental variable.
   ```bash
   export BMS_PROFILE=development (i)
   export CONFIG_REPO_PATH=<path/to/config-repo> (ii)
   ```
   1. This will make the services start up in a development mode. We need this because the services will be fetching data from the config server based on the `spring.profile.active` property.
   2. This is the path to the folder where you have the config-repo cloned. This is required because the config server will be fetching the configurations from this folder. Normally, it would be under `{USER_HOME}/path/to/bookstore-microservice/config-repo`. But, you can change it to any other location. Just make sure you set the `CONFIG_REPO_PATH` to that location.
3. Now, we need to install the dependencies. Make sure you have `Java 17` installed locally. Once sure, you can proceed.
   ```bash
   cd bookstore-microservice
   
   folders=("api-gateway" "eureka-server" "config-server" "user-service" "cart-service" "inventory-service")
   for folder in "${folders[@]}"; do
      cd "$element" && ./mvnw clean install && cd ../
   done
   ```
4. Finally, we can start the services one by one. Make sure you launch the services from the same terminal where you have set the temporary variables or from 
some IDE that has the same environmental variables set in the launch configuration.
   ```bash
   cd bookstore-microservice
   
   folders=("eureka-server" "config-server" "api-gateway" "user-service" "cart-service" "inventory-service")
   for folder in "${folders[@]}"; do
      cd "$element" && ./mvnw spring-boot:run & && cd ../
   done
   ```
5. You can now access the services at the following urls:
    - `Eureka Server` - http://localhost:8761
    - `Config Server` - http://localhost:8888
    - `API Gateway` - http://localhost:8765
    - `User Service` - http://localhost:8001
    - `Cart Service` - http://localhost:8002
    - `Inventory Service` - http://localhost:8004
    - `Zipkin` - http://localhost:9411
    - `RabbitMQ` - http://localhost:15672
    - `MySQL` - http://localhost:3306
    - `MongoDB` - http://localhost:27017
    - `Redis` - http://localhost:6379
   
   Make sure that you make the API calls to **`API Gateway` ONLY**. Otherwise the authentication won't work. Enjoy!

### Running in `Production` mode
TODO