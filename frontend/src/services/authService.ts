import apiClient from "./api";
import AsyncStorage from "@react-native-async-storage/async-storage";

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  dateNaissance: string;
  telephone: string;
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
      console.log('Login request:', credentials);
      const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
      console.log('Login response:', response.data);

      if (response.data) {
        // Store user info
        await AsyncStorage.setItem('userInfo', JSON.stringify(response.data));
        
        // Store authentication state
        await AsyncStorage.setItem('isAuthenticated', 'true');
        
        // Return the response data
        return response.data;
      }

      throw new Error('No data received from server');
    } catch (error: any) {
      console.error('Login error:', error.response?.data || error.message);
      if (error.response?.status === 401) {
        throw new Error('Email ou mot de passe incorrect');
      }
      throw new Error(error.response?.data?.message || 'Erreur lors de la connexion');
    }
  }

  async register(credentials: RegisterCredentials): Promise<AuthResponse> {
    try {
      console.log('Register request:', credentials);
      const response = await apiClient.post<AuthResponse>('/clients', credentials);
      console.log('Register response:', response.data);

      if (response.data) {
        // Generate a simple token
        const token = btoa(JSON.stringify({
          id: response.data.id,
          email: response.data.user.email,
          timestamp: new Date().getTime()
        }));

        // Save token and user info
        await AsyncStorage.setItem('token', token);
        await AsyncStorage.setItem('userInfo', JSON.stringify(response.data.user));
      }

      return response.data;
    } catch (error: any) {
      console.error('Register error:', error.response?.data || error.message);
      if (error.response?.status === 409) {
        throw new Error('Cet email est déjà utilisé');
      }
      throw new Error(error.response?.data?.message || 'Erreur lors de l\'inscription');
    }
  }

  async logout(): Promise<void> {
    try {
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('userInfo');
      await AsyncStorage.removeItem('selectedAutoEcole');
    } catch (error) {
      console.error('Logout error:', error);
      throw error;
    }
  }

  async getCurrentUser() {
    try {
      const userInfo = await AsyncStorage.getItem('userInfo');
      return userInfo ? JSON.parse(userInfo) : null;
    } catch (error) {
      console.error('Get current user error:', error);
      return null;
    }
  }

  async isAuthenticated(): Promise<boolean> {
    try {
      const token = await AsyncStorage.getItem('token');
      return !!token;
    } catch (error) {
      console.error('Check authentication error:', error);
      return false;
    }
  }
}

export const authService = new AuthService();
