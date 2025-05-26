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
import userService from "../services/userService";
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { API_URL } from "../config";

interface UserInfo {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  autoEcole: {
    id: number;
    nom: string;
  };
}

export default function HomeScreen() {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState("home");
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUserInfo();
  }, []);

  const loadUserInfo = async () => {
    try {
      const userInfoStr = await AsyncStorage.getItem("userInfo");
      if (userInfoStr) {
        const userData = JSON.parse(userInfoStr);
        const token = await AsyncStorage.getItem("token");
        if (!token) {
          throw new Error("No authentication token found");
        }

        const response = await axios.get(`${API_URL}/clients/${userData.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (response.data) {
          setUserInfo(response.data);
        }
      }
    } catch (error) {
      console.error("Error loading user info:", error);
      Alert.alert("Error", "Failed to load user information. Please try again.");
    } finally {
      setLoading(false);
    }
  };

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
        router.push("/home");
        break;
      case "schedule":
        router.push("/calendar")
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

      <Header
        title={userInfo?.autoEcole?.nom || "PermisConnect"}
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <ScrollView style={styles.scrollView}>
        {/* Greeting Section */}
        <View style={styles.greetingSection}>
          <Text style={styles.helloText}>Hello, {userInfo?.prenom}</Text>
          <Text style={styles.userName}>{userInfo?.nom}</Text>
        </View>

        {/* Progress Circles */}
        <View style={styles.progressContainer}>
          <TouchableOpacity activeOpacity={0.8} onPress={handleTheoryPress}>
            <ProgressCircle
              size={120}
              strokeWidth={10}
              progress={65}
              color="#ff6b35"
              icon="school-outline"
              label="THEORETICAL COURS"
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

        {/* Action Buttons */}
        <View style={styles.actionButtonsContainer}>
          <TouchableOpacity
            style={styles.actionButton}
            onPress={() => router.push('/profile')}
          >
            <Icon name="person-outline" size={24} color="#fff" />
            <Text style={styles.actionButtonText}>My Profile</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.actionButton, styles.logoutButton]}
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
    padding: 20,
  },
  helloText: {
    fontSize: 24,
    color: "#2c3e50",
    marginBottom: 5,
  },
  userName: {
    fontSize: 28,
    fontWeight: "bold",
    color: "#2c3e50",
  },
  progressContainer: {
    flexDirection: "row",
    justifyContent: "space-around",
    paddingHorizontal: 20,
    marginBottom: 30,
  },
  actionButtonsContainer: {
    padding: 20,
  },
  actionButton: {
    backgroundColor: "#ff6b35",
    flexDirection: "row",
    alignItems: "center",
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
  },
  actionButtonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "bold",
    marginLeft: 10,
  },
  logoutButton: {
    backgroundColor: "#e74c3c",
  },
});
