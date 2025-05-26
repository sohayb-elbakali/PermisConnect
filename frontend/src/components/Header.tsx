import React, { useRef, useEffect, useState } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Animated } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import { router } from "expo-router";
import NotificationBar from "./NotificationBar";
import AsyncStorage from "@react-native-async-storage/async-storage";

// Define props interface for Header component
interface HeaderProps {
  title: string;
  onProfilePress?: () => void;
  onNotificationPress?: () => void;
}

const Header: React.FC<HeaderProps> = ({
  title,
  onProfilePress,
  onNotificationPress,
}) => {
  const [showNotifications, setShowNotifications] = React.useState(false);
  const [headerTitle, setHeaderTitle] = useState(title);
  const slideAnim = useRef(new Animated.Value(-1000)).current;

  useEffect(() => {
    const loadAutoEcoleName = async () => {
      try {
        const selectedAutoEcole = await AsyncStorage.getItem('selectedAutoEcole');
        if (selectedAutoEcole) {
          const autoEcole = JSON.parse(selectedAutoEcole);
          setHeaderTitle(autoEcole.nom);
        }
      } catch (error) {
        console.error('Error loading auto-école name:', error);
      }
    };

    loadAutoEcoleName();
  }, []);

  const handleProfilePress = () => {
    // Navigate to profile screen
    router.push("/profile");

    // Call the optional prop function if provided
    if (onProfilePress) {
      onProfilePress();
    }
  };

  const handleNotificationPress = () => {
    if (showNotifications) {
      // Close notification bar
      Animated.timing(slideAnim, {
        toValue: -1000,
        duration: 300,
        useNativeDriver: true,
      }).start(() => {
        setShowNotifications(false);
      });
    } else {
      // Open notification bar
      setShowNotifications(true);
      Animated.timing(slideAnim, {
        toValue: 0,
        duration: 300,
        useNativeDriver: true,
      }).start();
    }

    // Call the optional prop function if provided
    if (onNotificationPress) {
      onNotificationPress();
    }
  };

  const closeNotificationBar = () => {
    Animated.timing(slideAnim, {
      toValue: -1000,
      duration: 300,
      useNativeDriver: true,
    }).start(() => {
      setShowNotifications(false);
    });
  };

  return (
    <>
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.iconButton}
          onPress={handleProfilePress}
          activeOpacity={0.7}
        >
          <Icon name="person-circle-outline" size={24} color="#ff6b35" />
        </TouchableOpacity>

        <Text style={styles.headerTitle}>{headerTitle}</Text>

        <TouchableOpacity
          style={[styles.iconButton, showNotifications && styles.activeIconButton]}
          onPress={handleNotificationPress}
          activeOpacity={0.7}
        >
          <Icon 
            name="notifications-outline" 
            size={24} 
            color={showNotifications ? "#fff" : "#ff6b35"} 
          />
          {/* Notification badge */}
          <View style={styles.notificationBadge}>
            <Text style={styles.badgeText}>3</Text>
          </View>
        </TouchableOpacity>
      </View>

      {/* Notification Bar */}
      <NotificationBar
        isVisible={showNotifications}
        onClose={closeNotificationBar}
        slideAnim={slideAnim}
      />
    </>
  );
};

const styles = StyleSheet.create({
  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 20,
    paddingTop: 10,
    paddingBottom: 15,
    borderBottomWidth: 1,
    borderBottomColor: "#eaeaea",
    backgroundColor: "#fff",
    zIndex: 999,
  },
  headerTitle: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
  },
  iconButton: {
    padding: 8,
    borderRadius: 20,
    position: "relative",
  },
  activeIconButton: {
    backgroundColor: "#ff6b35",
  },
  notificationBadge: {
    position: "absolute",
    top: 2,
    right: 2,
    backgroundColor: "#e74c3c",
    borderRadius: 8,
    minWidth: 16,
    height: 16,
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 2,
    borderColor: "#fff",
  },
  badgeText: {
    color: "#fff",
    fontSize: 10,
    fontWeight: "bold",
  },
});

export default Header;
