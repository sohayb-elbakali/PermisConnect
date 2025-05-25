import apiClient from "./api";
import AsyncStorage from "@react-native-async-storage/async-storage";

export interface LoginCredentials {
  email: string;
  motDePasse: string;  // Changed from password to match backend field name
}

export interface RegisterCredentials {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;
  telephone: string;
  adresse: string;
  dateNaissance: string;
  numeroPermis: string;
  typePermis: string;
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
      // Format the date to match backend format (YYYY-MM-DD)
      const formattedCredentials = {
        ...credentials,
        dateNaissance: credentials.dateNaissance.split('T')[0] // Ensure date is in YYYY-MM-DD format
      };

      const response = await apiClient.post("/clients", formattedCredentials);

      if (response.data) {
        return {
          success: true,
          message: "Inscription réussie",
          user: {
            id: response.data.id,
            name: `${response.data.prenom} ${response.data.nom}`,
            email: response.data.email,
            role: 'CLIENT'
          }
        };
      }

      return {
        success: false,
        message: "Échec de l'inscription",
      };
    } catch (error: any) {
      console.error("Registration error:", error);
      // Handle specific error cases
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        const errorMessage = error.response.data?.message || "Erreur lors de l'inscription";
        return {
          success: false,
          message: errorMessage
        };
      } else if (error.request) {
        // The request was made but no response was received
        return {
          success: false,
          message: "Impossible de se connecter au serveur. Veuillez vérifier votre connexion internet."
        };
      } else {
        // Something happened in setting up the request that triggered an Error
        return {
          success: false,
          message: "Une erreur est survenue. Veuillez réessayer."
        };
      }
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
          message: "Connexion réussie",
          user: userInfo,
          token: response.data.token,
        };
      }

      return {
        success: false,
        message: response.data.message || "Échec de la connexion",
      };
    } catch (error: any) {
      console.error("Login error:", error);
      if (error.response) {
        return {
          success: false,
          message: error.response.data?.message || "Échec de la connexion"
        };
      } else if (error.request) {
        return {
          success: false,
          message: "Impossible de se connecter au serveur. Veuillez vérifier votre connexion internet."
        };
      } else {
        return {
          success: false,
          message: "Une erreur est survenue. Veuillez réessayer."
        };
      }
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
