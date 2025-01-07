import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { categoryService } from '../../services/categoryService';
import './Category.css';

const CategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const data = await categoryService.getAll();
      setCategories(data);
    } catch (err) {
      setError('Failed to load categories');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this category?')) {
      try {
        await categoryService.delete(id);
        loadCategories();
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to delete category');
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading categories...</div>;
  }

  return (
    <div className="category-list-container">
      <div className="category-list-header">
        <h1>Categories</h1>
        <Link to="/categories/new" className="btn-primary">
          Create New Category
        </Link>
      </div>
      {error && <div className="error-message">{error}</div>}
      {categories.length === 0 ? (
        <div className="empty-state">No categories found. Create your first category!</div>
      ) : (
        <div className="categories-table">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {categories.map((category) => (
                <tr key={category.id}>
                  <td>{category.name}</td>
                  <td>{category.description || '-'}</td>
                  <td>{new Date(category.createdAt).toLocaleDateString()}</td>
                  <td>
                    <Link to={`/categories/${category.id}/edit`} className="btn-edit">
                      Edit
                    </Link>
                    <button
                      onClick={() => handleDelete(category.id)}
                      className="btn-delete"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default CategoryList;

