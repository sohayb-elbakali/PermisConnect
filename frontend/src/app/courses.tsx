import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import CoursesScreen from '../screens/CoursesScreen';

export default function Courses() {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <CoursesScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
}); 