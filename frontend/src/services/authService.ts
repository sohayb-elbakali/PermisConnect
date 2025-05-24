import apiClient from "./api";
import AsyncStorage from "@react-native-async-storage/async-storage";

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role?: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  user?: User;
  token?: string;
}

class AuthService {
  // Login user
  async login(credentials: LoginCredentials): Promise<LoginResponse> {
    try {
      const response = await apiClient.post("/auth/login", credentials);

      if (response.data.success && response.data.token) {
        // Store token and user info
        await AsyncStorage.setItem("authToken", response.data.token);
        await AsyncStorage.setItem(
          "userInfo",
          JSON.stringify(response.data.user)
        );

        return {
          success: true,
          message: "Login successful",
          user: response.data.user,
          token: response.data.token,
        };
      }

      return {
        success: false,
        message: response.data.message || "Login failed",
      };
    } catch (error: any) {
      console.error("Login error:", error);
      return {
        success: false,
        message:
          error.response?.data?.message || "Network error. Please try again.",
      };
    }
  }

  // Logout user
  async logout(): Promise<void> {
    try {
      // Optional: Call logout endpoint
      await apiClient.post("/auth/logout");
    } catch (error) {
      console.error("Logout error:", error);
    } finally {
      // Always clear local storage
      await AsyncStorage.removeItem("authToken");
      await AsyncStorage.removeItem("userInfo");
    }
  }

  // Get current user
  async getCurrentUser(): Promise<User | null> {
    try {
      const userInfo = await AsyncStorage.getItem("userInfo");
      return userInfo ? JSON.parse(userInfo) : null;
    } catch (error) {
      console.error("Error getting user:", error);
      return null;
    }
  }

  // Check if authenticated
  async isAuthenticated(): Promise<boolean> {
    try {
      const token = await AsyncStorage.getItem("authToken");
      return !!token;
    } catch (error) {
      return false;
    }
  }
}

export default new AuthService();
