# Development Environment Setup

This document explains how to set up the development environment for the project. It includes sections on prerequisites, installation steps, and configuration. It also provides examples of common setup issues and their solutions, explains how to run the project locally, and mentions any tools or dependencies required.

## Prerequisites

Before setting up the development environment, ensure you have the following prerequisites installed:

- Java Development Kit (JDK) 17 or later
- Gradle 7.0 or later
- Docker
- Node.js 14 or later
- npm 6 or later

## Installation Steps

Follow these steps to set up the development environment:

1. **Clone the repository:**

   ```sh
   git clone https://github.com/krhrtky/domestic-accounts-booking.git
   cd domestic-accounts-booking
   ```

2. **Set up the backend:**

   ```sh
   cd backend
   ./gradlew build
   ```

3. **Set up the frontend:**

   ```sh
   cd frontend
   npm install
   ```

4. **Start the Docker services:**

   ```sh
   docker-compose up -d
   ```

## Configuration

Configure the development environment by creating a `.env` file in the root directory with the following content:

```env
# Database configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=application_db
DB_USER=app
DB_PASSWORD=password

# Other configurations
API_URL=http://localhost:8080/api
```

## Common Setup Issues and Solutions

### Issue: Gradle build fails

**Solution:** Ensure you have the correct version of Gradle installed. You can check the version by running `gradle -v`. If the version is incorrect, download and install the correct version from the [Gradle website](https://gradle.org/install/).

### Issue: Docker services fail to start

**Solution:** Ensure Docker is running on your machine. You can check the status of Docker by running `docker info`. If Docker is not running, start it and try again.

### Issue: npm install fails

**Solution:** Ensure you have the correct version of Node.js and npm installed. You can check the versions by running `node -v` and `npm -v`. If the versions are incorrect, download and install the correct versions from the [Node.js website](https://nodejs.org/).

## Running the Project Locally

To run the project locally, follow these steps:

1. **Start the backend:**

   ```sh
   cd backend
   ./gradlew bootRun
   ```

2. **Start the frontend:**

   ```sh
   cd frontend
   npm start
   ```

3. **Access the application:**

   Open your web browser and navigate to `http://localhost:3000` to access the frontend application. The backend API will be available at `http://localhost:8080/api`.

## Tools and Dependencies

The project uses the following tools and dependencies:

- **Spring Boot:** A framework for building Java-based applications.
- **Kotlin:** A programming language used for the backend code.
- **React:** A JavaScript library for building user interfaces.
- **Docker:** A platform for developing, shipping, and running applications in containers.
- **Gradle:** A build automation tool used for managing dependencies and building the project.
- **Node.js:** A JavaScript runtime used for the frontend development.
- **npm:** A package manager for Node.js.

By following these steps and guidelines, you should be able to set up the development environment and run the project locally without any issues.
