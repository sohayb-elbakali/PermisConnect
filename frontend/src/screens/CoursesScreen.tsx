import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { useRouter } from "expo-router";
import React, { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  FlatList, // Import Linking for opening URLs
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import Header from "../components/Header";
import { API_URL } from "../config";
import { useAuth } from "../contexts/AuthContext";

interface Course {
  id: number;
  titre: string;
  description: string;
  cloudinaryUrl: string;
  courseType: "PUBLIC" | "PRIVATE";
  fileType: string;
  estGratuit?: boolean; // Add estGratuit for public courses
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
  const router = useRouter();
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [courses, setCourses] = useState<Course[]>([]);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);

  useEffect(() => {
    const loadUserInfoAndCourses = async () => {
      try {
        // Load user info
        const userInfoStr = await AsyncStorage.getItem("userInfo");
        let userData: UserInfo | null = null;
        if (userInfoStr) {
          userData = JSON.parse(userInfoStr);
          setUserInfo(userData);
        }

        if (!userData) {
          // Handle case where user info is not available (e.g., not logged in)
          setLoading(false);
          Alert.alert("Error", "User information not found.");
          return;
        }

        const token = await AsyncStorage.getItem("token");
        if (!token) {
          throw new Error("No authentication token found");
        }

        let response;
        if (userData.autoEcole && userData.autoEcole.id) {
          // Fetch private courses for the autoEcole
          response = await axios.get(
            `${API_URL}/courses/private/${userData.autoEcole.id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
          );
        } else {
          // Fetch public courses
          response = await axios.get(`${API_URL}/courses/public`, {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          });
        }

        if (response.data) {
          setCourses(response.data);
        }
      } catch (error) {
        console.error("Error loading courses:", error);
        Alert.alert("Error", "Failed to load courses. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    loadUserInfoAndCourses();
  }, []);

  const handleCoursePress = (course: Course) => {
    router.push({
      pathname: "/course-viewer",
      params: {
        cloudinaryUrl: course.cloudinaryUrl,
        fileType: course.fileType,
        courseId: course.id?.toString(),
        courseType: course.courseType,
      },
    });
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
        keyExtractor={(item, index) => {
          // Safely access item.id and convert to string,
          // or use index as a fallback if item or item.id is undefined/null
          return item?.id?.toString() ?? index.toString();
        }}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.courseItem}
            onPress={() => handleCoursePress(item)}
          >
            <Text style={styles.courseTitle}>{item.titre}</Text>
            <Text style={styles.courseDescription}>{item.description}</Text>
            {/* Show payment status only for unpaid public courses */}
            {item.courseType === "PUBLIC" && item.estGratuit === false && (
              <Text
                style={{
                  color: "red",
                  fontWeight: "bold",
                  marginBottom: 5,
                }}
              >
                Unpaid
              </Text>
            )}
            {/* Show Pay button for public, non-free courses */}
            {item.courseType === "PUBLIC" && item.estGratuit === false && (
              <TouchableOpacity
                style={{
                  marginTop: 0,
                  marginBottom: 5,
                  backgroundColor: "#ff6b35",
                  paddingVertical: 4,
                  paddingHorizontal: 12,
                  borderRadius: 4,
                  alignItems: "flex-start",
                  alignSelf: "flex-start",
                  shadowColor: "#000",
                  shadowOffset: { width: 0, height: 1 },
                  shadowOpacity: 0.15,
                  shadowRadius: 2,
                  elevation: 2,
                }}
                activeOpacity={0.7}
                onPress={async () => {
                  try {
                    const token = await AsyncStorage.getItem("token");
                    const response = await axios.post(
                      `${API_URL}/courses/${item.id}/create-payment-session`,
                      {},
                      {
                        headers: {
                          Authorization: `Bearer ${token}`,
                          "Content-Type": "application/json",
                        },
                      }
                    );
                    const sessionUrl = response.data.sessionUrl;
                    if (sessionUrl) {
                      // Navigate to payment screen and pass sessionUrl
                      router.push({
                        pathname: "/payment",
                        params: { sessionUrl },
                      });
                    } else {
                      Alert.alert("Error", "No payment session URL returned.");
                    }
                  } catch (error) {
                    console.error("Error creating payment session:", error);
                    Alert.alert("Error", "Failed to initiate payment.");
                  }
                }}
              >
                <Text
                  style={{ color: "#fff", fontWeight: "bold", fontSize: 13 }}
                >
                  Pay
                </Text>
              </TouchableOpacity>
            )}
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
    backgroundColor: "#f8f9fa",
  },
  loadingContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  listContent: {
    padding: 20,
  },
  courseItem: {
    backgroundColor: "#fff",
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  courseTitle: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 5,
  },
  courseDescription: {
    fontSize: 14,
    color: "#666",
  },
});
