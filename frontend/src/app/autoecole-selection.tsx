import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import AutoEcoleSelectionScreen from '../screens/AutoEcoleSelectionScreen';

export default function AutoEcoleSelection() {
  return (
    <SafeAreaView style={styles.container}>
      <AutoEcoleSelectionScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
}); 