import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  ActivityIndicator,
  ScrollView,
} from 'react-native';
import { router } from 'expo-router';
import autoEcoleService, { AutoEcole } from '../services/autoEcoleService';
import { authService } from '../services/authService';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function AutoEcoleSelectionScreen() {
  const [autoEcoles, setAutoEcoles] = useState<AutoEcole[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedAutoEcole, setSelectedAutoEcole] = useState<AutoEcole | null>(null);
  const [autoEcoleId, setAutoEcoleId] = useState('');
  const [searchResults, setSearchResults] = useState<AutoEcole[]>([]);
  const [isSearching, setIsSearching] = useState(false);
  const [showResults, setShowResults] = useState(false);

  useEffect(() => {
    loadAutoEcoles();
  }, []);

  const loadAutoEcoles = async () => {
    try {
      const response = await autoEcoleService.getAllAutoEcoles();
      setAutoEcoles(response);
    } catch (error) {
      console.error('Error loading auto-écoles:', error);
      Alert.alert('Erreur', 'Impossible de charger la liste des auto-écoles');
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
      const id = parseInt(autoEcoleId, 10);
      if (isNaN(id)) {
        Alert.alert('Erreur', 'L\'ID doit être un nombre valide');
        return;
      }
      const autoEcole = await autoEcoleService.getAutoEcoleById(id);
      if (autoEcole) {
        setSearchResults([autoEcole]);
        setSelectedAutoEcole(autoEcole);
        setShowResults(true);
      } else {
        Alert.alert('Erreur', 'Aucune auto-école trouvée avec cet ID');
        setSearchResults([]);
        setShowResults(false);
      }
    } catch (error) {
      console.error('Search error:', error);
      Alert.alert('Erreur', 'Impossible de trouver l\'auto-école');
      setSearchResults([]);
      setShowResults(false);
    } finally {
      setIsSearching(false);
    }
  };

  const handleValidate = async () => {
    if (!selectedAutoEcole) {
      Alert.alert('Erreur', 'Veuillez d\'abord rechercher et sélectionner une auto-école');
      return;
    }

    try {
      setLoading(true);
      
      // Save the selected auto-école in AsyncStorage
      await AsyncStorage.setItem('selectedAutoEcole', JSON.stringify(selectedAutoEcole));
      
      // Navigate to home screen
      router.replace('/home');
      
    } catch (error) {
      console.error('Validation error:', error);
      Alert.alert('Erreur', 'Une erreur est survenue');
    } finally {
      setLoading(false);
    }
  };

  const renderAutoEcoleItem = (autoEcole: AutoEcole) => (
    <TouchableOpacity
      key={autoEcole.id}
      style={[
        styles.autoEcoleCard,
        selectedAutoEcole?.id === autoEcole.id && styles.selectedCard
      ]}
      onPress={() => setSelectedAutoEcole(autoEcole)}
    >
      <Text style={styles.autoEcoleName}>{autoEcole.nom}</Text>
      <Text style={styles.autoEcoleAddress}>{autoEcole.adresse}</Text>
      <Text style={styles.autoEcolePhone}>{autoEcole.telephone}</Text>
    </TouchableOpacity>
  );

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#007AFF" />
      </View>
    );
  }

  return (
    <ScrollView style={styles.container}>
      <View style={styles.searchContainer}>
        <Text style={styles.title}>Sélectionner votre auto-école</Text>
        
        <View style={styles.searchBox}>
          <TextInput
            style={styles.searchInput}
            placeholder="Entrez l'ID de l'auto-école"
            value={autoEcoleId}
            onChangeText={(text) => {
              setAutoEcoleId(text);
              setShowResults(false);
            }}
            keyboardType="numeric"
          />
          <TouchableOpacity
            style={styles.searchButton}
            onPress={handleSearch}
            disabled={isSearching}
          >
            <Text style={styles.searchButtonText}>
              {isSearching ? 'Recherche...' : 'Rechercher'}
            </Text>
          </TouchableOpacity>
        </View>

        {showResults && searchResults.length > 0 && (
          <View style={styles.resultsContainer}>
            <Text style={styles.sectionTitle}>Résultat de la recherche</Text>
            {searchResults.map(renderAutoEcoleItem)}
            
            <TouchableOpacity
              style={styles.validateButton}
              onPress={handleValidate}
              disabled={!selectedAutoEcole}
            >
              <Text style={styles.validateButtonText}>Valider</Text>
            </TouchableOpacity>
          </View>
        )}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  searchContainer: {
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    color: '#2c3e50',
  },
  searchBox: {
    flexDirection: 'row',
    marginBottom: 20,
  },
  searchInput: {
    flex: 1,
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 5,
    padding: 10,
    marginRight: 10,
    backgroundColor: '#fff',
  },
  searchButton: {
    backgroundColor: '#007AFF',
    padding: 10,
    borderRadius: 5,
    justifyContent: 'center',
  },
  searchButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  resultsContainer: {
    marginTop: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#2c3e50',
  },
  autoEcoleCard: {
    backgroundColor: '#fff',
    padding: 15,
    borderRadius: 8,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  selectedCard: {
    borderColor: '#007AFF',
    borderWidth: 2,
  },
  autoEcoleName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#2c3e50',
    marginBottom: 5,
  },
  autoEcoleAddress: {
    fontSize: 14,
    color: '#7f8c8d',
    marginBottom: 3,
  },
  autoEcolePhone: {
    fontSize: 14,
    color: '#7f8c8d',
  },
  validateButton: {
    backgroundColor: '#28a745',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 20,
  },
  validateButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
}); 