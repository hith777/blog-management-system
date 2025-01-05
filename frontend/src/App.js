import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/layout/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import Home from './pages/Home';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import PostList from './components/posts/PostList';
import PostForm from './components/posts/PostForm';
import PostDetail from './components/posts/PostDetail';
import CategoryList from './components/categories/CategoryList';
import CategoryForm from './components/categories/CategoryForm';
import TagList from './components/tags/TagList';
import TagForm from './components/tags/TagForm';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            
            <Route
              path="/posts"
              element={
                <ProtectedRoute>
                  <PostList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/posts/new"
              element={
                <ProtectedRoute>
                  <PostForm />
                </ProtectedRoute>
              }
            />
            <Route
              path="/posts/:id"
              element={
                <ProtectedRoute>
                  <PostDetail />
                </ProtectedRoute>
              }
            />
            <Route
              path="/posts/:id/edit"
              element={
                <ProtectedRoute>
                  <PostForm />
                </ProtectedRoute>
              }
            />
            
            <Route
              path="/categories"
              element={
                <ProtectedRoute>
                  <CategoryList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/categories/new"
              element={
                <ProtectedRoute>
                  <CategoryForm />
                </ProtectedRoute>
              }
            />
            <Route
              path="/categories/:id/edit"
              element={
                <ProtectedRoute>
                  <CategoryForm />
                </ProtectedRoute>
              }
            />
            
            <Route
              path="/tags"
              element={
                <ProtectedRoute>
                  <TagList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/tags/new"
              element={
                <ProtectedRoute>
                  <TagForm />
                </ProtectedRoute>
              }
            />
            <Route
              path="/tags/:id/edit"
              element={
                <ProtectedRoute>
                  <TagForm />
                </ProtectedRoute>
              }
            />
            
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
}

export default App;
