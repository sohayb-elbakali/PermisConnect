import AsyncStorage from "@react-native-async-storage/async-storage";
import apiClient from "./api";
import clientService from "./clientService";

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  telephone: string;
  adresse: string;
  dateNaissance: string;
  numeroPermis: string;
  typePermis: string;
}

export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  adresse: string;
}

export interface AuthResponse {
  id: number;
  user: {
    id: number;
    nom: string;
    prenom: string;
    email: string;
    telephone: string;
    adresse: string;
  };
  dateNaissance: string;
  numeroPermis: string;
  typePermis: string;
  autoEcole: any;
}

class AuthService {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    try {
      console.log("Login request:", credentials);
      const response = await apiClient.post<AuthResponse>(
        "/auth/login",
        credentials
      );
      console.log("Login response:", response.data);

      if (response.data) {
        // Store user info
        await AsyncStorage.setItem("userInfo", JSON.stringify(response.data));

        // Store authentication state
        await AsyncStorage.setItem("isAuthenticated", "true");

        // Return the response data
        return response.data;
      }

      throw new Error("No data received from server");
    } catch (error: any) {
      console.error("Login error:", error.response?.data || error.message);
      if (error.response?.status === 401) {
        throw new Error("Email ou mot de passe incorrect");
      }
      throw new Error(
        error.response?.data?.message || "Erreur lors de la connexion"
      );
    }
  }

  async register(
    credentials: RegisterCredentials
  ): Promise<{ success: boolean; message?: string; data?: AuthResponse }> {
    try {
      console.log("Register request:", credentials);
      const response = await clientService.createClient(credentials);
      console.log("Register response:", response);

      if (response) {
        return {
          success: true,
          data: {
            id: response.id,
            user: {
              id: response.id,
              nom: response.nom,
              prenom: response.prenom,
              email: response.email,
              telephone: response.telephone,
              adresse: response.adresse,
            },
            dateNaissance: response.dateNaissance,
            numeroPermis: response.numeroPermis,
            typePermis: response.typePermis,
            autoEcole: null,
          },
        };
      }

      return {
        success: false,
        message: "Aucune donnée reçue du serveur",
      };
    } catch (error: any) {
      console.error("Register error:", error.response?.data || error.message);
      let errorMessage = "Erreur lors de l'inscription";

      if (error.response?.status === 409) {
        errorMessage = "Cet email ou ce numéro de téléphone est déjà utilisé";
      } else if (error.response?.status === 400) {
        errorMessage = "Données invalides. Veuillez vérifier vos informations.";
      } else if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      }

      return {
        success: false,
        message: errorMessage,
      };
    }
  }

  async logout(): Promise<void> {
    try {
      await AsyncStorage.removeItem("token");
      await AsyncStorage.removeItem("userInfo");
      await AsyncStorage.removeItem("selectedAutoEcole");
    } catch (error) {
      console.error("Logout error:", error);
      throw error;
    }
  }

  async getCurrentUser() {
    try {
      const userInfo = await AsyncStorage.getItem("userInfo");
      return userInfo ? JSON.parse(userInfo) : null;
    } catch (error) {
      console.error("Get current user error:", error);
      return null;
    }
  }

  async isAuthenticated(): Promise<boolean> {
    try {
      const token = await AsyncStorage.getItem("token");
      return !!token;
    } catch (error) {
      console.error("Check authentication error:", error);
      return false;
    }
  }
}

export const authService = new AuthService();
