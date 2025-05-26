import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import BlancTestScreen from '../screens/BlancTestScreen';

export default function BlancTestViewer() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <BlancTestScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
}); 