import { StatusBar } from "expo-status-bar";
import React from "react";
import { SafeAreaView, StyleSheet } from "react-native";
import ProfileScreen from "../screens/ProfileScreen";

export default function Profile() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <ProfileScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
});
