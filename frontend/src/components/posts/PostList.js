import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { postService } from '../../services/postService';
import './Post.css';

const PostList = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPosts();
  }, []);

  const loadPosts = async () => {
    try {
      setLoading(true);
      const data = await postService.getAll();
      setPosts(data);
    } catch (err) {
      setError('Failed to load posts');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this post?')) {
      try {
        await postService.delete(id);
        loadPosts();
      } catch (err) {
        setError('Failed to delete post');
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading posts...</div>;
  }

  return (
    <div className="post-list-container">
      <div className="post-list-header">
        <h1>Posts</h1>
        <Link to="/posts/new" className="btn-primary">
          Create New Post
        </Link>
      </div>
      {error && <div className="error-message">{error}</div>}
      {posts.length === 0 ? (
        <div className="empty-state">No posts found. Create your first post!</div>
      ) : (
        <div className="posts-grid">
          {posts.map((post) => (
            <div key={post.id} className="post-card">
              <h3>
                <Link to={`/posts/${post.id}`}>{post.title}</Link>
              </h3>
              <div className="post-meta">
                <span>By {post.authorUsername}</span>
                {post.categoryName && <span> • {post.categoryName}</span>}
                <span> • {new Date(post.createdAt).toLocaleDateString()}</span>
              </div>
              <div className="post-content-preview">
                {post.content.substring(0, 150)}...
              </div>
              {post.tags && post.tags.length > 0 && (
                <div className="post-tags">
                  {post.tags.map((tag) => (
                    <span key={tag.id} className="tag">
                      {tag.name}
                    </span>
                  ))}
                </div>
              )}
              <div className="post-actions">
                <Link to={`/posts/${post.id}/edit`} className="btn-edit">
                  Edit
                </Link>
                <button onClick={() => handleDelete(post.id)} className="btn-delete">
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

export default PostList;

