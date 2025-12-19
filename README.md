# Blog Management System

A full-stack blog management application built with React and Spring Boot, featuring user authentication, post management, categories, tags, and role-based access control.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Security Features](#security-features)
- [Troubleshooting](#troubleshooting)
- [Production Deployment](#production-deployment)

## Features

### Core Features
- **User Authentication & Authorization**
  - User registration and login
  - JWT-based authentication
  - Role-based access control (User, Admin)
  - Protected routes

- **Post Management**
  - Create, read, update, and delete posts
  - Rich text editor (React Quill) for content creation
  - HTML content sanitization (DOMPurify) for XSS protection
  - Post preview with HTML tag stripping
  - Author-based permissions (users can only edit/delete their own posts)
  - Admin override (admins can edit/delete any post)

- **Category Management**
  - Create, update, and delete categories
  - Assign categories to posts
  - Prevent deletion of categories with associated posts

- **Tag Management**
  - Create, update, and delete tags
  - Many-to-many relationship with posts
  - Tag filtering and organization

- **User Interface**
  - Responsive design
  - Modern, clean UI
  - Real-time form validation
  - Error handling and user feedback

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.5
- **Database**: PostgreSQL 12+
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security with JWT
- **Validation**: Jakarta Validation
- **Testing**: JUnit 5, Spring Boot Test, H2 (for testing)

### Frontend
- **Framework**: React 18.2.0
- **Routing**: React Router DOM 6.20.0
- **HTTP Client**: Axios 1.6.2
- **Rich Text Editor**: React Quill 2.0.0
- **Security**: DOMPurify 3.0.6 (XSS protection)
- **State Management**: React Context API
- **Testing**: Jest, React Testing Library
- **Build Tool**: Create React App (react-scripts 5.0.1)

## Architecture

### Backend Architecture
```
┌─────────────────────────────────────────┐
│         REST Controllers                 │
│  (Auth, Post, Category, Tag)            │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer                   │
│  (Business Logic & Validation)          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer (JPA)             │
│  (Data Access & Custom Queries)         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      PostgreSQL Database                │
└─────────────────────────────────────────┘
```

### Security Flow
```
Client Request
    │
    ├─► JWT Authentication Filter
    │       │
    │       ├─► Extract JWT Token
    │       ├─► Validate Token
    │       └─► Load User Details
    │
    └─► Spring Security
            │
            ├─► Role-Based Authorization
            └─► Controller Access
```

### Frontend Architecture
```
┌─────────────────────────────────────────┐
│         React Components                │
│  (Pages, Layout, Forms, Lists)          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Context API                     │
│  (AuthContext - User State)             │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer                   │
│  (API Calls with Axios)                 │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Backend API                     │
│  (Spring Boot REST Endpoints)           │
└─────────────────────────────────────────┘
```

## Project Structure

```
new_project/
├── backend/                          # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/blog/
│   │   │   │   ├── BlogApplication.java
│   │   │   │   ├── controller/       # REST Controllers
│   │   │   │   │   ├── AuthController.java
│   │   │   │   │   ├── PostController.java
│   │   │   │   │   ├── CategoryController.java
│   │   │   │   │   └── TagController.java
│   │   │   │   ├── service/         # Business Logic
│   │   │   │   │   ├── AuthService.java
│   │   │   │   │   ├── PostService.java
│   │   │   │   │   ├── CategoryService.java
│   │   │   │   │   └── TagService.java
│   │   │   │   ├── repository/      # Data Access
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── PostRepository.java
│   │   │   │   │   ├── CategoryRepository.java
│   │   │   │   │   └── TagRepository.java
│   │   │   │   ├── entity/          # JPA Entities
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Post.java
│   │   │   │   │   ├── Category.java
│   │   │   │   │   └── Tag.java
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   │   ├── AuthRequest.java
│   │   │   │   │   ├── AuthResponse.java
│   │   │   │   │   ├── PostDTO.java
│   │   │   │   │   └── ...
│   │   │   │   └── security/        # Security Configuration
│   │   │   │       ├── SecurityConfig.java
│   │   │   │       ├── JwtTokenProvider.java
│   │   │   │       ├── JwtAuthenticationFilter.java
│   │   │   │       ├── CustomUserDetailsService.java
│   │   │   │       └── UserPrincipal.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                    # Test Files
│   │       ├── java/com/blog/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── repository/
│   │       │   └── security/
│   │       └── resources/
│   │           └── application-test.properties
│   ├── build.gradle
│   ├── settings.gradle
│   └── gradlew
│
└── frontend/                         # React Frontend
    ├── public/
    │   └── index.html
    ├── src/
    │   ├── components/
    │   │   ├── auth/                # Authentication Components
    │   │   │   ├── Login.js
    │   │   │   └── Register.js
    │   │   ├── posts/               # Post Components
    │   │   │   ├── PostList.js
    │   │   │   ├── PostDetail.js
    │   │   │   └── PostForm.js
    │   │   ├── categories/          # Category Components
    │   │   ├── tags/                # Tag Components
    │   │   ├── layout/              # Layout Components
    │   │   │   ├── Navbar.js
    │   │   │   └── Layout.js
    │   │   └── ProtectedRoute.js
    │   ├── context/
    │   │   └── AuthContext.js      # Authentication Context
    │   ├── services/                # API Service Layer
    │   │   ├── api.js
    │   │   ├── authService.js
    │   │   ├── postService.js
    │   │   ├── categoryService.js
    │   │   └── tagService.js
    │   ├── pages/
    │   │   └── Home.js
    │   ├── App.js
    │   ├── index.js
    │   └── setupTests.js
    └── package.json
```

## Prerequisites

### Backend Requirements
- **Java 17** or higher ([Download](https://adoptium.net/))
- **PostgreSQL 12+** ([Download](https://www.postgresql.org/download/))
- **Gradle 8.5+** (included via Gradle Wrapper)

### Frontend Requirements
- **Node.js 16+** and **npm** ([Download](https://nodejs.org/))

### Database Setup
- PostgreSQL server running and accessible
- Database user with appropriate permissions

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd new_project
```

### 2. Database Setup

#### Create PostgreSQL Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE blogdb;

# Exit psql
\q
```

**Note for macOS Homebrew users**: If you get a "role postgres does not exist" error, use your macOS username instead:

```bash
# Connect using your username
psql -d postgres

# Create database
CREATE DATABASE blogdb;
\q
```

### 3. Backend Setup

```bash
cd backend

# Set environment variables (optional, see Configuration section)
export DB_USERNAME=your_username
export DB_PASSWORD=your_password

# Build the project (downloads dependencies)
./gradlew build

# Run the application
./gradlew bootRun
```

**Windows users:**
```bash
gradlew.bat build
gradlew.bat bootRun
```

The backend will start on `http://localhost:8080`

### 4. Frontend Setup

Open a **new terminal window** (keep backend running):

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The frontend will start on `http://localhost:3000` and automatically open in your browser.

## Configuration

### Environment Variables

You can configure the application using environment variables:

#### Backend Environment Variables

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DB_URL` | PostgreSQL database URL | `jdbc:postgresql://localhost:5432/blogdb` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | Secret key for JWT tokens | `your-secret-key-change-this-in-production...` |
| `JWT_EXPIRATION` | JWT token expiration (ms) | `86400000` (24 hours) |

**Setting Environment Variables:**

**Linux/Mac:**
```bash
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your-secure-random-secret-key
```

**Windows (PowerShell):**
```powershell
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
$env:JWT_SECRET="your-secure-random-secret-key"
```

**Windows (CMD):**
```cmd
set DB_USERNAME=your_username
set DB_PASSWORD=your_password
set JWT_SECRET=your-secure-random-secret-key
```

#### Frontend Environment Variables

Create a `.env` file in the `frontend` directory:

```env
REACT_APP_API_URL=http://localhost:8080/api
```

### Application Properties

Backend configuration is in `backend/src/main/resources/application.properties`:

- **Server Port**: 8080
- **Database**: Configured via environment variables
- **JPA**: Auto-update schema, SQL logging enabled
- **CORS**: Configured for `http://localhost:3000`

## Running the Application

### Development Mode

#### Start Backend
```bash
cd backend
./gradlew bootRun
```

#### Start Frontend (in separate terminal)
```bash
cd frontend
npm start
```

### Production Build

#### Build Backend JAR
```bash
cd backend
./gradlew bootJar
java -jar build/libs/blog-0.0.1-SNAPSHOT.jar
```

#### Build Frontend
```bash
cd frontend
npm run build
# Serve the build/ directory with a web server
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:** Same as register

### Post Endpoints

#### Get All Posts
```http
GET /api/posts
```

#### Get Post by ID
```http
GET /api/posts/{id}
```

#### Create Post (Requires Authentication)
```http
POST /api/posts
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "My First Post",
  "content": "<p>This is the post content</p>",
  "categoryId": 1,
  "tagIds": [1, 2, 3]
}
```

#### Update Post (Requires Authentication - Author or Admin)
```http
PUT /api/posts/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Updated Title",
  "content": "<p>Updated content</p>",
  "categoryId": 2,
  "tagIds": [4, 5]
}
```

#### Delete Post (Requires Authentication - Author or Admin)
```http
DELETE /api/posts/{id}
Authorization: Bearer {token}
```

### Category Endpoints

#### Get All Categories
```http
GET /api/categories
```

#### Get Category by ID
```http
GET /api/categories/{id}
```

#### Create Category
```http
POST /api/categories
Content-Type: application/json

{
  "name": "Technology",
  "description": "Posts about technology"
}
```

#### Update Category
```http
PUT /api/categories/{id}
Content-Type: application/json

{
  "name": "Tech",
  "description": "Updated description"
}
```

#### Delete Category
```http
DELETE /api/categories/{id}
```

### Tag Endpoints

#### Get All Tags
```http
GET /api/tags
```

#### Get Tag by ID
```http
GET /api/tags/{id}
```

#### Create Tag
```http
POST /api/tags
Content-Type: application/json

{
  "name": "Java"
}
```

#### Update Tag
```http
PUT /api/tags/{id}
Content-Type: application/json

{
  "name": "Spring Boot"
}
```

#### Delete Tag
```http
DELETE /api/tags/{id}
```

## 🧪 Testing

### Backend Tests

```bash
cd backend

# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests PostServiceTest

# Generate test report
./gradlew test
# Report available at: build/reports/tests/test/index.html
```

**Test Coverage:**
- Repository tests (User, Post)
- Service tests (Auth, Post, Category, Tag)
- Controller integration tests (Auth, Post)
- Security tests (JWT Token Provider)

### Frontend Tests

```bash
cd frontend

# Run tests in watch mode
npm test

# Run tests once
CI=true npm test

# Run tests with coverage
npm test -- --coverage
```

**Test Coverage:**
- Service tests (authService, postService)
- Component tests (Login, PostList)
- Context tests (AuthContext)

## Security Features

### Authentication
- **JWT (JSON Web Tokens)** for stateless authentication
- **BCrypt** password hashing
- **Token expiration** (configurable, default 24 hours)

### Authorization
- **Role-based access control** (ROLE_USER, ROLE_ADMIN)
- **Resource-level permissions** (users can only edit/delete their own posts)
- **Admin override** (admins can manage all resources)

### Security Measures
- **CORS** configuration for allowed origins
- **XSS Protection** via DOMPurify for HTML content sanitization
- **SQL Injection Prevention** via JPA parameterized queries
- **Password Validation** (minimum 6 characters)
- **Input Validation** using Jakarta Validation

### Security Configuration
- Public endpoints: `/api/auth/register`, `/api/auth/login`
- Protected endpoints: All other `/api/*` endpoints require authentication
- JWT token sent in `Authorization: Bearer {token}` header

## Troubleshooting

### Backend Issues

#### PostgreSQL Connection Failed
**Symptoms:** `Connection refused` or `FATAL: role "postgres" does not exist`

**Solutions:**
1. **Check PostgreSQL is running:**
   ```bash
   # Mac/Linux
   pg_isready
   brew services list | grep postgresql
   
   # Linux
   sudo systemctl status postgresql
   ```

2. **Use your system username** (for Homebrew PostgreSQL on macOS):
   ```bash
   export DB_USERNAME=$(whoami)
   export DB_PASSWORD=
   ```

3. **Verify database exists:**
   ```bash
   psql -U your_username -l
   ```

#### Port 8080 Already in Use
**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port in application.properties
server.port=8081
```

#### Gradle Build Fails
**Solutions:**
1. **Clean and rebuild:**
   ```bash
   ./gradlew clean build
   ```

2. **Stop Gradle daemon:**
   ```bash
   ./gradlew --stop
   ```

3. **Check Java version:**
   ```bash
   java -version  # Should be 17+
   ```

### Frontend Issues

#### npm install Fails
**Solutions:**
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

#### Port 3000 Already in Use
**Solution:**
```bash
# Find and kill process
lsof -ti:3000 | xargs kill -9

# Or use different port
PORT=3001 npm start
```

#### CORS Errors
**Solution:**
- Ensure backend is running on `http://localhost:8080`
- Check `app.cors.allowed-origins` in `application.properties`
- Verify frontend is running on `http://localhost:3000`

### Database Issues

#### Migration/Schema Issues
**Solution:**
```bash
# Drop and recreate database
psql -U your_username
DROP DATABASE blogdb;
CREATE DATABASE blogdb;
\q

# Restart backend (will auto-create schema)
```

## Production Deployment

### Backend Deployment

1. **Set Production Environment Variables:**
   ```bash
   export DB_URL=jdbc:postgresql://prod-db-host:5432/blogdb
   export DB_USERNAME=prod_user
   export DB_PASSWORD=secure_password
   export JWT_SECRET=very-secure-random-secret-key-min-256-bits
   ```

2. **Build JAR:**
   ```bash
   ./gradlew clean bootJar
   ```

3. **Run JAR:**
   ```bash
   java -jar build/libs/blog-0.0.1-SNAPSHOT.jar
   ```

4. **Use Process Manager** (PM2, systemd, etc.)

### Frontend Deployment

1. **Build for Production:**
   ```bash
   npm run build
   ```

2. **Serve `build/` directory** with:
   - Nginx
   - Apache
   - AWS S3 + CloudFront
   - Netlify
   - Vercel

3. **Update API URL:**
   ```env
   REACT_APP_API_URL=https://api.yourdomain.com/api
   ```

### Production Checklist

- [ ] Set strong `JWT_SECRET` (minimum 256 bits)
- [ ] Use production database with proper credentials
- [ ] Configure CORS for production domain
- [ ] Enable HTTPS
- [ ] Set up database backups
- [ ] Configure logging
- [ ] Set up monitoring
- [ ] Review security settings
- [ ] Test all endpoints
- [ ] Load testing


---
