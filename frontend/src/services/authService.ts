import apiClient from "./api";
import AsyncStorage from "@react-native-async-storage/async-storage";

export interface LoginCredentials {
  email: string;
  password: string;  // We'll keep this as 'password' to match the backend's LoginRequest
}

export interface RegisterCredentials {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;  // Changed from password to match backend field name
  telephone?: string;
  adresse?: string;
  statut?: string; // Added to match backend requirements
}

export interface User {
  id: string;
  name: string;
  email: string;
  role?: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  user?: User;
  token?: string;
}

class AuthService {
  // Register new user
  async register(credentials: RegisterCredentials): Promise<AuthResponse> {
    try {
      const response = await apiClient.post("/auth/register", credentials);

      // Check if registration was successful - now using only success flag
      if (response.data.success) {
        // Only store token and user info if they exist
        if (response.data.token) {
          await AsyncStorage.setItem("authToken", response.data.token);

          const userInfo = {
            id: response.data.id,
            name: `${response.data.prenom} ${response.data.nom}`,
            email: response.data.email,
            role: response.data.role
          };

          await AsyncStorage.setItem("userInfo", JSON.stringify(userInfo));
        }

        return {
          success: true,
          message: response.data.message || "Registration successful",
          user: response.data.token ? {
            id: response.data.id,
            name: `${response.data.prenom} ${response.data.nom}`,
            email: response.data.email,
            role: response.data.role
          } : undefined,
          token: response.data.token,
        };
      }

      return {
        success: false,
        message: response.data.message || "Registration failed",
      };
    } catch (error: any) {
      console.error("Registration error:", error);
      return {
        success: false,
        message: error.response?.data?.message || "Network error. Please try again.",
      };
    }
  }

  // Login user
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    try {
      const response = await apiClient.post("/auth/login", credentials);

      if (response.data.success && response.data.token) {
        // Store token and user info
        await AsyncStorage.setItem("authToken", response.data.token);

        const userInfo = {
          id: response.data.id,
          name: `${response.data.prenom} ${response.data.nom}`,
          email: response.data.email,
          role: response.data.role
        };

        await AsyncStorage.setItem("userInfo", JSON.stringify(userInfo));

        return {
          success: true,
          message: "Login successful",
          user: userInfo,
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
