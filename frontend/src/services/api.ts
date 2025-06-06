import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { router } from 'expo-router';

// Choose one of these API URLs based on your setup
// For web browser running on the same machine as backend:
const API_BASE_URL = 'http://localhost:8080/api';

// Use your actual machine's local IP address if testing from a physical device
// const API_BASE_URL = "http://192.168.1.100:8080/api";

// When testing with an Android emulator:
// const API_BASE_URL = 'http://10.0.2.2:8080/api';

// For debugging only - to see which URL is being used
console.log('API URL:', API_BASE_URL);

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20000, // Increased timeout for debugging
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
});

// Request interceptor to add authentication state
apiClient.interceptors.request.use(
  async (config) => {
    try {
      const isAuthenticated = await AsyncStorage.getItem('isAuthenticated');
      if (isAuthenticated === 'true') {
        // Add any necessary headers for authenticated requests
        config.headers['X-Authenticated'] = 'true';
      }
      return config;
    } catch (error) {
      console.error('Request interceptor error:', error);
      return config;
    }
  },
  (error) => {
    console.error('Request interceptor error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor to handle authentication errors
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If error is 401 (Unauthorized) and we haven't tried to refresh yet
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // Clear authentication state
        await AsyncStorage.removeItem('isAuthenticated');
        await AsyncStorage.removeItem('userInfo');
        await AsyncStorage.removeItem('selectedAutoEcole');

        // Redirect to login
        router.replace('/login');
        
        return Promise.reject(error);
      } catch (refreshError) {
        console.error('Authentication refresh error:', refreshError);
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;