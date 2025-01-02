# Blog Backend API

Spring Boot backend application for the Blog Management System.

## Prerequisites

- Java 17 or higher
- PostgreSQL database
- Gradle 8.5 or higher

## Setup

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE blogdb;
   ```

2. Copy the environment variables template:
   ```bash
   cp .env.example .env
   ```

3. Edit `.env` file with your database credentials and JWT secret:
   ```bash
   DB_URL=jdbc:postgresql://localhost:5432/blogdb
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   JWT_SECRET=your-secure-random-secret-key
   ```

4. Build and run the application:
   ```bash
   ./gradlew build
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

## Environment Variables

The following environment variables can be configured:

- `DB_URL` - PostgreSQL database URL (default: `jdbc:postgresql://localhost:5432/blogdb`)
- `DB_USERNAME` - Database username (default: `postgres`)
- `DB_PASSWORD` - Database password (default: `postgres`)
- `JWT_SECRET` - Secret key for JWT token generation (required in production)
- `JWT_EXPIRATION` - JWT token expiration time in milliseconds (default: `86400000`)

## API Endpoints

- Authentication: `/api/auth/*`
- Posts: `/api/posts`
- Categories: `/api/categories`
- Tags: `/api/tags`

