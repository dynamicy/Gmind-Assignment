# BTC Pricing Application

This is a Spring Boot application that updates and retrieves the current BTC price using Redis and MongoDB.

## Features

- Update BTC price every 5 seconds.
- Store the latest BTC price in Redis.
- Persist the BTC price in MongoDB every minute.
- Retrieve the current BTC price from Redis or MongoDB.

## Prerequisites

- Java 11 or higher
- MongoDB
- Redis

## Project Structure
```
.
├── .gradle
├── .idea
├── build
├── gradle
├── src
│   └── main
│       └── java
│           └── io
│               └── csie
│                   └── chris
│                       └── demo
│                           ├── common
│                           │   ├── Constants.java
│                           │   └── TransactionType.java
│                           ├── config
│                           │   ├── MongoConfig.java
│                           │   ├── RedisConfig.java
│                           │   └── SwaggerConfig.java
│                           ├── controller
│                           │   ├── BtcPriceController.java
│                           │   ├── HealthCheckController.java
│                           │   ├── TransactionController.java
│                           │   └── UserController.java
│                           ├── dto
│                           │   ├── BtcPriceModel.java
│                           │   ├── TransactionModel.java
│                           │   └── UserModel.java
│                           ├── entity
│                           │   ├── BtcPrice.java
│                           │   ├── Transaction.java
│                           │   └── User.java
│                           ├── exception
│                           │   ├── GlobalExceptionHandler.java
│                           │   ├── InsufficientBalanceException.java
│                           │   ├── InsufficientBtcBalanceException.java
│                           │   └── UserNotFoundException.java
│                           ├── filter
│                           │   └── RateLimitingFilter.java
│                           ├── repository
│                           │   ├── BtcPriceRepository.java
│                           │   ├── TransactionRepository.java
│                           │   └── UserRepository.java
│                           ├── response
│                           │   └── ApiResponse.java
│                           ├── scheduler
│                           │   └── BtcPriceScheduler.java
│                           ├── serializer
│                           │   └── ObjectIdSerializer.java
│                           ├── service
│                           │   ├── BtcPriceService.java
│                           │   ├── TransactionService.java
│                           │   └── UserService.java
│                           └── BtcPricingApplication.java
│       ├── resources
│       │   ├── static
│       │   ├── templates
│       │   └── application.properties
│   └── test
│       └── java
│           └── io
│               └── csie
│                   └── chris
│                       └── demo
│                           ├── scheduler
│                           │   └── BtcPriceSchedulerTest.java
│                           ├── serializer
│                           │   └── ObjectIdSerializerTest.java
│                           └── service
│                               ├── TransactionServiceTest.java
│                               └── UserServiceTest.java
├── build.gradle
├── gradle.properties
└── README.md
```

## Getting Started
### Add MongoDB URI

Before running the application, you need to configure the MongoDB URI in the `application.properties` file located in `src/main/resources`.

```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.mongodb.net/<database>?retryWrites=true&w=majority
```
### Add Redis URI

Before running the application, you need to configure the MongoDB URI in the `application.properties` file located in `src/main/resources`.

```properties
spring.data.redis.host=????
spring.data.redis.port=6379
spring.data.redis.password=????
```

### Running

```
./gradlew build
./gradlew bootRun
```

### Testing
```
./gradlew test
```

## Swagger Integration

The application uses Swagger for API documentation. You can access the Swagger UI at http://localhost:8080/swagger-ui.html once the application is running.

```
http://localhost:8080/swagger-ui/index.html
```