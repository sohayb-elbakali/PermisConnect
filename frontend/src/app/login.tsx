import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import LoginScreen from '../screens/LoginScreen';

export default function Login() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <LoginScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});
