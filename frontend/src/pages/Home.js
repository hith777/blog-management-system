import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Home.css';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="home-container">
      <div className="home-content">
        <h1>Welcome to Blog Management System</h1>
        <p>A full-featured blog management application with authentication, posts, categories, and tags.</p>
        {isAuthenticated ? (
          <div className="home-actions">
            <Link to="/posts" className="btn-primary">
              View Posts
            </Link>
            <Link to="/posts/new" className="btn-secondary">
              Create Post
            </Link>
          </div>
        ) : (
          <div className="home-actions">
            <Link to="/login" className="btn-primary">
              Login
            </Link>
            <Link to="/register" className="btn-secondary">
              Register
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;

