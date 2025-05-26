import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  FlatList,
  TouchableOpacity,
  ActivityIndicator,
  Alert,
  Linking // Import Linking for opening URLs
} from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API_URL } from '../config';
import Header from '../components/Header';
import { useRouter } from 'expo-router';

interface Course {
  id: number;
  titre: string;
  description: string;
  cloudinaryUrl: string;
  courseType: 'PUBLIC' | 'PRIVATE';
  fileType: string;
  // Add other fields as needed from your backend Course entity
}

interface UserInfo {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  autoEcole: {
    id: number;
    nom: string;
  } | null; // autoEcole can be null for users not linked to one
}

export default function CoursesScreen() {
  const [loading, setLoading] = useState(true);
  const [courses, setCourses] = useState<Course[]>([]);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const router = useRouter();

  useEffect(() => {
    const loadUserInfoAndCourses = async () => {
      try {
        // Load user info
        const userInfoStr = await AsyncStorage.getItem('userInfo');
        let userData: UserInfo | null = null;
        if (userInfoStr) {
          userData = JSON.parse(userInfoStr);
          setUserInfo(userData);
        }

        if (!userData) {
          // Handle case where user info is not available (e.g., not logged in)
          setLoading(false);
          Alert.alert('Error', 'User information not found.');
          return;
        }

        const token = await AsyncStorage.getItem('token');
        if (!token) {
          throw new Error("No authentication token found");
        }

        let response;
        if (userData.autoEcole && userData.autoEcole.id) {
          // Fetch private courses for the autoEcole
          response = await axios.get(`${API_URL}/courses/private/${userData.autoEcole.id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          });
        } else {
          // Fetch public courses
          response = await axios.get(`${API_URL}/courses/public`, {
             headers: {
              Authorization: `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
          });
        }

        if (response.data) {
          setCourses(response.data);
        }

      } catch (error) {
        console.error('Error loading courses:', error);
        Alert.alert('Error', 'Failed to load courses. Please try again.');
      } finally {
        setLoading(false);
      }
    };

    loadUserInfoAndCourses();
  }, []);

  const handleCoursePress = (course: Course) => {
    if (course.cloudinaryUrl) {
      router.push({ pathname: '/course-viewer', params: { cloudinaryUrl: course.cloudinaryUrl, fileType: course.fileType } });
    } else {
      Alert.alert('Error', 'Course material URL not available.');
    }
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#ff6b35" />
      </View>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      {/* You might want a dynamic title based on public/private courses */}
      <Header title="Courses" /> 

      <FlatList
        data={courses}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <TouchableOpacity style={styles.courseItem} onPress={() => handleCoursePress(item)}>
            <Text style={styles.courseTitle}>{item.titre}</Text>
            <Text style={styles.courseDescription}>{item.description}</Text>
            {/* You might add an icon or indicator for course type */}
          </TouchableOpacity>
        )}
        contentContainerStyle={styles.listContent}
      />
    </SafeAreaView>
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
  listContent: {
    padding: 20,
  },
  courseItem: {
    backgroundColor: '#fff',
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  courseTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 5,
  },
  courseDescription: {
    fontSize: 14,
    color: '#666',
  },
}); 