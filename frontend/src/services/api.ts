import axios, { AxiosInstance, InternalAxiosRequestConfig } from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";

// Choose one of these API URLs based on your setup
// For web browser running on the same machine as backend:
const API_BASE_URL = 'http://localhost:8080/api';

// Use your actual machine's local IP address if testing from a physical device
// const API_BASE_URL = "http://192.168.1.100:8080/api";

// When testing with an Android emulator:
// const API_BASE_URL = 'http://10.0.2.2:8080/api';

// For debugging only - to see which URL is being used
console.log('API URL:', API_BASE_URL);

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 20000, // Increased timeout for debugging
  headers: {
    "Content-Type": "application/json",
  },
});

// Add token to requests automatically
apiClient.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    try {
      const token = await AsyncStorage.getItem("authToken");
      if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    } catch (error) {
      console.error("Error getting token:", error);
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle token expiration
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      // Token expired - clear storage
      await AsyncStorage.removeItem("authToken");
      await AsyncStorage.removeItem("userInfo");
      console.log("Token expired, please login again");
    }
    return Promise.reject(error);
  }
);

export default apiClient;
