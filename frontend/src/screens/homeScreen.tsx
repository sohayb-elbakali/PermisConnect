import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  TouchableOpacity,
  Image,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { router } from "expo-router";
import Header from "../components/Header";
import ProgressCircle from "../components/ProgressCircle";
import Footer from "../components/Footer";

export default function HomeScreen() {
  const [activeTab, setActiveTab] = useState("home");

  const handleProfilePress = () => {
    console.log("Profile pressed");
    // router.push('/profile');
  };

  const handleNotificationPress = () => {
    console.log("Notification pressed");
    // router.push('/notifications');
  };

  const handleCoursesPress = () => {
    console.log("Courses button pressed");
    // router.push('/courses');
  };

  const handleTestPress = () => {
    console.log("Blanc test pressed");
    // router.push('/blanc-test');
  };

  const handleTheoryPress = () => {
    console.log("Theory section pressed");
    // router.push('/theory');
  };

  const handlePilotagePress = () => {
    console.log("Pilotage section pressed");
    // router.push('/pilotage');
  };

  // ...existing code...
  // ...existing code...
  // ...existing code...
  const handleBottomNavPress = (tab: string) => {
    setActiveTab(tab);
    console.log(`Navigating to: ${tab}`); // Debug log

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
  // ...existing code...
  

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />

      {/* Custom Header */}
      <Header
        title="PermisConnect"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      {/* User Section */}
      <View style={styles.userSection}>
        <View style={styles.userInfo}>
          <Image
            source={{ uri: "https://via.placeholder.com/50" }}
            style={styles.avatar}
          />
          <View style={styles.welcomeContainer}>
            <Text style={styles.welcomeText}>Welcome</Text>
            <Text style={styles.userRole}>PermisConnect</Text>
          </View>
        </View>
      </View>

      {/* Greeting */}
      <View style={styles.greetingSection}>
        <Text style={styles.helloText}>Hello</Text>
        <Text style={styles.userName}>User</Text>
      </View>

      {/* Progress Circles */}
      <View style={styles.progressContainer}>
        {/* Theoretical Test Circle */}
        <TouchableOpacity activeOpacity={0.8} onPress={handleTheoryPress}>
          <ProgressCircle
            size={120}
            strokeWidth={10}
            progress={65}
            color="#ff6b35"
            icon="school-outline"
            label="THEORETICAL TEST"
          />
        </TouchableOpacity>

        {/* Pilotage Circle */}
        <TouchableOpacity activeOpacity={0.8} onPress={handlePilotagePress}>
          <ProgressCircle
            size={120}
            strokeWidth={10}
            progress={80}
            color="#4CAF50"
            icon="car-outline"
            label="PILOTAGE"
          />
        </TouchableOpacity>
      </View>

      {/* Main Buttons */}
      <View style={styles.buttonsContainer}>
        <TouchableOpacity
          style={styles.actionButton}
          onPress={handleCoursesPress}
          activeOpacity={0.8}
        >
          <Icon name="book-outline" size={24} color="#fff" />
          <Text style={styles.actionButtonText}>COURSES</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.actionButton}
          onPress={handleTestPress}
          activeOpacity={0.8}
        >
          <Icon name="document-text-outline" size={24} color="#fff" />
          <Text style={styles.actionButtonText}>BLANC TEST</Text>
        </TouchableOpacity>
      </View>

      {/* Bottom Navigation */}
      <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  userSection: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 20,
    marginBottom: 20,
  },
  userInfo: {
    flexDirection: "row",
    alignItems: "center",
  },
  avatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: "#ddd",
  },
  welcomeContainer: {
    marginLeft: 12,
  },
  welcomeText: {
    fontSize: 14,
    color: "#666",
  },
  userRole: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#000",
  },
  greetingSection: {
    paddingHorizontal: 20,
    marginBottom: 30,
  },
  helloText: {
    fontSize: 24,
    color: "#666",
    marginBottom: 5,
  },
  userName: {
    fontSize: 28,
    fontWeight: "bold",
    color: "#000",
  },
  progressContainer: {
    flexDirection: "row",
    justifyContent: "space-evenly",
    marginBottom: 30,
  },
  buttonsContainer: {
    flexDirection: "row",
    justifyContent: "space-evenly",
    marginBottom: 80,
  },
  actionButton: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#ff6b35",
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 25,
    elevation: 3,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
  },
  actionButtonText: {
    color: "#fff",
    fontWeight: "bold",
    marginLeft: 8,
  },
});
