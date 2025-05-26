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
import { useFocusEffect } from '@react-navigation/native';

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
    totalTheoreticalCourses: 40, // Fixed total of 40 courses
    viewedTheoreticalCourses: 0
  });
  const [lastBlancTestScore, setLastBlancTestScore] = useState<number | null>(null);

  useEffect(() => {
    const fetchUserInfoAndProgress = async () => {
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

      if (userInfo && userInfo.id) {
        fetchTheoreticalProgress(userInfo.id);
      }
    };
    fetchUserInfoAndProgress();
  }, [userInfo?.id]);

  useFocusEffect(
    React.useCallback(() => {
      const fetchLastBlancTestScore = async () => {
        const score = await AsyncStorage.getItem('lastBlancTestScore');
        if (score) setLastBlancTestScore(Number(score));
      };
      fetchLastBlancTestScore();
    }, [])
  );

  const fetchTheoreticalProgress = async (clientId: number) => {
    console.log('Attempting to fetch theoretical progress for client:', clientId);
    try {
      setLoading(true);
      const response = await axios.get(`${API_URL}/api/courses/progress/theoretical/${clientId}`);
      console.log('Theoretical progress response:', response.data);
      
      // Simulate progress based on actual viewed courses
      const actualViewedCourses = response.data.viewedTheoreticalCourses;
      const simulatedProgress = Math.min(actualViewedCourses, 40); // Cap at 40 courses
      
      setTheoreticalProgress({
        totalTheoreticalCourses: 40, // Fixed total
        viewedTheoreticalCourses: simulatedProgress
      });
    } catch (error) {
      console.error('Error fetching theoretical progress:', error);
      Alert.alert('Error', 'Failed to load course progress');
      setTheoreticalProgress({ 
        totalTheoreticalCourses: 40, 
        viewedTheoreticalCourses: 0 
      });
    } finally {
      setLoading(false);
    }
  };

  const calculateProgress = () => {
    // Calculate percentage based on simulated progress
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

  console.log('lastBlancTestScore for ProgressCircle:', lastBlancTestScore);

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
          <TouchableOpacity 
            activeOpacity={0.7} 
            onPress={handleTheoryPress}
            style={styles.progressCircleContainer}
          >
            <ProgressCircle
              size={120}
              strokeWidth={10}
              progress={theoreticalProgressPercentage}
              color="#4CAF50"
              icon="book-outline"
              label="COURS"
            />
          </TouchableOpacity>

          <TouchableOpacity 
            activeOpacity={0.7} 
            onPress={() => router.push('/test-blanc-viewer')}
            style={styles.progressCircleContainer}
          >
            <ProgressCircle
              size={120}
              strokeWidth={10}
              progress={lastBlancTestScore !== null ? Math.round(lastBlancTestScore) : 0}
              color="#ff6b35"
              icon="document-text-outline"
              label="BLANC TEST"
            />
          </TouchableOpacity>
        </View>

        <View style={styles.drivingPerformanceContainer}>
          <Text style={styles.drivingPerformanceTitle}>Driving Performance</Text>
          <View style={styles.progressBarContainer}>
            <View 
              style={[
                styles.progressBar, 
                { width: '75%', backgroundColor: '#4CAF50' }
              ]} 
            />
          </View>
          <View style={styles.performanceDetails}>
            <Text style={styles.performanceNote}>Note: 15/20</Text>
            <Text style={styles.performanceLabel}>Good Progress</Text>
          </View>
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
  progressCircleContainer: {
    backgroundColor: '#fff',
    borderRadius: 100,
    padding: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 8,
    // Add a subtle border
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.05)',
  },
  drivingPerformanceContainer: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    margin: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  drivingPerformanceTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 15,
  },
  progressBarContainer: {
    height: 20,
    backgroundColor: '#f0f0f0',
    borderRadius: 10,
    overflow: 'hidden',
    marginBottom: 10,
  },
  progressBar: {
    height: '100%',
    borderRadius: 10,
  },
  performanceDetails: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  performanceNote: {
    fontSize: 16,
    fontWeight: '600',
    color: '#4CAF50',
  },
  performanceLabel: {
    fontSize: 14,
    color: '#666',
  },
});
