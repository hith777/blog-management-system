#!/bin/bash
set -e

cd /Users/hitheshk/Projects/new_project

# Initialize git if needed
if [ ! -d .git ]; then
    git init
    git config user.name "Developer"
    git config user.email "dev@example.com"
fi

# Commit 1: Backend setup
git add backend/build.gradle backend/settings.gradle backend/gradle/ backend/src/main/java/com/blog/BlogApplication.java backend/src/main/resources/application.properties backend/.gitignore backend/README.md
git commit -m "feat: initialize Spring Boot project"

# Commit 2: Entities
git add backend/src/main/java/com/blog/entity/
git commit -m "feat: add JPA entities"

# Commit 3: Repositories
git add backend/src/main/java/com/blog/repository/
git commit -m "feat: add JPA repositories"

# Commit 4: DTOs
git add backend/src/main/java/com/blog/dto/
git commit -m "feat: add DTOs"

# Commit 5: Security
git add backend/src/main/java/com/blog/security/
git commit -m "feat: add Spring Security with JWT"

# Commit 6: Services
git add backend/src/main/java/com/blog/service/
git commit -m "feat: add service layer"

# Commit 7: Controllers
git add backend/src/main/java/com/blog/controller/
git commit -m "feat: add REST controllers"

# Commit 8: Frontend setup
git add frontend/package.json frontend/public/ frontend/src/index.js frontend/src/index.css frontend/src/App.js frontend/src/App.css frontend/.gitignore frontend/README.md
git commit -m "feat: initialize React app"

# Commit 9: API services
git add frontend/src/services/
git commit -m "feat: add API services"

# Commit 10: Auth context
git add frontend/src/context/
git commit -m "feat: add AuthContext"

# Commit 11: Auth components
git add frontend/src/components/auth/
git commit -m "feat: add auth components"

# Commit 12: Layout components
git add frontend/src/components/layout/
git commit -m "feat: add layout components"

# Commit 13: Post components
git add frontend/src/components/posts/
git commit -m "feat: add post components"

# Commit 14: Category and Tag components
git add frontend/src/components/categories/ frontend/src/components/tags/
git commit -m "feat: add category and tag components"

# Commit 15: Routing
git add frontend/src/components/ProtectedRoute.js frontend/src/pages/ frontend/src/App.js
git commit -m "feat: add routing"

# Commit 16: Styling
git add frontend/src/index.css frontend/src/App.css
git commit -m "style: improve styling"

echo "All commits created successfully!"
git log --oneline

