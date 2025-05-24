import React, { useState } from "react";
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  Switch,
  Alert,
  ScrollView,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import Footer from "../components/Footer";
import Header from "../components/Header";
import { router } from "expo-router";

export default function SettingsScreen() {
  const [activeTab, setActiveTab] = useState<string>('settings');
  const [notificationsEnabled, setNotificationsEnabled] = useState<boolean>(true);
  const [darkModeEnabled, setDarkModeEnabled] = useState<boolean>(false);
  const [selectedLanguage, setSelectedLanguage] = useState<string>('Français');

  const handleProfilePress = () => {
    console.log("Profile pressed");
  };

  const handleNotificationPress = () => {
    console.log("Notification pressed");
  };

  const handleBottomNavPress = (tab: string) => {
    setActiveTab(tab);
    
    switch (tab) {
      case "home":
        router.push("/home");
        break;
      case "schedule":
        router.push("/calendar");
        break;
      case "chat":
        router.push("/chat");
        break;
      case "settings":
        // Already on settings
        break;
    }
  };

  const handleLanguageSelect = () => {
    Alert.alert(
      "Choisir la langue",
      "Sélectionnez votre langue préférée",
      [
        { text: "Français", onPress: () => setSelectedLanguage("Français") },
        { text: "English", onPress: () => setSelectedLanguage("English") },
        { text: "العربية", onPress: () => setSelectedLanguage("العربية") },
        { text: "Annuler", style: "cancel" },
      ]
    );
  };

  const handleAbout = () => {
    Alert.alert(
      "À propos de PermisConnect",
      "PermisConnect v1.0.0\n\nVotre assistant personnel pour réussir le permis de conduire.\n\nDéveloppé avec ❤️ pour vous aider à maîtriser le code de la route.",
      [{ text: "OK" }]
    );
  };

  const handleHelp = () => {
    Alert.alert(
      "Aide & Support",
      "Comment pouvons-nous vous aider ?\n\n• Consultez notre FAQ\n• Contactez le support\n• Regardez les tutoriels\n• Rejoignez la communauté",
      [
        { text: "FAQ", onPress: () => console.log("FAQ pressed") },
        { text: "Support", onPress: () => console.log("Support pressed") },
        { text: "Fermer", style: "cancel" },
      ]
    );
  };

  const handleLogout = () => {
    Alert.alert(
      "Déconnexion",
      "Êtes-vous sûr de vouloir vous déconnecter ?",
      [
        { text: "Annuler", style: "cancel" },
        { 
          text: "Déconnexion", 
          style: "destructive",
          onPress: () => {
            console.log("User logged out");
            // Add logout logic here
          }
        },
      ]
    );
  };

  const SettingItem = ({ 
    icon, 
    title, 
    subtitle, 
    onPress, 
    rightComponent, 
    showArrow = true 
  }: {
    icon: string;
    title: string;
    subtitle?: string;
    onPress?: () => void;
    rightComponent?: React.ReactNode;
    showArrow?: boolean;
  }) => (
    <TouchableOpacity style={styles.settingItem} onPress={onPress}>
      <View style={styles.settingLeft}>
        <View style={styles.iconContainer}>
          <Icon name={icon} size={22} color="#667eea" />
        </View>
        <View style={styles.settingText}>
          <Text style={styles.settingTitle}>{title}</Text>
          {subtitle && <Text style={styles.settingSubtitle}>{subtitle}</Text>}
        </View>
      </View>
      <View style={styles.settingRight}>
        {rightComponent}
        {showArrow && !rightComponent && (
          <Icon name="chevron-forward" size={20} color="#999" />
        )}
      </View>
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      <Header
        title="Paramètres"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
        {/* User Profile Section */}
        <View style={styles.section}>
          <View style={styles.profileCard}>
            <View style={styles.profileAvatar}>
              <Icon name="person" size={40} color="#667eea" />
            </View>
            <View style={styles.profileInfo}>
              <Text style={styles.profileName}>Utilisateur PermisConnect</Text>
              <Text style={styles.profileEmail}>user@permisconnect.com</Text>
            </View>
            <TouchableOpacity style={styles.editProfile}>
              <Icon name="create-outline" size={20} color="#667eea" />
            </TouchableOpacity>
          </View>
        </View>

        {/* Notifications Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Notifications</Text>
          <View style={styles.settingsGroup}>
            <SettingItem
              icon="notifications-outline"
              title="Notifications push"
              subtitle="Recevoir des rappels et alertes"
              rightComponent={
                <Switch
                  value={notificationsEnabled}
                  onValueChange={setNotificationsEnabled}
                  trackColor={{ false: "#e0e0e0", true: "#667eea" }}
                  thumbColor={notificationsEnabled ? "#fff" : "#f4f3f4"}
                />
              }
              showArrow={false}
            />
          </View>
        </View>

        {/* Appearance Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Apparence</Text>
          <View style={styles.settingsGroup}>
            <SettingItem
              icon="moon-outline"
              title="Mode sombre"
              subtitle="Interface sombre pour vos yeux"
              rightComponent={
                <Switch
                  value={darkModeEnabled}
                  onValueChange={setDarkModeEnabled}
                  trackColor={{ false: "#e0e0e0", true: "#667eea" }}
                  thumbColor={darkModeEnabled ? "#fff" : "#f4f3f4"}
                />
              }
              showArrow={false}
            />
            <SettingItem
              icon="language-outline"
              title="Langue"
              subtitle={selectedLanguage}
              onPress={handleLanguageSelect}
            />
          </View>
        </View>

        {/* Learning Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Apprentissage</Text>
          <View style={styles.settingsGroup}>
            <SettingItem
              icon="stats-chart-outline"
              title="Statistiques"
              subtitle="Voir vos progrès"
              onPress={() => console.log("Statistics pressed")}
            />
            <SettingItem
              icon="trophy-outline"
              title="Objectifs"
              subtitle="Définir vos objectifs d'apprentissage"
              onPress={() => console.log("Goals pressed")}
            />
            <SettingItem
              icon="bookmark-outline"
              title="Favoris"
              subtitle="Questions sauvegardées"
              onPress={() => console.log("Favorites pressed")}
            />
          </View>
        </View>

        {/* Support Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Support</Text>
          <View style={styles.settingsGroup}>
            <SettingItem
              icon="help-circle-outline"
              title="Aide"
              subtitle="FAQ et support"
              onPress={handleHelp}
            />
            <SettingItem
              icon="information-circle-outline"
              title="À propos"
              subtitle="Version et informations"
              onPress={handleAbout}
            />
            <SettingItem
              icon="mail-outline"
              title="Nous contacter"
              subtitle="Envoyer vos commentaires"
              onPress={() => console.log("Contact pressed")}
            />
          </View>
        </View>

        {/* Account Section */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Compte</Text>
          <View style={styles.settingsGroup}>
            <SettingItem
              icon="shield-checkmark-outline"
              title="Confidentialité"
              subtitle="Gestion des données"
              onPress={() => console.log("Privacy pressed")}
            />
            <SettingItem
              icon="sync-outline"
              title="Synchronisation"
              subtitle="Sauvegarder vos données"
              onPress={() => console.log("Sync pressed")}
            />
            <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
              <Icon name="log-out-outline" size={22} color="#e74c3c" />
              <Text style={styles.logoutText}>Déconnexion</Text>
            </TouchableOpacity>
          </View>
        </View>

        <View style={styles.bottomPadding} />
      </ScrollView>

      <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  content: {
    flex: 1,
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: "600",
    color: "#333",
    marginHorizontal: 20,
    marginBottom: 12,
  },
  settingsGroup: {
    backgroundColor: "#fff",
    marginHorizontal: 20,
    borderRadius: 12,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.05,
    shadowRadius: 3,
    elevation: 2,
  },
  settingItem: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 16,
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: "#f0f0f0",
  },
  settingLeft: {
    flexDirection: "row",
    alignItems: "center",
    flex: 1,
  },
  iconContainer: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: "#f8f9ff",
    justifyContent: "center",
    alignItems: "center",
    marginRight: 12,
  },
  settingText: {
    flex: 1,
  },
  settingTitle: {
    fontSize: 16,
    fontWeight: "500",
    color: "#333",
    marginBottom: 2,
  },
  settingSubtitle: {
    fontSize: 14,
    color: "#666",
  },
  settingRight: {
    flexDirection: "row",
    alignItems: "center",
  },
  profileCard: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    marginHorizontal: 20,
    padding: 20,
    borderRadius: 12,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  profileAvatar: {
    width: 60,
    height: 60,
    borderRadius: 30,
    backgroundColor: "#f8f9ff",
    justifyContent: "center",
    alignItems: "center",
    marginRight: 16,
  },
  profileInfo: {
    flex: 1,
  },
  profileName: {
    fontSize: 18,
    fontWeight: "600",
    color: "#333",
    marginBottom: 4,
  },
  profileEmail: {
    fontSize: 14,
    color: "#666",
  },
  editProfile: {
    padding: 8,
  },
  logoutButton: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 16,
    paddingVertical: 16,
  },
  logoutText: {
    fontSize: 16,
    fontWeight: "500",
    color: "#e74c3c",
    marginLeft: 12,
  },
  bottomPadding: {
    height: 100,
  },
});
