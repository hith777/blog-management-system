import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          Blog Management
        </Link>
        <div className="navbar-menu">
          {isAuthenticated ? (
            <>
              <Link to="/posts" className="navbar-link">
                Posts
              </Link>
              <Link to="/categories" className="navbar-link">
                Categories
              </Link>
              <Link to="/tags" className="navbar-link">
                Tags
              </Link>
              <div className="navbar-user">
                <span className="navbar-username">{user?.username}</span>
                <button onClick={handleLogout} className="btn-logout">
                  Logout
                </button>
              </div>
            </>
          ) : (
            <>
              <Link to="/login" className="navbar-link">
                Login
              </Link>
              <Link to="/register" className="navbar-link">
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

