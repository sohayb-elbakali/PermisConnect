import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  TouchableOpacity,
  Image,
  ScrollView,
  Alert,
  ActivityIndicator,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { router, useRouter } from "expo-router";
import Header from "../components/Header";
import ProgressCircle from "../components/ProgressCircle";
import Footer from "../components/Footer";
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { API_URL } from "../config";
import { useAuth } from '../contexts/AuthContext';

interface UserInfo {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  autoEcole: {
    id: number;
    nom: string;
  } | null;
  // Assuming client ID is available in userInfo, maybe as 'id' or a separate field
  // If not, you'll need to fetch client info separately or adjust the backend endpoint
}

interface Course {
  id: number;
  courseType: 'PUBLIC' | 'PRIVATE';
  // Add other fields as needed to identify theoretical courses
  categorie?: string; // From CoursPublic
  type?: string; // From CoursPrive
}

interface TheoreticalProgress {
  totalTheoreticalCourses: number;
  viewedTheoreticalCourses: number;
}

export default function HomeScreen() {
  const router = useRouter();
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState("home");
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const [loading, setLoading] = useState(true);
  const [theoreticalProgress, setTheoreticalProgress] = useState<TheoreticalProgress>({
    totalTheoreticalCourses: 0,
    viewedTheoreticalCourses: 0
  });

  useEffect(() => {
    loadUserInfo();
    if (user?.id) {
      fetchTheoreticalProgress(user.id);
    }
  }, [user?.id]);

  const loadUserInfo = async () => {
    try {
      const userInfoStr = await AsyncStorage.getItem("userInfo");
      if (userInfoStr) {
        const userData = JSON.parse(userInfoStr);
        const token = await AsyncStorage.getItem("token");
        if (!token) {
          throw new Error("No authentication token found");
        }

        // Fetch user info
        const userResponse = await axios.get(`${API_URL}/clients/${userData.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (userResponse.data) {
          setUserInfo(userResponse.data);
          console.log('User Info loaded:', userResponse.data);
        }
      }
    } catch (error) {
      console.error("Error loading user info:", error);
      Alert.alert("Error", "Failed to load user information. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const fetchTheoreticalProgress = async (clientId: number) => {
    console.log('Attempting to fetch theoretical progress for client:', clientId);
    try {
      setLoading(true);
      const response = await axios.get(`${API_URL}/api/courses/progress/theoretical/${clientId}`);
      console.log('Theoretical progress response:', response.data);
      setTheoreticalProgress(response.data);
    } catch (error) {
      console.error('Error fetching theoretical progress:', error);
      Alert.alert('Error', 'Failed to load course progress');
      setTheoreticalProgress({ totalTheoreticalCourses: 0, viewedTheoreticalCourses: 0 });
    } finally {
      setLoading(false);
    }
  };

  const calculateProgress = () => {
    if (theoreticalProgress.totalTheoreticalCourses === 0) return 0;
    return (theoreticalProgress.viewedTheoreticalCourses / theoreticalProgress.totalTheoreticalCourses) * 100;
  };

  const theoreticalProgressPercentage = calculateProgress();

  console.log('Progress calculation for display:', {
    totalCourses: theoreticalProgress.totalTheoreticalCourses,
    viewedCourses: theoreticalProgress.viewedTheoreticalCourses,
    percentage: theoreticalProgressPercentage
  });

  const handleProfilePress = () => {
    router.push('/profile');
  };

  const handleNotificationPress = () => {
    console.log("Notification pressed");
  };

  const handleTheoryPress = () => {
    router.push('/courses');
  };

  const handlePilotagePress = () => {
    router.push('/pilotage');
  };

  const handleBottomNavPress = (tab: string) => {
    setActiveTab(tab);
    switch (tab) {
      case "home":
        break;
      case "schedule":
        router.push("/calendar");
        break;
      case "chat":
        router.push("/chat");
        break;
      case "settings":
        router.push("/settings");
        break;
    }
  };

  const handleLogout = async () => {
    try {
      await AsyncStorage.removeItem('userInfo');
      await AsyncStorage.removeItem('selectedAutoEcole');
      router.replace('/login');
    } catch (error) {
      console.error('Error during logout:', error);
      Alert.alert('Error', 'An error occurred during logout');
    }
  };

  if (loading && !userInfo) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#ff6b35" />
        <Text style={{ marginTop: 10 }}>Loading...</Text>
      </View>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />

      <Header
        title={userInfo?.autoEcole?.nom || "PermisConnect"}
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <ScrollView style={styles.scrollView}>
        <View style={styles.greetingSection}>
          <Text style={styles.helloText}>Hello, {userInfo?.prenom}</Text>
          <Text style={styles.userName}>{userInfo?.nom}</Text>
        </View>

        <View style={styles.progressContainer}>
          <TouchableOpacity activeOpacity={0.8} onPress={handleTheoryPress}>
            <ProgressCircle
              size={120}
              strokeWidth={10}
              progress={theoreticalProgressPercentage}
              color="#4CAF50"
              icon="book-outline"
              label="COURS"
            />
          </TouchableOpacity>

          <TouchableOpacity activeOpacity={0.8} onPress={handlePilotagePress}>
            <ProgressCircle
              size={120}
              strokeWidth={10}
              progress={80}
              color="#ff6b35"
              icon="car-outline"
              label="DRIVING TEST"
            />
          </TouchableOpacity>
        </View>

        <TouchableOpacity 
          style={styles.blancTestContainer}
          onPress={() => router.push('/test-blanc-viewer')}
        >
          <View style={styles.blancTestContent}>
            <Text style={styles.blancTestText}>blanc test</Text>
            <View style={styles.blancTestProgressContainer}>
              <ProgressCircle
                size={50}
                strokeWidth={4}
                progress={65}
                color="#4CAF50"
                icon=""
                label=""
              />
              <Text style={styles.blancTestProgressText}>65%</Text>
            </View>
          </View>
        </TouchableOpacity>

        <View style={styles.bottomButtonsContainer}>
          <TouchableOpacity
            style={[styles.actionButton, { marginRight: 10, flex: 1 }]}
            onPress={() => router.push('/profile')}
          >
            <Icon name="person-outline" size={24} color="#fff" />
            <Text style={styles.actionButtonText}>My Profile</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.actionButton, { marginLeft: 10, flex: 1 }]}
            onPress={handleLogout}
          >
            <Icon name="log-out-outline" size={24} color="#fff" />
            <Text style={styles.actionButtonText}>Logout</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>

      <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  scrollView: {
    flex: 1,
  },
  greetingSection: {
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 10,
  },
  helloText: {
    fontSize: 22,
    color: "#2c3e50",
    marginBottom: 2,
  },
  userName: {
    fontSize: 26,
    fontWeight: "bold",
    color: "#2c3e50",
  },
  progressContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    paddingHorizontal: 20,
    marginTop: 10,
    marginBottom: 20,
  },
  blancTestContainer: {
    backgroundColor: '#d1dabf',
    borderRadius: 6,
    padding: 6,
    marginHorizontal: 20,
    marginTop: 0,
    marginBottom: 15,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 3,
  },
  blancTestContent: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  blancTestText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333',
    flex: 1,
    marginRight: 10,
  },
  blancTestProgressContainer: {
    alignItems: 'center',
  },
  blancTestProgressText: {
    fontSize: 12,
    fontWeight: '600',
    color: '#7a0c0c',
    marginTop: 3,
  },
  bottomButtonsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    marginBottom: 20,
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#4CAF50',
    padding: 15,
    borderRadius: 10,
    shadowColor: '#ffffff',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 3,
  },
  actionButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
    marginLeft: 8,
  },
  progressText: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
  },
  progressLabel: {
    fontSize: 14,
    color: '#666',
  },
});
