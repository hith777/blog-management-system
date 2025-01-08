import { authService } from '../authService';
import api from '../api';

jest.mock('../api');

describe('authService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  describe('register', () => {
    it('should register a user and store token and user data', async () => {
      const mockResponse = {
        data: {
          token: 'test-token',
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          roles: ['ROLE_USER'],
        },
      };

      api.post.mockResolvedValue(mockResponse);

      const result = await authService.register('testuser', 'test@example.com', 'password123');

      expect(api.post).toHaveBeenCalledWith('/auth/register', {
        username: 'testuser',
        email: 'test@example.com',
        password: 'password123',
      });
      expect(localStorage.setItem).toHaveBeenCalledWith('token', 'test-token');
      expect(localStorage.setItem).toHaveBeenCalledWith(
        'user',
        JSON.stringify({
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          roles: ['ROLE_USER'],
        })
      );
      expect(result).toEqual(mockResponse.data);
    });

    it('should not store token if response does not contain token', async () => {
      const mockResponse = {
        data: {
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
        },
      };

      api.post.mockResolvedValue(mockResponse);

      await authService.register('testuser', 'test@example.com', 'password123');

      expect(localStorage.setItem).not.toHaveBeenCalledWith('token', expect.anything());
    });
  });

  describe('login', () => {
    it('should login a user and store token and user data', async () => {
      const mockResponse = {
        data: {
          token: 'test-token',
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          roles: ['ROLE_USER'],
        },
      };

      api.post.mockResolvedValue(mockResponse);

      const result = await authService.login('testuser', 'password123');

      expect(api.post).toHaveBeenCalledWith('/auth/login', {
        username: 'testuser',
        password: 'password123',
      });
      expect(localStorage.setItem).toHaveBeenCalledWith('token', 'test-token');
      expect(localStorage.setItem).toHaveBeenCalledWith(
        'user',
        JSON.stringify({
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          roles: ['ROLE_USER'],
        })
      );
      expect(result).toEqual(mockResponse.data);
    });
  });

  describe('logout', () => {
    it('should remove token and user from localStorage', () => {
      authService.logout();

      expect(localStorage.removeItem).toHaveBeenCalledWith('token');
      expect(localStorage.removeItem).toHaveBeenCalledWith('user');
    });
  });

  describe('getCurrentUser', () => {
    it('should return parsed user from localStorage', () => {
      const user = { id: 1, username: 'testuser', email: 'test@example.com' };
      localStorage.getItem.mockReturnValue(JSON.stringify(user));

      const result = authService.getCurrentUser();

      expect(localStorage.getItem).toHaveBeenCalledWith('user');
      expect(result).toEqual(user);
    });

    it('should return null if no user in localStorage', () => {
      localStorage.getItem.mockReturnValue(null);

      const result = authService.getCurrentUser();

      expect(result).toBeNull();
    });
  });

  describe('getToken', () => {
    it('should return token from localStorage', () => {
      localStorage.getItem.mockReturnValue('test-token');

      const result = authService.getToken();

      expect(localStorage.getItem).toHaveBeenCalledWith('token');
      expect(result).toBe('test-token');
    });
  });
});

