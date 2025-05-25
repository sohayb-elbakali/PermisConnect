import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
  Alert,
} from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import autoEcoleService, { AutoEcole } from '../services/autoEcoleService';
import { Colors } from '../constants/Colors';

export default function AutoEcoleDetailsScreen() {
  const { id } = useLocalSearchParams();
  const [autoEcole, setAutoEcole] = useState<AutoEcole | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadAutoEcoleDetails();
  }, [id]);

  const loadAutoEcoleDetails = async () => {
    try {
      const data = await autoEcoleService.getAutoEcoleById(Number(id));
      setAutoEcole(data);
    } catch (error) {
      Alert.alert('Erreur', 'Impossible de charger les détails de l\'auto-école');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={Colors.light.tint} />
      </View>
    );
  }

  if (!autoEcole) {
    return (
      <View style={styles.errorContainer}>
        <Text style={styles.errorText}>Auto-école non trouvée</Text>
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>{autoEcole.nom}</Text>
        <Text style={styles.subtitle}>Numéro d'agrément: {autoEcole.numeroAgreement}</Text>
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Informations de contact</Text>
        <Text style={styles.infoText}>📞 {autoEcole.telephone}</Text>
        <Text style={styles.infoText}>📧 {autoEcole.email}</Text>
        <Text style={styles.infoText}>📍 {autoEcole.adresse}</Text>
      </View>

      {autoEcole.description && (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Description</Text>
          <Text style={styles.descriptionText}>{autoEcole.description}</Text>
        </View>
      )}

      {autoEcole.horaires && (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Horaires d'ouverture</Text>
          <Text style={styles.infoText}>{autoEcole.horaires}</Text>
        </View>
      )}

      {autoEcole.services && autoEcole.services.length > 0 && (
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Services proposés</Text>
          {autoEcole.services.map((service, index) => (
            <Text key={index} style={styles.serviceText}>• {service}</Text>
          ))}
        </View>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorText: {
    fontSize: 16,
    color: '#e74c3c',
  },
  header: {
    padding: 20,
    backgroundColor: Colors.light.tint,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: '#fff',
    opacity: 0.8,
  },
  section: {
    padding: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#e1e8ed',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#2c3e50',
    marginBottom: 12,
  },
  infoText: {
    fontSize: 16,
    color: '#34495e',
    marginBottom: 8,
  },
  descriptionText: {
    fontSize: 16,
    color: '#34495e',
    lineHeight: 24,
  },
  serviceText: {
    fontSize: 16,
    color: '#34495e',
    marginBottom: 8,
  },
}); 