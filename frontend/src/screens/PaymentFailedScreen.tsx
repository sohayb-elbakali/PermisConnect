import { useRouter } from "expo-router";
import React from "react";
import { Button, StyleSheet, Text, View } from "react-native";

export default function PaymentFailedScreen() {
  const router = useRouter();
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Payment Failed</Text>
      <Text style={styles.subtitle}>
        Your payment was not completed. Please try again.
      </Text>
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
    color: "#D7263D",
  },
  subtitle: {
    fontSize: 16,
    color: "#333",
    marginBottom: 20,
  },
});
