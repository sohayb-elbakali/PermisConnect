import React from 'react';
import { SafeAreaView, StyleSheet } from 'react-native';
import AutoEcoleDetailsScreen from '../screens/AutoEcoleDetailsScreen';

export default function AutoEcoleDetails() {
  return (
    <SafeAreaView style={styles.container}>
      <AutoEcoleDetailsScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
}); 