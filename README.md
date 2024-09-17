# Distributed Transactions in Reactive Spring with Kafka

This project demonstrates a distributed transaction architecture using Reactive Spring and Kafka. The application consists of two services: `order-service` and `payment-service`, and utilizes Kafka events to ensure consistency between them. The order is confirmed only when the payment is successful. If the user doesn't have sufficient balance, the payment-service denies the payment and stores the transaction in db for future reference.

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Internal Flow](#internal-flow)
- [Testing the Application](#testing-the-application)

## Architecture Overview

The system consists of the following components:

- **Common Dto**: Consists of common components in the applicaiton.
- **Order Service**: Responsible for managing order creation.
- **Payment Service**: Manages the payment process for orders.

## Technologies Used

- **Spring Boot** for building the services.
- **Reactive Spring** for non-blocking, reactive programming.
- **Apache Kafka** for event-driven architecture.
- **SQL** for storage
- **Postman** for API testing.

## Setup and Installation

1. **Clone the repository**:

   ```bash
   git clone <repository-url>
   cd <repository-name>
   ```

2. **Build the project**:
   Navigate to the root directory and run the following command:

   ```bash
   mvn clean install
   ```

3. **Set up Zookeeper and Kafka**:
   Follow the steps below to install and run Kafka and Zookeeper locally:

   - Download and install Apache Kafka from [here](https://kafka.apache.org/quickstart).
   - Start Zookeeper:
     ```bash
     bin/zookeeper-server-start.sh config/zookeeper.properties
     ```
   - Start Kafka broker:
     ```bash
     bin/kafka-server-start.sh config/server.properties
     ```

4. **Run Order Service**:
   Navigate to the `order-service` directory and run:

   ```bash
   mvn spring-boot:run
   ```

5. **Database Setup**:
   Make sure your database is set up with the required tables/collections for order service.

6. **Run Payment Service**:
   Navigate to the `payment-service` directory and run:

   ```bash
   mvn spring-boot:run
   ```

7. **Import Postman Collection**:
   Import the provided Postman collection to test the application APIs. This collection includes test cases for placing orders and handling payments.

## Internal Flow

1. **Order Creation**:

   - The `Order Service` is responsible for creating orders. When an order is placed, it produces an `order-event` and sends it to Kafka.
   - Example request:
     ```json
     {
       "userId": 2,
       "productId": 3547,
       "price": 1200
     }
     ```

2. **Order Event Consumption**:
   - The `Payment Service` listens for the `order-event` from Kafka. Once it consumes the event, it processes the payment based on the user balance and the order amount.
3. **Payment Event**:

   - Depending on the payment result, the `Payment Service` produces a `payment-event` and sends it back to Kafka. This event contains the payment status (`SUCCESS`, `FAILED`).
   - Example payment event:
     ```json
     {
       "userId": 2,
       "productId": 3547,
       "price": 1200,
       "paymentStatus": "FAILED"
     }
     ```

4. **Order Update**:

   - The `Order Service` consumes the `payment-event` and updates the order status. If the payment is successful, the order status is marked as `COMPLETED`. Otherwise, the order is marked as `CANCELLED`.

   - **Success Flow**:

     - Payment is successful → `orderStatus` is updated to `COMPLETED`.

   - **Failure Flow**:
     - Payment fails → `orderStatus` is updated to `CANCELLED`.

## Testing the Application

1. Place an order using the `Order Service` API:

   - Endpoint: `POST /orders/create`
   - Example request body:
     ```json
     {
       "userId": 1,
       "productId": 3923,
       "price": 3000
     }
     ```

2. Observe the payment processing in `Payment Service`, which consumes the order event and produces a payment event based on user balance.

3. Check the order status:
   - If the payment is successful, the order status will be `COMPLETED`.
   - If the payment fails, the order status will be `CANCELLED`.

## Example Response

1. **Order with successful payment**:

   ```json
   {
     "orderId": 3,
     "userId": 2,
     "productId": 3923,
     "price": 3000,
     "paymentStatus": "SUCCESS",
     "orderStatus": "COMPLETED"
   }
   ```

2. **Order with failed payment**:
   ```json
   {
     "orderId": 2,
     "userId": 1,
     "productId": 3547,
     "price": 1200,
     "paymentStatus": "FAILED",
     "orderStatus": "CANCELLED"
   }
   ```
