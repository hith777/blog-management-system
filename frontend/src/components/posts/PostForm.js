import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { postService } from '../../services/postService';
import { categoryService } from '../../services/categoryService';
import { tagService } from '../../services/tagService';
import './Post.css';

const PostForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  const [formData, setFormData] = useState({
    title: '',
    content: '',
    categoryId: '',
    tagIds: [],
  });
  const [categories, setCategories] = useState([]);
  const [tags, setTags] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadCategories();
    loadTags();
    if (isEdit) {
      loadPost();
    }
  }, [id, isEdit]);

  const loadPost = async () => {
    try {
      const post = await postService.getById(id);
      setFormData({
        title: post.title,
        content: post.content,
        categoryId: post.categoryId ? String(post.categoryId) : '',
        tagIds: post.tags ? post.tags.map((tag) => tag.id) : [],
      });
    } catch (err) {
      setError('Failed to load post');
    }
  };

  const loadCategories = async () => {
    try {
      const data = await categoryService.getAll();
      setCategories(data);
    } catch (err) {
      console.error('Failed to load categories');
    }
  };

  const loadTags = async () => {
    try {
      const data = await tagService.getAll();
      setTags(data);
    } catch (err) {
      console.error('Failed to load tags');
    }
  };

  const handleChange = (e) => {
    let value = e.target.value;
    // Keep categoryId as string for select element, convert to number/null only on submit
    if (e.target.name === 'categoryId') {
      // Keep as string (empty string for "no category" option)
      value = e.target.value;
    }
    setFormData({
      ...formData,
      [e.target.name]: value,
    });
  };

  const handleContentChange = (value) => {
    setFormData({
      ...formData,
      content: value,
    });
  };

  const handleTagChange = (tagId) => {
    setFormData({
      ...formData,
      tagIds: formData.tagIds.includes(tagId)
        ? formData.tagIds.filter((id) => id !== tagId)
        : [...formData.tagIds, tagId],
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const postData = {
        title: formData.title,
        content: formData.content,
        // Convert empty string to null, otherwise convert to number
        categoryId: formData.categoryId && formData.categoryId !== '' 
          ? Number(formData.categoryId) 
          : null,
        tagIds: formData.tagIds,
      };

      if (isEdit) {
        await postService.update(id, postData);
      } else {
        await postService.create(postData);
      }
      navigate('/posts');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save post');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="post-form-container">
      <h1>{isEdit ? 'Edit Post' : 'Create New Post'}</h1>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit} className="post-form">
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="categoryId">Category</label>
          <select
            id="categoryId"
            name="categoryId"
            value={formData.categoryId}
            onChange={handleChange}
          >
            <option value="">Select a category</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Tags</label>
          <div className="tags-checkbox-group">
            {tags.map((tag) => (
              <label key={tag.id} className="checkbox-label">
                <input
                  type="checkbox"
                  checked={formData.tagIds.includes(tag.id)}
                  onChange={() => handleTagChange(tag.id)}
                />
                {tag.name}
              </label>
            ))}
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="content">Content</label>
          <ReactQuill
            theme="snow"
            value={formData.content}
            onChange={handleContentChange}
            style={{ minHeight: '300px', marginBottom: '50px' }}
          />
        </div>

        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Saving...' : isEdit ? 'Update Post' : 'Create Post'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/posts')}
            className="btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default PostForm;

