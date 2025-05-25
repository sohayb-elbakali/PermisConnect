import React, { useState } from 'react';
import { View, StyleSheet, ScrollView, Alert, ActivityIndicator, Text } from 'react-native';
import { router } from 'expo-router'; // Change from useNavigation to router
import TextField from '../components/TextField';
import Button from '../components/Button';
import authService from '../services/authService';
import { Colors } from '../constants/Colors';

const RegisterScreen = () => {
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    password: '',
    confirmPassword: '',
    telephone: '',
    adresse: '',
  });
  const [errors, setErrors] = useState({
    nom: '',
    prenom: '',
    email: '',
    password: '',
    confirmPassword: '',
    telephone: '',
    adresse: '',
  });

  const validateForm = () => {
    let isValid = true;
    const newErrors = { ...errors };

    // Validate name
    if (!formData.nom.trim()) {
      newErrors.nom = 'Le nom est obligatoire';
      isValid = false;
    } else {
      newErrors.nom = '';
    }

    // Validate firstname
    if (!formData.prenom.trim()) {
      newErrors.prenom = 'Le prénom est obligatoire';
      isValid = false;
    } else {
      newErrors.prenom = '';
    }

    // Validate email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email.trim()) {
      newErrors.email = 'L\'email est obligatoire';
      isValid = false;
    } else if (!emailRegex.test(formData.email)) {
      newErrors.email = 'Veuillez entrer une adresse email valide';
      isValid = false;
    } else {
      newErrors.email = '';
    }

    // Validate password
    if (!formData.password) {
      newErrors.password = 'Le mot de passe est obligatoire';
      isValid = false;
    } else if (formData.password.length < 6) {
      newErrors.password = 'Le mot de passe doit contenir au moins 6 caractères';
      isValid = false;
    } else {
      newErrors.password = '';
    }

    // Validate password confirmation
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Les mots de passe ne correspondent pas';
      isValid = false;
    } else {
      newErrors.confirmPassword = '';
    }

    setErrors(newErrors);
    return isValid;
  };

  const handleChange = (name: string, value: string) => {
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);

      const { nom, prenom, email, password, telephone, adresse } = formData;
      const response = await authService.register({
        nom,
        prenom,
        email,
        motDePasse: password,
        telephone,
        adresse,
        statut: "ACTIF"
      });

      if (response.success) {
        Alert.alert(
          'Inscription réussie !',
          'Votre compte a été créé avec succès. Veuillez vous connecter pour accéder à votre espace.',
          [
            { text: 'Se connecter', onPress: () => router.push('/login') }
          ]
        );
      } else {
        Alert.alert('Erreur', response.message);
      }
    } catch (error) {
      console.error('Registration error:', error);
      Alert.alert('Erreur', 'Une erreur est survenue lors de l\'inscription');
    } finally {
      setLoading(false);
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.formContainer}>
        <Text style={styles.title}>Créer un compte</Text>

        <TextField
          label="Nom"
          value={formData.nom}
          onChangeText={(value) => handleChange('nom', value)}
          error={errors.nom}
          placeholder="Entrez votre nom"
        />

        <TextField
          label="Prénom"
          value={formData.prenom}
          onChangeText={(value) => handleChange('prenom', value)}
          error={errors.prenom}
          placeholder="Entrez votre prénom"
        />

        <TextField
          label="Email"
          value={formData.email}
          onChangeText={(value) => handleChange('email', value)}
          error={errors.email}
          placeholder="Entrez votre email"
          keyboardType="email-address"
          autoCapitalize="none"
        />

        <TextField
          label="Mot de passe"
          value={formData.password}
          onChangeText={(value) => handleChange('password', value)}
          error={errors.password}
          placeholder="Entrez votre mot de passe"
          secureTextEntry
        />

        <TextField
          label="Confirmez le mot de passe"
          value={formData.confirmPassword}
          onChangeText={(value) => handleChange('confirmPassword', value)}
          error={errors.confirmPassword}
          placeholder="Confirmez votre mot de passe"
          secureTextEntry
        />

        <TextField
          label="Téléphone (optionnel)"
          value={formData.telephone}
          onChangeText={(value) => handleChange('telephone', value)}
          error={errors.telephone}
          placeholder="Entrez votre numéro de téléphone"
          keyboardType="phone-pad"
        />

        <TextField
          label="Adresse (optionnelle)"
          value={formData.adresse}
          onChangeText={(value) => handleChange('adresse', value)}
          error={errors.adresse}
          placeholder="Entrez votre adresse"
        />

        {loading ? (
          <ActivityIndicator size="large" color={Colors.light.tint} />
        ) : (
          <>
            <Button title="S'inscrire" onPress={handleSubmit} />
            <Button
              title="Déjà un compte ? Se connecter"
              onPress={() => router.push('/login')}
              type="secondary"
            />
          </>
        )}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  formContainer: {
    flex: 1,
    justifyContent: 'center',
    width: '100%',
    maxWidth: 400,
    alignSelf: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: Colors.light.tint,
    marginBottom: 20,
    textAlign: 'center',
  }
});

export default RegisterScreen;
