#!/bin/bash
set -e

cd "$(dirname "$0")"

# Initialize git if needed
if [ ! -d .git ]; then
    git init
    git config user.name "Developer"
    git config user.email "dev@example.com"
fi

echo "Creating organized commits..."

# Commit 1: Backend setup
echo "Commit 1: Backend setup"
git add backend/build.gradle backend/settings.gradle backend/gradle/ backend/src/main/java/com/blog/BlogApplication.java backend/src/main/resources/application.properties backend/.gitignore backend/README.md
git commit -m "feat: initialize Spring Boot project with Gradle" || true

# Commit 2: Entities
echo "Commit 2: Entities"
git add backend/src/main/java/com/blog/entity/
git commit -m "feat: add JPA entities with relationships" || true

# Commit 3: Repositories
echo "Commit 3: Repositories"
git add backend/src/main/java/com/blog/repository/
git commit -m "feat: add JPA repositories with custom queries" || true

# Commit 4: DTOs
echo "Commit 4: DTOs"
git add backend/src/main/java/com/blog/dto/
git commit -m "feat: add DTOs for request/response" || true

# Commit 5: Security
echo "Commit 5: Security"
git add backend/src/main/java/com/blog/security/
git commit -m "feat: configure Spring Security with JWT" || true

# Commit 6: Services
echo "Commit 6: Services"
git add backend/src/main/java/com/blog/service/
git commit -m "feat: implement service layer with CRUD operations" || true

# Commit 7: Controllers
echo "Commit 7: Controllers"
git add backend/src/main/java/com/blog/controller/
git commit -m "feat: add REST controllers" || true

# Commit 8: Frontend setup
echo "Commit 8: Frontend setup"
git add frontend/package.json frontend/public/ frontend/src/index.js frontend/src/index.css frontend/src/App.js frontend/src/App.css frontend/.gitignore frontend/README.md
git commit -m "feat: initialize React app" || true

# Commit 9: API services
echo "Commit 9: API services"
git add frontend/src/services/
git commit -m "feat: add API service layer" || true

# Commit 10: Auth context
echo "Commit 10: Auth context"
git add frontend/src/context/
git commit -m "feat: add AuthContext" || true

# Commit 11: Auth components
echo "Commit 11: Auth components"
git add frontend/src/components/auth/
git commit -m "feat: add authentication components" || true

# Commit 12: Layout components
echo "Commit 12: Layout components"
git add frontend/src/components/layout/
git commit -m "feat: add layout components" || true

# Commit 13: Post components
echo "Commit 13: Post components"
git add frontend/src/components/posts/
git commit -m "feat: add post components with rich text editor" || true

# Commit 14: Category and Tag components
echo "Commit 14: Category and Tag components"
git add frontend/src/components/categories/ frontend/src/components/tags/
git commit -m "feat: add category and tag components" || true

# Commit 15: Routing
echo "Commit 15: Routing"
git add frontend/src/components/ProtectedRoute.js frontend/src/pages/ frontend/src/App.js
git commit -m "feat: add routing with protected routes" || true

# Commit 16: Styling
echo "Commit 16: Styling"
git add frontend/src/index.css frontend/src/App.css
git commit -m "style: improve UI styling and responsiveness" || true

# Commit any remaining files
echo "Commit 17: Remaining files"
git add -A
if [ -n "$(git status --porcelain)" ]; then
    git commit -m "chore: add remaining project files" || true
fi

echo ""
echo "=== All commits created! ==="
echo ""
git log --oneline

