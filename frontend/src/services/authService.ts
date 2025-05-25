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

export const authService = {
  async register(credentials: RegisterCredentials) {
    try {
      console.log('Registration request payload:', credentials);
      const response = await apiClient.post('/clients', credentials);
      console.log('Registration response:', response.data);
      return response.data;
    } catch (error: any) {
      console.error('Registration error:', error);
      if (error.response) {
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
        
        if (error.response.status === 409) {
          throw new Error('Cet email est déjà utilisé. Veuillez utiliser une autre adresse email.');
        }
      }
      throw error;
    }
  },

  async login(credentials: LoginCredentials) {
    try {
      console.log('Login attempt with:', credentials);
      if (!credentials.password) {
        throw new Error('Le mot de passe est requis');
      }
      
      // Convert motDePasse to rawPassword for backend
      const loginRequest = {
        email: credentials.email,
        password: credentials.password
      };
      
      console.log('Login request payload:', loginRequest);
      const response = await apiClient.post('/auth/login', loginRequest);
      
      console.log('Login response:', response.data);
      
      if (response.data.token) {
        await AsyncStorage.setItem('authToken', response.data.token);
        await AsyncStorage.setItem('userInfo', JSON.stringify(response.data.user));
      }
      
      return response.data;
    } catch (error: any) {
      console.error('Login error:', error);
      if (error.response) {
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
        
        if (error.response.status === 401) {
          throw new Error('Email ou mot de passe incorrect');
        }
      }
      throw error;
    }
  },

  async logout() {
    try {
      await AsyncStorage.removeItem('authToken');
      await AsyncStorage.removeItem('userInfo');
    } catch (error) {
      console.error('Logout error:', error);
      throw error;
    }
  },

  async getCurrentUser() {
    try {
      const userInfo = await AsyncStorage.getItem('userInfo');
      return userInfo ? JSON.parse(userInfo) : null;
    } catch (error) {
      console.error('Get current user error:', error);
      return null;
    }
  },

  async isAuthenticated(): Promise<boolean> {
    try {
      const token = await AsyncStorage.getItem("authToken");
      return !!token;
    } catch (error) {
      return false;
    }
  }
};
