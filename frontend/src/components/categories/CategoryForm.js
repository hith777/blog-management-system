import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { categoryService } from '../../services/categoryService';
import './Category.css';

const CategoryForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  const [formData, setFormData] = useState({
    name: '',
    description: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isEdit) {
      loadCategory();
    }
  }, [id, isEdit]);

  const loadCategory = async () => {
    try {
      const category = await categoryService.getById(id);
      setFormData({
        name: category.name,
        description: category.description || '',
      });
    } catch (err) {
      setError('Failed to load category');
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isEdit) {
        await categoryService.update(id, formData);
      } else {
        await categoryService.create(formData);
      }
      navigate('/categories');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save category');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="category-form-container">
      <h1>{isEdit ? 'Edit Category' : 'Create New Category'}</h1>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit} className="category-form">
        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
          />
        </div>
        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Saving...' : isEdit ? 'Update Category' : 'Create Category'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/categories')}
            className="btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default CategoryForm;

