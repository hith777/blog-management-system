import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import PostList from '../PostList';
import { postService } from '../../../services/postService';

jest.mock('../../../services/postService');

// Mock window.confirm
global.confirm = jest.fn();

describe('PostList', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const mockPosts = [
    {
      id: 1,
      title: 'Post 1',
      content: 'Content 1'.repeat(20),
      authorUsername: 'author1',
      categoryName: 'Tech',
      createdAt: '2024-01-01T00:00:00',
      tags: [{ id: 1, name: 'Java' }],
    },
    {
      id: 2,
      title: 'Post 2',
      content: 'Content 2'.repeat(20),
      authorUsername: 'author2',
      categoryName: null,
      createdAt: '2024-01-02T00:00:00',
      tags: [],
    },
  ];

  const renderPostList = () => {
    return render(
      <BrowserRouter>
        <PostList />
      </BrowserRouter>
    );
  };

  it('should render loading state initially', () => {
    postService.getAll.mockImplementation(() => new Promise(() => {})); // Never resolves

    renderPostList();

    expect(screen.getByText('Loading posts...')).toBeInTheDocument();
  });

  it('should render posts after loading', async () => {
    postService.getAll.mockResolvedValue(mockPosts);

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('Post 1')).toBeInTheDocument();
      expect(screen.getByText('Post 2')).toBeInTheDocument();
    });

    expect(screen.getByText('By author1')).toBeInTheDocument();
    expect(screen.getByText('By author2')).toBeInTheDocument();
    expect(screen.getByText('Tech')).toBeInTheDocument();
  });

  it('should show empty state when no posts', async () => {
    postService.getAll.mockResolvedValue([]);

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('No posts found. Create your first post!')).toBeInTheDocument();
    });
  });

  it('should show error message on load failure', async () => {
    postService.getAll.mockRejectedValue(new Error('Failed to fetch'));

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('Failed to load posts')).toBeInTheDocument();
    });
  });

  it('should delete post when confirmed', async () => {
    postService.getAll.mockResolvedValue(mockPosts);
    postService.delete.mockResolvedValue({});
    global.confirm.mockReturnValue(true);

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('Post 1')).toBeInTheDocument();
    });

    const deleteButtons = screen.getAllByText('Delete');
    fireEvent.click(deleteButtons[0]);

    await waitFor(() => {
      expect(global.confirm).toHaveBeenCalledWith('Are you sure you want to delete this post?');
      expect(postService.delete).toHaveBeenCalledWith(1);
      expect(postService.getAll).toHaveBeenCalledTimes(2); // Initial load + reload after delete
    });
  });

  it('should not delete post when not confirmed', async () => {
    postService.getAll.mockResolvedValue(mockPosts);
    global.confirm.mockReturnValue(false);

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('Post 1')).toBeInTheDocument();
    });

    const deleteButtons = screen.getAllByText('Delete');
    fireEvent.click(deleteButtons[0]);

    await waitFor(() => {
      expect(global.confirm).toHaveBeenCalled();
      expect(postService.delete).not.toHaveBeenCalled();
    });
  });

  it('should show error message on delete failure', async () => {
    postService.getAll.mockResolvedValue(mockPosts);
    postService.delete.mockRejectedValue(new Error('Failed to delete'));
    global.confirm.mockReturnValue(true);

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('Post 1')).toBeInTheDocument();
    });

    const deleteButtons = screen.getAllByText('Delete');
    fireEvent.click(deleteButtons[0]);

    await waitFor(() => {
      expect(screen.getByText('Failed to delete post')).toBeInTheDocument();
    });
  });

  it('should render tags when available', async () => {
    postService.getAll.mockResolvedValue(mockPosts);

    renderPostList();

    await waitFor(() => {
      expect(screen.getByText('Java')).toBeInTheDocument();
    });
  });

  it('should render create post link', async () => {
    postService.getAll.mockResolvedValue(mockPosts);

    renderPostList();

    await waitFor(() => {
      const createLink = screen.getByText('Create New Post');
      expect(createLink).toBeInTheDocument();
      expect(createLink.closest('a')).toHaveAttribute('href', '/posts/new');
    });
  });
});

