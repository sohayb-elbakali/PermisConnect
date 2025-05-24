import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";

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
  return (
    <View style={styles.header}>
      <TouchableOpacity
        style={styles.iconButton}
        onPress={onProfilePress}
        activeOpacity={0.7}
      >
        <Icon name="person-circle-outline" size={24} color="#ff6b35" />
      </TouchableOpacity>

      <Text style={styles.headerTitle}>{title}</Text>

      <TouchableOpacity
        style={styles.iconButton}
        onPress={onNotificationPress}
        activeOpacity={0.7}
      >
        <Icon name="notifications-outline" size={24} color="#ff6b35" />
      </TouchableOpacity>
    </View>
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
  },
  headerTitle: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
  },
  iconButton: {
    padding: 8,
    borderRadius: 20,
  },
});

export default Header;
