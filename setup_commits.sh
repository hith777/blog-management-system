#!/bin/bash

cd "$(dirname "$0")"

echo "Initializing git repository..."
git init
git config user.name "Developer" || true
git config user.email "dev@example.com" || true

echo "Creating commits..."

echo "Commit 1: Backend setup"
git add backend/build.gradle backend/settings.gradle backend/gradle/ backend/src/main/java/com/blog/BlogApplication.java backend/src/main/resources/application.properties backend/.gitignore backend/README.md 2>&1
git commit -m "feat: initialize Spring Boot project" 2>&1

echo "Commit 2: Entities"
git add backend/src/main/java/com/blog/entity/ 2>&1
git commit -m "feat: add JPA entities" 2>&1

echo "Commit 3: Repositories"
git add backend/src/main/java/com/blog/repository/ 2>&1
git commit -m "feat: add JPA repositories" 2>&1

echo "Commit 4: DTOs"
git add backend/src/main/java/com/blog/dto/ 2>&1
git commit -m "feat: add DTOs" 2>&1

echo "Commit 5: Security"
git add backend/src/main/java/com/blog/security/ 2>&1
git commit -m "feat: add Spring Security with JWT" 2>&1

echo "Commit 6: Services"
git add backend/src/main/java/com/blog/service/ 2>&1
git commit -m "feat: add service layer" 2>&1

echo "Commit 7: Controllers"
git add backend/src/main/java/com/blog/controller/ 2>&1
git commit -m "feat: add REST controllers" 2>&1

echo "Commit 8: Frontend setup"
git add frontend/package.json frontend/public/ frontend/src/index.js frontend/src/index.css frontend/src/App.js frontend/src/App.css frontend/.gitignore frontend/README.md 2>&1
git commit -m "feat: initialize React app" 2>&1

echo "Commit 9: API services"
git add frontend/src/services/ 2>&1
git commit -m "feat: add API services" 2>&1

echo "Commit 10: Auth context"
git add frontend/src/context/ 2>&1
git commit -m "feat: add AuthContext" 2>&1

echo "Commit 11: Auth components"
git add frontend/src/components/auth/ 2>&1
git commit -m "feat: add auth components" 2>&1

echo "Commit 12: Layout components"
git add frontend/src/components/layout/ 2>&1
git commit -m "feat: add layout components" 2>&1

echo "Commit 13: Post components"
git add frontend/src/components/posts/ 2>&1
git commit -m "feat: add post components" 2>&1

echo "Commit 14: Category and Tag components"
git add frontend/src/components/categories/ frontend/src/components/tags/ 2>&1
git commit -m "feat: add category and tag components" 2>&1

echo "Commit 15: Routing"
git add frontend/src/components/ProtectedRoute.js frontend/src/pages/ frontend/src/App.js 2>&1
git commit -m "feat: add routing" 2>&1

echo "Commit 16: Styling"
git add frontend/src/index.css frontend/src/App.css 2>&1
git commit -m "style: improve styling" 2>&1

echo ""
echo "=== All commits created! ==="
echo ""
git log --oneline --all

