import axios, { AxiosInstance, InternalAxiosRequestConfig } from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";

// Replace with your actual API URL
const API_BASE_URL = "http://192.168.1.100:3000/api"; // Use your computer's IP for mobile testing
// const API_BASE_URL = 'http://localhost:3000/api'; // For web testing

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
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
