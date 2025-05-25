import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  ActivityIndicator,
  Alert,
  TextInput,
} from 'react-native';
import { router } from 'expo-router';
import autoEcoleService, { AutoEcole } from '../services/autoEcoleService';
import { Colors } from '../constants/Colors';
import AsyncStorage from '@react-native-async-storage/async-storage';
import apiClient from '../services/api';

export default function AutoEcoleSelectionScreen() {
  const [autoEcoles, setAutoEcoles] = useState<AutoEcole[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedAutoEcole, setSelectedAutoEcole] = useState<AutoEcole | null>(null);
  const [autoEcoleId, setAutoEcoleId] = useState('');
  const [searchResults, setSearchResults] = useState<AutoEcole[]>([]);
  const [isSearching, setIsSearching] = useState(false);

  useEffect(() => {
    loadAutoEcoles();
  }, []);

  const loadAutoEcoles = async () => {
    try {
      const data = await autoEcoleService.getAllAutoEcoles();
      setAutoEcoles(data);
    } catch (error) {
      Alert.alert('Erreur', 'Impossible de charger les auto-écoles');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!autoEcoleId.trim()) {
      Alert.alert('Erreur', 'Veuillez entrer un ID d\'auto-école');
      return;
    }

    try {
      setIsSearching(true);
      const response = await apiClient.get(`/autoecoles/${autoEcoleId}`);
      if (response.data) {
        setSearchResults([response.data]);
        setSelectedAutoEcole(response.data);
      } else {
        Alert.alert('Erreur', 'Auto-école non trouvée');
      }
    } catch (error) {
      Alert.alert('Erreur', 'Impossible de trouver l\'auto-école');
    } finally {
      setIsSearching(false);
    }
  };

  const handleSelectAutoEcole = async (autoEcole: AutoEcole) => {
    try {
      setSelectedAutoEcole(autoEcole);
      // Store selected auto-école in AsyncStorage
      await AsyncStorage.setItem('selectedAutoEcole', JSON.stringify(autoEcole));
      
      // Update user's auto-école in backend
      const userInfo = await AsyncStorage.getItem('userInfo');
      if (userInfo) {
        const user = JSON.parse(userInfo);
        await apiClient.put(`/clients/${user.id}/autoecole`, {
          autoEcoleId: autoEcole.id
        });
      }

      // Navigate to home page
      router.replace('/home');
    } catch (error) {
      console.error('Error saving auto-école selection:', error);
      Alert.alert('Erreur', 'Impossible de sauvegarder la sélection');
    }
  };

  const renderAutoEcoleItem = ({ item }: { item: AutoEcole }) => (
    <TouchableOpacity
      style={[
        styles.autoEcoleCard,
        selectedAutoEcole?.id === item.id && styles.selectedAutoEcoleCard
      ]}
      onPress={() => handleSelectAutoEcole(item)}
    >
      <Text style={styles.autoEcoleName}>{item.nom}</Text>
      <Text style={styles.autoEcoleAddress}>{item.adresse}</Text>
      <Text style={styles.autoEcolePhone}>{item.telephone}</Text>
    </TouchableOpacity>
  );

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={Colors.light.tint} />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Sélectionnez votre auto-école</Text>

      <View style={styles.searchContainer}>
        <TextInput
          style={styles.searchInput}
          placeholder="Entrez l'ID de l'auto-école"
          value={autoEcoleId}
          onChangeText={setAutoEcoleId}
          keyboardType="numeric"
        />
        <TouchableOpacity
          style={styles.searchButton}
          onPress={handleSearch}
          disabled={isSearching}
        >
          {isSearching ? (
            <ActivityIndicator size="small" color="#fff" />
          ) : (
            <Text style={styles.searchButtonText}>Rechercher</Text>
          )}
        </TouchableOpacity>
      </View>

      <FlatList
        data={searchResults.length > 0 ? searchResults : autoEcoles}
        renderItem={renderAutoEcoleItem}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.listContainer}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    padding: 20,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: Colors.light.tint,
    marginBottom: 20,
  },
  searchContainer: {
    flexDirection: 'row',
    marginBottom: 20,
  },
  searchInput: {
    flex: 1,
    height: 50,
    borderWidth: 1,
    borderColor: '#e1e8ed',
    borderRadius: 8,
    paddingHorizontal: 15,
    marginRight: 10,
    fontSize: 16,
  },
  searchButton: {
    backgroundColor: Colors.light.tint,
    height: 50,
    paddingHorizontal: 20,
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
  },
  searchButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
  listContainer: {
    paddingBottom: 20,
  },
  autoEcoleCard: {
    backgroundColor: '#f8f9fa',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: '#e1e8ed',
  },
  selectedAutoEcoleCard: {
    borderColor: Colors.light.tint,
    backgroundColor: '#e3f2fd',
  },
  autoEcoleName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#2c3e50',
    marginBottom: 8,
  },
  autoEcoleAddress: {
    fontSize: 14,
    color: '#7f8c8d',
    marginBottom: 4,
  },
  autoEcolePhone: {
    fontSize: 14,
    color: '#7f8c8d',
  },
}); 