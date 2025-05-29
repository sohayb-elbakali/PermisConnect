import AsyncStorage from "@react-native-async-storage/async-storage";
import { useFocusEffect } from "@react-navigation/native";
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
  const [showPayWarningId, setShowPayWarningId] = useState<number | null>(null);

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
          // Merge paid status from AsyncStorage
          const paidCoursesStr = await AsyncStorage.getItem("paidCourses");
          let paidCourses = paidCoursesStr ? JSON.parse(paidCoursesStr) : [];
          const updatedCourses = response.data.map((course: any) =>
            paidCourses.includes(course.id.toString()) ? { ...course, estGratuit: true } : course
          );
          setCourses(updatedCourses);
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

  useFocusEffect(
    React.useCallback(() => {
      const checkPayment = async () => {
        const hasPaid = await AsyncStorage.getItem("hasPaid");
        if (!hasPaid) {
          Alert.alert(
            "Payment Required",
            "You must pay before accessing courses.",
            [{ text: "OK", onPress: () => router.replace("/payment") }]
          );
        }
      };
      checkPayment();
    }, [router])
  );

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
            onPress={() => {
              if (item.estGratuit === false) {
                setShowPayWarningId(item.id);
                Alert.alert(
                  "Payment Required",
                  "You must pay for this course before accessing it."
                );
                return;
              }
              handleCoursePress(item);
            }}
            disabled={false}
          >
            <Text style={styles.courseTitle}>{item.titre}</Text>
            <Text style={styles.courseDescription}>{item.description}</Text>
            {/* Show 'Free' in green for free courses */}
            {item.estGratuit === true && (
              <Text
                style={{ color: "green", fontWeight: "bold", marginBottom: 5 }}
              >
                Free
              </Text>
            )}
            {/* Show Pay button for unpaid courses */}
            {item.estGratuit === false && (
              <TouchableOpacity
                style={{
                  marginTop: 8,
                  backgroundColor: "#ff6b35",
                  paddingVertical: 6,
                  paddingHorizontal: 18,
                  borderRadius: 6,
                  alignItems: "center",
                  alignSelf: "flex-start",
                }}
                onPress={() =>
                  router.replace({
                    pathname: "/payment",
                    params: { courseId: item.id },
                  })
                }
              >
                <Text
                  style={{ color: "#fff", fontWeight: "bold", fontSize: 15 }}
                >
                  Pay
                </Text>
              </TouchableOpacity>
            )}
            {/* Show warning line only if user tried to open this unpaid course */}
            {item.estGratuit === false && showPayWarningId === item.id && (
              <Text style={{ color: "red", fontWeight: "bold", marginTop: 8 }}>
                You must pay for this course before accessing it.
              </Text>
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
