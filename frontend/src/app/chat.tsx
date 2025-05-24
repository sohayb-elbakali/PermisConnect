import { StatusBar } from "expo-status-bar";
import React from "react";
import { SafeAreaView, StyleSheet } from "react-native";
import ChatScreen from "../screens/ChatScreen";

export default function Chat() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <ChatScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
});
