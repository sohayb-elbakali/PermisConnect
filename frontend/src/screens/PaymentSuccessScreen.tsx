import { useRouter } from "expo-router";
import React from "react";
import { Button, StyleSheet, Text, View } from "react-native";

export default function PaymentSuccessScreen() {
  const router = useRouter();
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Thank you for your payment!</Text>
      <Text style={styles.subtitle}>Your course is now unlocked.</Text>
      <Button
        title="Back to Courses"
        onPress={() => router.replace("/courses")}
      />
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
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
    color: "#4BB543",
  },
  subtitle: {
    fontSize: 16,
    color: "#333",
    marginBottom: 20,
  },
});
