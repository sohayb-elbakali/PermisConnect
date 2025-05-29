import AsyncStorage from "@react-native-async-storage/async-storage";
import { useRouter } from "expo-router";
import React, { useEffect } from "react";
import { StyleSheet, Text, View } from "react-native";

export default function SuccessfulPayment() {
  const router = useRouter();

  useEffect(() => {
    const finishPayment = async () => {
      await AsyncStorage.setItem("hasPaid", "true");
      // Update all courses in AsyncStorage to estGratuit=true if they exist
      const coursesStr = await AsyncStorage.getItem("courses");
      if (coursesStr) {
        try {
          const courses = JSON.parse(coursesStr);
          const updated = courses.map((c) => ({ ...c, estGratuit: true }));
          await AsyncStorage.setItem("courses", JSON.stringify(updated));
        } catch {}
      }
      setTimeout(() => {
        router.replace("/courses");
      }, 1500);
    };
    finishPayment();
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.success}>Payment Successful!</Text>
      <Text style={styles.info}>Redirecting to your courses...</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fff",
  },
  success: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#4CAF50",
    marginBottom: 16,
  },
  info: {
    fontSize: 16,
    color: "#333",
  },
});
