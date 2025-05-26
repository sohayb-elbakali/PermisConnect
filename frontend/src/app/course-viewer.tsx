import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import CourseViewerScreen from '../screens/CourseViewerScreen';
import { useLocalSearchParams } from 'expo-router';

export default function CourseViewer() {
  const params = useLocalSearchParams();
  const { cloudinaryUrl, fileType } = params;

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar style="auto" />
      <CourseViewerScreen cloudinaryUrl={cloudinaryUrl as string} fileType={fileType as string} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
}); 