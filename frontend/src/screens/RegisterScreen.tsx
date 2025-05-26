import { router } from "expo-router";
import React, { useState } from "react";
import {
  Animated,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import Button from "../components/Button";
import TextField from "../components/TextField";
import { Colors } from "../constants/Colors";
import { authService } from "../services/authService";

const RegisterScreen = () => {
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState({ message: "", type: "" });
  const [notificationOpacity] = useState(new Animated.Value(0));
  const [formData, setFormData] = useState({
    nom: "",
    prenom: "",
    email: "",
    password: "",
    telephone: "",
    adresse: "",
    dateNaissance: "",
    numeroPermis: "",
    typePermis: "",
  });
  const [errors, setErrors] = useState({
    nom: "",
    prenom: "",
    email: "",
    password: "",
    telephone: "",
    adresse: "",
    dateNaissance: "",
    numeroPermis: "",
    typePermis: "",
  });

  const showNotification = (message: string, type: "success" | "error") => {
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
    setFormData((prev) => ({
      ...prev,
      [field]: value,
    }));
    // Clear error when user types
    if (errors[field as keyof typeof errors]) {
      setErrors((prev) => ({
        ...prev,
        [field]: "",
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {
      nom: "",
      prenom: "",
      email: "",
      password: "",
      telephone: "",
      adresse: "",
      dateNaissance: "",
      numeroPermis: "",
      typePermis: "",
    };

    if (!formData.nom.trim()) newErrors.nom = "Le nom est requis";
    if (!formData.prenom.trim()) newErrors.prenom = "Le prénom est requis";
    if (!formData.email.trim()) newErrors.email = "L'email est requis";
    if (!formData.password.trim())
      newErrors.password = "Le mot de passe est requis";
    if (formData.password.length < 6)
      newErrors.password =
        "Le mot de passe doit contenir au moins 6 caractères";
    if (!formData.telephone.trim())
      newErrors.telephone = "Le téléphone est requis";
    if (!formData.adresse.trim()) newErrors.adresse = "L'adresse est requise";
    if (!formData.dateNaissance.trim())
      newErrors.dateNaissance = "La date de naissance est requise";
    if (!formData.numeroPermis.trim())
      newErrors.numeroPermis = "Le numéro de permis est requis";
    if (!formData.typePermis.trim())
      newErrors.typePermis = "Le type de permis est requis";

    // Validate email format
    if (formData.email && !/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Format d'email invalide";
    }

    // Validate date format (YYYY-MM-DD)
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (formData.dateNaissance && !dateRegex.test(formData.dateNaissance)) {
      newErrors.dateNaissance = "Format de date invalide (YYYY-MM-DD)";
    }

    // Validate phone number format (at least 10 digits)
    const phoneRegex = /^[\+]?[0-9]{10,15}$/;
    const cleanPhone = formData.telephone.replace(/[\s\-\(\)]/g, "");
    if (formData.telephone && !phoneRegex.test(cleanPhone)) {
      newErrors.telephone =
        "Le téléphone doit contenir 10-15 chiffres (ex: 0123456789)";
    }

    setErrors(newErrors);
    return !Object.values(newErrors).some((error) => error !== "");
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      // Format phone number and prepare data according to API requirements
      const formattedData = {
        nom: formData.nom.trim(),
        prenom: formData.prenom.trim(),
        email: formData.email.trim().toLowerCase(),
        password: formData.password,
        telephone: formData.telephone.replace(/[\s\-\(\)]/g, ""), // Remove formatting characters
        adresse: formData.adresse.trim(),
        dateNaissance: formData.dateNaissance,
        numeroPermis: formData.numeroPermis.trim(),
        typePermis: formData.typePermis.trim().toUpperCase(),
      };

      console.log("Submitting registration with data:", formattedData);
      const response = await authService.register(formattedData);

      if (response.success) {
        showNotification(
          "Inscription réussie ! Redirection vers la connexion...",
          "success"
        );
        setTimeout(() => {
          router.replace("/login");
        }, 2000);
      } else {
        showNotification(response.message || "Échec de l'inscription", "error");
      }
    } catch (error: any) {
      console.error("Registration error:", error);
      showNotification(
        error.message ||
          "Une erreur est survenue lors de l'inscription. Veuillez réessayer.",
        "error"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <View style={styles.formContainer}>
          <Text style={styles.title}>Inscription</Text>

          <TextField
            label="Nom"
            value={formData.nom}
            onChangeText={(text) => handleChange("nom", text)}
            error={errors.nom}
          />

          <TextField
            label="Prénom"
            value={formData.prenom}
            onChangeText={(text) => handleChange("prenom", text)}
            error={errors.prenom}
          />

          <TextField
            label="Email"
            value={formData.email}
            onChangeText={(text) => handleChange("email", text)}
            error={errors.email}
            keyboardType="email-address"
            autoCapitalize="none"
          />

          <TextField
            label="Mot de passe"
            value={formData.password}
            onChangeText={(text) => handleChange("password", text)}
            error={errors.password}
            secureTextEntry
            placeholder="Au moins 6 caractères"
          />

          <TextField
            label="Téléphone"
            value={formData.telephone}
            onChangeText={(text) => handleChange("telephone", text)}
            error={errors.telephone}
            keyboardType="phone-pad"
            placeholder="Ex: 0123456789 ou +33123456789"
          />

          <TextField
            label="Adresse"
            value={formData.adresse}
            onChangeText={(text) => handleChange("adresse", text)}
            error={errors.adresse}
            placeholder="Votre adresse complète"
          />

          <TextField
            label="Date de naissance"
            value={formData.dateNaissance}
            onChangeText={(text) => handleChange("dateNaissance", text)}
            error={errors.dateNaissance}
            placeholder="YYYY-MM-DD (ex: 1990-01-15)"
          />

          <TextField
            label="Numéro de permis"
            value={formData.numeroPermis}
            onChangeText={(text) => handleChange("numeroPermis", text)}
            error={errors.numeroPermis}
            placeholder="Votre numéro de permis"
          />

          <TextField
            label="Type de permis"
            value={formData.typePermis}
            onChangeText={(text) => handleChange("typePermis", text)}
            error={errors.typePermis}
            placeholder="A, B, C, D..."
          />

          <Button
            title="S'inscrire"
            onPress={handleSubmit}
            loading={loading}
            style={styles.submitButton}
          />

          <Button
            title="Déjà un compte ? Se connecter"
            onPress={() => router.replace("/login")}
            type="secondary"
            style={styles.loginButton}
          />
        </View>

        <Animated.View
          style={[
            styles.notification,
            {
              opacity: notificationOpacity,
              backgroundColor:
                notification.type === "success" ? "#4CAF50" : "#f44336",
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
    backgroundColor: "#fff",
  },
  scrollContent: {
    flexGrow: 1,
    padding: 20,
  },
  formContainer: {
    flex: 1,
    width: "100%",
    maxWidth: 400,
    alignSelf: "center",
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    color: Colors.light.tint,
    marginBottom: 20,
    textAlign: "center",
  },
  submitButton: {
    marginTop: 20,
  },
  loginButton: {
    marginTop: 10,
  },
  notification: {
    position: "absolute",
    bottom: 20,
    left: 20,
    right: 20,
    padding: 15,
    borderRadius: 8,
    elevation: 3,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  notificationText: {
    color: "#fff",
    textAlign: "center",
    fontSize: 16,
  },
});

export default RegisterScreen;
