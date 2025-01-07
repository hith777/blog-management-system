import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { tagService } from '../../services/tagService';
import './Tag.css';

const TagList = () => {
  const [tags, setTags] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadTags();
  }, []);

  const loadTags = async () => {
    try {
      setLoading(true);
      const data = await tagService.getAll();
      setTags(data);
    } catch (err) {
      setError('Failed to load tags');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this tag?')) {
      try {
        await tagService.delete(id);
        loadTags();
      } catch (err) {
        setError('Failed to delete tag');
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading tags...</div>;
  }

  return (
    <div className="tag-list-container">
      <div className="tag-list-header">
        <h1>Tags</h1>
        <Link to="/tags/new" className="btn-primary">
          Create New Tag
        </Link>
      </div>
      {error && <div className="error-message">{error}</div>}
      {tags.length === 0 ? (
        <div className="empty-state">No tags found. Create your first tag!</div>
      ) : (
        <div className="tags-grid">
          {tags.map((tag) => (
            <div key={tag.id} className="tag-card">
              <h3>{tag.name}</h3>
              <div className="tag-meta">
                Created: {new Date(tag.createdAt).toLocaleDateString()}
              </div>
              <div className="tag-actions">
                <Link to={`/tags/${tag.id}/edit`} className="btn-edit">
                  Edit
                </Link>
                <button onClick={() => handleDelete(tag.id)} className="btn-delete">
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TagList;

