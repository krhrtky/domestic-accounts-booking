# System Overview

## Purpose

The purpose of this system is to provide a comprehensive solution for managing domestic accounts and bookings. It aims to simplify the process of tracking expenses, managing budgets, and splitting shared expenses among users.

## Key Features

1. **Expense Tracking**: Allows users to track their expenses and categorize them for better financial management.
2. **Budget Management**: Helps users set and manage budgets for different categories and track their spending against the budget.
3. **Shared Expense Splitting**: Enables users to split shared expenses with others and keep track of who owes what.
4. **Reporting and Analytics**: Provides detailed reports and analytics to help users understand their spending patterns and make informed financial decisions.
5. **User Authentication and Authorization**: Ensures secure access to the system by providing user authentication and authorization mechanisms.

## High-Level Architecture

The system is built using a microservices architecture, where each service is responsible for a specific set of tasks. The key components of the system include:

1. **API Gateway**: Acts as the entry point for all client requests and routes them to the appropriate microservices.
2. **Authentication Service**: Manages user authentication and authorization.
3. **User Service**: Handles user-related data and operations.
4. **Expense Service**: Manages expense tracking and categorization.
5. **Budget Service**: Handles budget management and tracking.
6. **Shared Expense Service**: Manages shared expense splitting and tracking.
7. **Reporting Service**: Provides reporting and analytics functionalities.

## Diagrams

### System Structure
![System Structure](../images/system_structure.png)

## Interactions between Components

1. The client sends a request to the API Gateway.
2. The API Gateway routes the request to the appropriate microservice.
3. The microservice processes the request and may interact with other microservices or external services.
4. The response is sent back to the API Gateway.
5. The API Gateway returns the response to the client.

## External Dependencies and Integrations

1. **External APIs**: The system integrates with various external APIs to fetch data and provide additional functionalities.
2. **Database**: The system uses PostgreSQL as the primary relational database for storing structured data.
3. **In-Memory Database**: The system uses Redis as an in-memory database for caching and fast data retrieval.
4. **Message Queues**: The system uses Kafka for asynchronous communication between microservices.
