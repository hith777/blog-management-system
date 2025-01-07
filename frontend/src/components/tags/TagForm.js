import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { tagService } from '../../services/tagService';
import './Tag.css';

const TagForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  const [formData, setFormData] = useState({
    name: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (isEdit) {
      loadTag();
    }
  }, [id, isEdit]);

  const loadTag = async () => {
    try {
      const tag = await tagService.getById(id);
      setFormData({
        name: tag.name,
      });
    } catch (err) {
      setError('Failed to load tag');
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
        await tagService.update(id, formData);
      } else {
        await tagService.create(formData);
      }
      navigate('/tags');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save tag');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="tag-form-container">
      <h1>{isEdit ? 'Edit Tag' : 'Create New Tag'}</h1>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit} className="tag-form">
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
        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Saving...' : isEdit ? 'Update Tag' : 'Create Tag'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/tags')}
            className="btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default TagForm;

