import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import DOMPurify from 'dompurify';
import { postService } from '../../services/postService';
import { useAuth } from '../../context/AuthContext';
import './Post.css';

const PostDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPost();
  }, [id]);

  const loadPost = async () => {
    try {
      setLoading(true);
      const data = await postService.getById(id);
      setPost(data);
    } catch (err) {
      setError('Failed to load post');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this post?')) {
      try {
        await postService.delete(id);
        navigate('/posts');
      } catch (err) {
        setError('Failed to delete post');
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading post...</div>;
  }

  if (error || !post) {
    return <div className="error-message">{error || 'Post not found'}</div>;
  }

  const canEdit = user && (user.id === post.authorId || user.roles?.includes('ROLE_ADMIN'));

  return (
    <div className="post-detail-container">
      <div className="post-detail-header">
        <Link to="/posts" className="back-link">← Back to Posts</Link>
        {canEdit && (
          <div className="post-actions">
            <Link to={`/posts/${id}/edit`} className="btn-edit">
              Edit
            </Link>
            <button onClick={handleDelete} className="btn-delete">
              Delete
            </button>
          </div>
        )}
      </div>

      <article className="post-detail">
        <h1>{post.title}</h1>
        <div className="post-meta">
          <span>By {post.authorUsername}</span>
          {post.categoryName && <span> • Category: {post.categoryName}</span>}
          <span> • {new Date(post.createdAt).toLocaleDateString()}</span>
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
        <div
          className="post-content"
          dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(post.content) }}
        />
      </article>
    </div>
  );
};

export default PostDetail;

