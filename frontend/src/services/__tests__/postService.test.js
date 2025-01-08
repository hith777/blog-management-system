import { postService } from '../postService';
import api from '../api';

jest.mock('../api');

describe('postService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getAll', () => {
    it('should fetch all posts', async () => {
      const mockPosts = [
        { id: 1, title: 'Post 1', content: 'Content 1' },
        { id: 2, title: 'Post 2', content: 'Content 2' },
      ];
      api.get.mockResolvedValue({ data: mockPosts });

      const result = await postService.getAll();

      expect(api.get).toHaveBeenCalledWith('/posts');
      expect(result).toEqual(mockPosts);
    });
  });

  describe('getById', () => {
    it('should fetch a post by id', async () => {
      const mockPost = { id: 1, title: 'Post 1', content: 'Content 1' };
      api.get.mockResolvedValue({ data: mockPost });

      const result = await postService.getById(1);

      expect(api.get).toHaveBeenCalledWith('/posts/1');
      expect(result).toEqual(mockPost);
    });
  });

  describe('create', () => {
    it('should create a new post', async () => {
      const postData = { title: 'New Post', content: 'New Content' };
      const mockPost = { id: 1, ...postData };
      api.post.mockResolvedValue({ data: mockPost });

      const result = await postService.create(postData);

      expect(api.post).toHaveBeenCalledWith('/posts', postData);
      expect(result).toEqual(mockPost);
    });
  });

  describe('update', () => {
    it('should update an existing post', async () => {
      const postData = { title: 'Updated Post', content: 'Updated Content' };
      const mockPost = { id: 1, ...postData };
      api.put.mockResolvedValue({ data: mockPost });

      const result = await postService.update(1, postData);

      expect(api.put).toHaveBeenCalledWith('/posts/1', postData);
      expect(result).toEqual(mockPost);
    });
  });

  describe('delete', () => {
    it('should delete a post', async () => {
      api.delete.mockResolvedValue({});

      await postService.delete(1);

      expect(api.delete).toHaveBeenCalledWith('/posts/1');
    });
  });
});

