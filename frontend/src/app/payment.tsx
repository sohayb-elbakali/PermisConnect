import AsyncStorage from "@react-native-async-storage/async-storage";
import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useState } from "react";
import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Platform,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";

export default function PaymentScreen() {
  const router = useRouter();
  const params = useLocalSearchParams();
  const [cardNumber, setCardNumber] = useState("");
  const [expiry, setExpiry] = useState("");
  const [cvc, setCvc] = useState("");
  const [name, setName] = useState("");
  const [loading, setLoading] = useState(false);

  // Get courseId from params
  const courseId = params.courseId;

  const handlePay = async () => {
    setLoading(true);
    try {
      // Get paid courses from AsyncStorage
      const paidCoursesStr = await AsyncStorage.getItem("paidCourses");
      let paidCourses = paidCoursesStr ? JSON.parse(paidCoursesStr) : [];
      if (!paidCourses.includes(courseId)) {
        paidCourses.push(courseId);
        await AsyncStorage.setItem("paidCourses", JSON.stringify(paidCourses));
      }
      setLoading(false);
      router.replace("/successfulpayment");
    } catch (e) {
      setLoading(false);
      Alert.alert("Error", "Failed to update course status");
    }
  };

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      <View style={styles.card}>
        <Text style={styles.title}>Card Payment</Text>
        <TextInput
          style={styles.input}
          placeholder="Cardholder Name"
          value={name}
          onChangeText={setName}
        />
        <TextInput
          style={styles.input}
          placeholder="Card Number"
          keyboardType="numeric"
          value={cardNumber}
          onChangeText={setCardNumber}
          maxLength={19}
        />
        <View style={styles.row}>
          <TextInput
            style={[styles.input, { flex: 1, marginRight: 8 }]}
            placeholder="MM/YY"
            value={expiry}
            onChangeText={setExpiry}
            maxLength={5}
          />
          <TextInput
            style={[styles.input, { flex: 1 }]}
            placeholder="CVC"
            value={cvc}
            onChangeText={setCvc}
            keyboardType="numeric"
            maxLength={4}
          />
        </View>
        <TouchableOpacity
          style={styles.payButton}
          onPress={handlePay}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.payText}>Pay</Text>
          )}
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f5f5f5",
    padding: 24,
  },
  card: {
    backgroundColor: "#fff",
    borderRadius: 16,
    padding: 24,
    width: 340,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 4,
  },
  title: {
    fontSize: 22,
    fontWeight: "bold",
    color: "#1a1a1a",
    marginBottom: 18,
    textAlign: "center",
  },
  input: {
    backgroundColor: "#f0f0f0",
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    marginBottom: 14,
  },
  row: {
    flexDirection: "row",
    marginBottom: 14,
  },
  payButton: {
    backgroundColor: "#1976d2",
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: "center",
    marginTop: 8,
  },
  payText: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "bold",
  },
});
