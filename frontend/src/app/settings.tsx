import { StatusBar } from "expo-status-bar";
import React from "react";
import { SafeAreaView, StyleSheet } from "react-native";
import SettingsScreen from "../screens/SettingsScreen";

export default function Settings() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <SettingsScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
});
