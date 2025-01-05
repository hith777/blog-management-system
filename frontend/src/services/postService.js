import api from './api';

export const postService = {
  getAll: async () => {
    const response = await api.get('/posts');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/posts/${id}`);
    return response.data;
  },

  create: async (postData) => {
    const response = await api.post('/posts', postData);
    return response.data;
  },

  update: async (id, postData) => {
    const response = await api.put(`/posts/${id}`, postData);
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/posts/${id}`);
  },
};

