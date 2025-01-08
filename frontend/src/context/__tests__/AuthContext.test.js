import React from 'react';
import { render, screen, waitFor, act } from '@testing-library/react';
import { AuthProvider, useAuth } from '../AuthContext';
import { authService } from '../../services/authService';

jest.mock('../../services/authService');

const TestComponent = () => {
  const { user, isAuthenticated, loading, login, register, logout } = useAuth();

  return (
    <div>
      <div data-testid="loading">{loading ? 'Loading' : 'Not Loading'}</div>
      <div data-testid="user">{user ? JSON.stringify(user) : 'No User'}</div>
      <div data-testid="authenticated">{isAuthenticated ? 'Authenticated' : 'Not Authenticated'}</div>
      <button
        data-testid="login-btn"
        onClick={() => login('testuser', 'password123')}
      >
        Login
      </button>
      <button
        data-testid="register-btn"
        onClick={() => register('testuser', 'test@example.com', 'password123')}
      >
        Register
      </button>
      <button data-testid="logout-btn" onClick={logout}>
        Logout
      </button>
    </div>
  );
};

describe('AuthContext', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  it('should initialize with no user when no token exists', async () => {
    authService.getCurrentUser.mockReturnValue(null);
    authService.getToken.mockReturnValue(null);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('authenticated')).toHaveTextContent('Not Authenticated');
    });
  });

  it('should initialize with user when token exists', async () => {
    const mockUser = { id: 1, username: 'testuser', email: 'test@example.com', roles: ['ROLE_USER'] };
    authService.getCurrentUser.mockReturnValue(mockUser);
    authService.getToken.mockReturnValue('test-token');

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent(JSON.stringify(mockUser));
      expect(screen.getByTestId('authenticated')).toHaveTextContent('Authenticated');
    });
  });

  it('should handle login', async () => {
    authService.getCurrentUser.mockReturnValue(null);
    authService.getToken.mockReturnValue(null);
    const mockResponse = {
      id: 1,
      username: 'testuser',
      email: 'test@example.com',
      roles: ['ROLE_USER'],
    };
    authService.login.mockResolvedValue(mockResponse);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
    });

    await act(async () => {
      screen.getByTestId('login-btn').click();
    });

    await waitFor(() => {
      expect(authService.login).toHaveBeenCalledWith('testuser', 'password123');
      expect(screen.getByTestId('authenticated')).toHaveTextContent('Authenticated');
    });
  });

  it('should handle register', async () => {
    authService.getCurrentUser.mockReturnValue(null);
    authService.getToken.mockReturnValue(null);
    const mockResponse = {
      id: 1,
      username: 'testuser',
      email: 'test@example.com',
      roles: ['ROLE_USER'],
    };
    authService.register.mockResolvedValue(mockResponse);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
    });

    await act(async () => {
      screen.getByTestId('register-btn').click();
    });

    await waitFor(() => {
      expect(authService.register).toHaveBeenCalledWith('testuser', 'test@example.com', 'password123');
      expect(screen.getByTestId('authenticated')).toHaveTextContent('Authenticated');
    });
  });

  it('should handle logout', async () => {
    const mockUser = { id: 1, username: 'testuser', email: 'test@example.com', roles: ['ROLE_USER'] };
    authService.getCurrentUser.mockReturnValue(mockUser);
    authService.getToken.mockReturnValue('test-token');

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId('authenticated')).toHaveTextContent('Authenticated');
    });

    await act(async () => {
      screen.getByTestId('logout-btn').click();
    });

    await waitFor(() => {
      expect(authService.logout).toHaveBeenCalled();
      expect(screen.getByTestId('authenticated')).toHaveTextContent('Not Authenticated');
    });
  });

  it('should throw error when useAuth is used outside AuthProvider', () => {
    const consoleError = jest.spyOn(console, 'error').mockImplementation(() => {});

    expect(() => {
      render(<TestComponent />);
    }).toThrow('useAuth must be used within an AuthProvider');

    consoleError.mockRestore();
  });
});

