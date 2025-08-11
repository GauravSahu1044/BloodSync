import axios from 'axios';

const API_BASE_URL = 'http://localhost:5000/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if it exists
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authApi = {
  login: (credentials: { email: string; password: string }) =>
    api.post('/auth/login', credentials),
  register: (userData: any) => api.post('/auth/register', userData),
};

export const userApi = {
  getAllUsers: () => api.get('/users'),
  getUserById: (id: number) => api.get(`/users/${id}`),
  updateUser: (id: number, data: any) => api.put(`/users/${id}`, data),
  deleteUser: (id: number) => api.delete(`/users/${id}`),
};

export const hospitalApi = {
  getAllHospitals: () => api.get('/hospitals'),
  getHospitalById: (id: number) => api.get(`/hospitals/${id}`),
  createHospital: (data: any) => api.post('/hospitals', data),
  updateHospital: (id: number, data: any) => api.put(`/hospitals/${id}`, data),
  deleteHospital: (id: number) => api.delete(`/hospitals/${id}`),
};

export const bloodInventoryApi = {
  getAllInventory: () => api.get('/blood-inventory'),
  getInventoryByHospital: (hospitalId: number) =>
    api.get(`/blood-inventory/hospital/${hospitalId}`),
  updateInventory: (id: number, data: any) =>
    api.put(`/blood-inventory/${id}`, data),
};

export const bloodDonationApi = {
  createDonation: (data: any) => api.post('/blood-donations', data),
  getDonationsByDonor: (donorId: number) =>
    api.get(`/blood-donations/donor/${donorId}`),
  updateDonationStatus: (id: number, status: string) =>
    api.put(`/blood-donations/${id}`, { status }),
};

export const bloodRequestApi = {
  createRequest: (data: any) => api.post('/blood-requests', data),
  getRequestsByPatient: (patientId: number) =>
    api.get(`/blood-requests/patient/${patientId}`),
  updateRequestStatus: (id: number, status: string) =>
    api.put(`/blood-requests/${id}`, { status }),
};

export default api;
