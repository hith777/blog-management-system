# Blog Management System

Full-stack blog management application with React frontend and Spring Boot backend.

## Prerequisites

### Backend
- **Java 17** or higher
- **PostgreSQL** 12+ (installed and running)
- **Gradle** 8.5+ (or use Gradle Wrapper included in project)

### Frontend
- **Node.js** 16+ and **npm**

## Quick Start

### 1. Database Setup

First, create a PostgreSQL database:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE blogdb;

# Exit psql
\q
```

### 2. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Build the project (Gradle will download dependencies automatically)
./gradlew build

# Run the application
./gradlew bootRun
```

**Or on Windows:**
```bash
gradlew.bat build
gradlew.bat bootRun
```

The backend will start on `http://localhost:8080`

#### Environment Variables (Optional)

You can set environment variables to override defaults:

```bash
# Linux/Mac
export DB_URL=jdbc:postgresql://localhost:5432/blogdb
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export JWT_SECRET=your-secure-random-secret-key

# Windows (PowerShell)
$env:DB_URL="jdbc:postgresql://localhost:5432/blogdb"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_password"
$env:JWT_SECRET="your-secure-random-secret-key"
```

**Default values** (if not set):
- `DB_URL`: `jdbc:postgresql://localhost:5432/blogdb`
- `DB_USERNAME`: `postgres`
- `DB_PASSWORD`: `postgres`
- `JWT_SECRET`: `your-secret-key-change-this-in-production-to-a-secure-random-string`

### 3. Frontend Setup

Open a **new terminal window** (keep backend running):

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies (first time only)
npm install

# Start the development server
npm start
```

The frontend will start on `http://localhost:3000` and automatically open in your browser.

## Running Tests

### Backend Tests

```bash
cd backend
./gradlew test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## Application URLs

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **API Documentation**: Check individual controller endpoints

## Default API Endpoints

- **Authentication**: 
  - `POST /api/auth/register` - Register new user
  - `POST /api/auth/login` - Login user
- **Posts**: 
  - `GET /api/posts` - Get all posts
  - `GET /api/posts/{id}` - Get post by ID
  - `POST /api/posts` - Create post (requires auth)
  - `PUT /api/posts/{id}` - Update post (requires auth)
  - `DELETE /api/posts/{id}` - Delete post (requires auth)
- **Categories**: `/api/categories`
- **Tags**: `/api/tags`

## Troubleshooting

### Backend won't start

1. **Check PostgreSQL is running:**
   ```bash
   # Mac/Linux
   pg_isready
   
   # Or check service status
   # Mac (Homebrew)
   brew services list
   
   # Linux
   sudo systemctl status postgresql
   ```

2. **Check database exists:**
   ```bash
   psql -U postgres -l
   ```

3. **Check port 8080 is available:**
   ```bash
   # Mac/Linux
   lsof -i :8080
   
   # Windows
   netstat -ano | findstr :8080
   ```

### Frontend won't start

1. **Check Node.js version:**
   ```bash
   node --version  # Should be 16+
   ```

2. **Clear npm cache and reinstall:**
   ```bash
   npm cache clean --force
   rm -rf node_modules package-lock.json
   npm install
   ```

3. **Check port 3000 is available:**
   ```bash
   # Mac/Linux
   lsof -i :3000
   ```

### Database Connection Issues

- Verify PostgreSQL is running
- Check database credentials match your PostgreSQL setup
- Ensure database `blogdb` exists
- Check PostgreSQL is listening on port 5432

## Development Tips

1. **Backend logs**: Check console output for SQL queries and application logs
2. **Frontend hot reload**: Changes to React components will automatically reload
3. **API testing**: Use Postman or curl to test API endpoints directly
4. **Database inspection**: Use `psql` or pgAdmin to inspect database tables

## Production Deployment

For production deployment:
1. Set strong `JWT_SECRET` environment variable
2. Use production database credentials
3. Configure CORS allowed origins
4. Build frontend: `npm run build`
5. Use production build tools for backend

