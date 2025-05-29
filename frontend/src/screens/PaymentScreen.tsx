import { useLocalSearchParams, useRouter } from "expo-router";
import React, { useEffect } from "react";
import { ActivityIndicator, Platform, StyleSheet, View } from "react-native";
import { WebView } from "react-native-webview";

export default function PaymentScreen() {
  const { sessionUrl } = useLocalSearchParams();
  const router = useRouter();

  useEffect(() => {
    if (Platform.OS === "web" && sessionUrl) {
      window.open(sessionUrl as string, "_blank");
      // Optionally, redirect back after opening
      router.replace("/courses");
    }
  }, [sessionUrl]);

  if (!sessionUrl) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#ff6b35" />
      </View>
    );
  }

  if (Platform.OS === "web") {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#ff6b35" />
      </View>
    );
  }

  return (
    <WebView
      source={{ uri: sessionUrl as string }}
      startInLoadingState
      renderLoading={() => (
        <View style={styles.centered}>
          <ActivityIndicator size="large" color="#ff6b35" />
        </View>
      )}
      onNavigationStateChange={(navState) => {
        // Optionally handle success/cancel URLs here
        if (navState.url.includes("payment-success")) {
          router.replace("/PaymentSuccessScreen");
        }
        if (navState.url.includes("payment-cancel")) {
          router.replace("/PaymentFailedScreen");
        }
      }}
    />
  );
}

const styles = StyleSheet.create({
  centered: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fff",
  },
});
