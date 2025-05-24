import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import Icon from "react-native-vector-icons/Ionicons";

interface FooterProps {
  activeTab: string;
  onTabPress: (tab: string) => void;
}

const Footer: React.FC<FooterProps> = ({ activeTab, onTabPress }) => {
  return (
    <View style={styles.bottomNav}>
      <TouchableOpacity
        style={[styles.navItem, activeTab === "home" && styles.activeNavItem]}
        onPress={() => onTabPress("home")}
      >
        <Icon
          name="home"
          size={24}
          color={activeTab === "home" ? "#4CAF50" : "#666"}
        />
        <Text
          style={[
            styles.navItemText,
            activeTab === "home" && styles.activeNavItemText,
          ]}
        >
          Home
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[
          styles.navItem,
          activeTab === "schedule" && styles.activeNavItem,
        ]}
        onPress={() => onTabPress("schedule")}
      >
        <Icon
          name="calendar-outline"
          size={24}
          color={activeTab === "schedule" ? "#4CAF50" : "#666"}
        />
        <Text
          style={[
            styles.navItemText,
            activeTab === "schedule" && styles.activeNavItemText,
          ]}
        >
          Schedule
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[styles.navItem, activeTab === "chat" && styles.activeNavItem]}
        onPress={() => onTabPress("chat")}
      >
        <Icon
          name="chatbubble-outline"
          size={24}
          color={activeTab === "chat" ? "#4CAF50" : "#666"}
        />
        <Text
          style={[
            styles.navItemText,
            activeTab === "chat" && styles.activeNavItemText,
          ]}
        >
          Chat
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[
          styles.navItem,
          activeTab === "settings" && styles.activeNavItem,
        ]}
        onPress={() => onTabPress("settings")}
      >
        <Icon
          name="settings-outline"
          size={24}
          color={activeTab === "settings" ? "#4CAF50" : "#666"}
        />
        <Text
          style={[
            styles.navItemText,
            activeTab === "settings" && styles.activeNavItemText,
          ]}
        >
          Settings
        </Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  bottomNav: {
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
    flexDirection: "row",
    backgroundColor: "#fff",
    paddingVertical: 12,
    paddingHorizontal: 15,
    borderTopWidth: 1,
    borderTopColor: "#e0e0e0",
    elevation: 8,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: -2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
  },
  navItem: {
    flex: 1,
    alignItems: "center",
    paddingVertical: 8,
  },
  activeNavItem: {
    backgroundColor: "#f0f8f0",
    borderRadius: 8,
  },
  navItemText: {
    fontSize: 12,
    color: "#666",
    marginTop: 4,
  },
  activeNavItemText: {
    color: "#4CAF50",
    fontWeight: "500",
  },
});

export default Footer;
