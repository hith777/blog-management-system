import api from './api';

export const tagService = {
  getAll: async () => {
    const response = await api.get('/tags');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/tags/${id}`);
    return response.data;
  },

  create: async (tagData) => {
    const response = await api.post('/tags', tagData);
    return response.data;
  },

  update: async (id, tagData) => {
    const response = await api.put(`/tags/${id}`, tagData);
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/tags/${id}`);
  },
};

