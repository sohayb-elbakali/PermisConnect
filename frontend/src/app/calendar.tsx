import { StatusBar } from "expo-status-bar";
import React from "react";
import { SafeAreaView, StyleSheet } from "react-native";
import CalendarScreen from "../screens/CalendarScreen";

export default function Calendar() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <CalendarScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
});
