import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  Alert,
  ActivityIndicator,
  ScrollView,
  Animated,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { router } from "expo-router";
import { useAuth } from "../hooks/useAuth";
import AsyncStorage from "@react-native-async-storage/async-storage";
import TextField from '../components/TextField';
import Button from '../components/Button';
import authService from '../services/authService';
import { Colors } from '../constants/Colors';

const LoginScreen = () => {
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState({ message: '', type: '' });
  const [notificationOpacity] = useState(new Animated.Value(0));
  const [formData, setFormData] = useState({
    email: '',
    motDePasse: '',
  });
  const [errors, setErrors] = useState({
    email: '',
    motDePasse: '',
  });

  const { login, isLoading } = useAuth();

  const showNotification = (message: string, type: 'success' | 'error') => {
    setNotification({ message, type });
    Animated.sequence([
      Animated.timing(notificationOpacity, {
        toValue: 1,
        duration: 300,
        useNativeDriver: true,
      }),
      Animated.delay(3000),
      Animated.timing(notificationOpacity, {
        toValue: 0,
        duration: 300,
        useNativeDriver: true,
      }),
    ]).start();
  };

  const handleChange = (field: string, value: string) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
    // Clear error when user types
    if (errors[field as keyof typeof errors]) {
      setErrors(prev => ({
        ...prev,
        [field]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {
      email: '',
      motDePasse: '',
    };

    if (!formData.email.trim()) {
      newErrors.email = 'L\'email est requis';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Format d\'email invalide';
    }

    if (!formData.motDePasse.trim()) {
      newErrors.motDePasse = 'Le mot de passe est requis';
    }

    setErrors(newErrors);
    return !Object.values(newErrors).some(error => error !== '');
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      const response = await authService.login(formData);
      if (response.success) {
        showNotification('Connexion réussie', 'success');
        setTimeout(() => {
          router.replace('/autoecole-selection');
        }, 2000);
      } else {
        showNotification(response.message || 'Échec de la connexion', 'error');
      }
    } catch (error: any) {
      console.error('Login error:', error);
      showNotification(error.response?.data?.message || 'Échec de la connexion', 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />

      <View style={styles.header}>
        <Text style={styles.title}>Welcome Back</Text>
        <Text style={styles.subtitle}>Sign in to continue</Text>
      </View>

      <ScrollView contentContainerStyle={styles.form}>
        <View style={styles.formContainer}>
          <Text style={styles.title}>Connexion</Text>

          <TextField
            label="Email"
            value={formData.email}
            onChangeText={(text) => handleChange('email', text)}
            error={errors.email}
            keyboardType="email-address"
            autoCapitalize="none"
          />

          <TextField
            label="Mot de passe"
            value={formData.motDePasse}
            onChangeText={(text) => handleChange('motDePasse', text)}
            error={errors.motDePasse}
            secureTextEntry
          />

          <Button
            title="Se connecter"
            onPress={handleSubmit}
            loading={loading}
            style={styles.submitButton}
          />

          <Button
            title="Pas encore de compte ? S'inscrire"
            onPress={() => router.replace('/register')}
            type="secondary"
            style={styles.registerButton}
          />
        </View>

        <Animated.View
          style={[
            styles.notification,
            {
              opacity: notificationOpacity,
              backgroundColor: notification.type === 'success' ? '#4CAF50' : '#f44336',
            },
          ]}
        >
          <Text style={styles.notificationText}>{notification.message}</Text>
        </Animated.View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  header: {
    paddingHorizontal: 30,
    paddingTop: 60,
    paddingBottom: 40,
  },
  title: {
    fontSize: 32,
    fontWeight: "bold",
    color: "#2c3e50",
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: "#7f8c8d",
  },
  form: {
    flex: 1,
    paddingHorizontal: 30,
  },
  formContainer: {
    flex: 1,
    width: '100%',
    maxWidth: 400,
    alignSelf: 'center',
  },
  submitButton: {
    marginTop: 20,
  },
  registerButton: {
    marginTop: 10,
  },
  notification: {
    position: 'absolute',
    bottom: 20,
    left: 20,
    right: 20,
    padding: 15,
    borderRadius: 8,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  notificationText: {
    color: '#fff',
    textAlign: 'center',
    fontSize: 16,
  },
});

export default LoginScreen;
