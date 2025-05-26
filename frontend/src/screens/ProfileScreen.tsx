import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  TouchableOpacity,
  ScrollView,
  TextInput,
  Alert,
  Image,
  ActivityIndicator,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { router } from "expo-router";
import { useAuth } from "../hooks/useAuth";
import userService from "../services/userService";
import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";
import { API_URL } from "../config";

interface UserProfile {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  dateNaissance: string;
  adresse: string;
  ville: string;
  codePostal: string;
  autoEcole: {
    id: number;
    nom: string;
  };
}

export default function ProfileScreen() {
  const [activeTab, setActiveTab] = useState("profile");
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [editedProfile, setEditedProfile] = useState<UserProfile | null>(null);

  const { user } = useAuth();

  useEffect(() => {
    loadUserProfile();
  }, []);

  const loadUserProfile = async () => {
    try {
      const userInfoStr = await AsyncStorage.getItem("userInfo");
      if (userInfoStr) {
        const userData = JSON.parse(userInfoStr);
        // Get the token from AsyncStorage
        const token = await AsyncStorage.getItem("token");
        if (!token) {
          throw new Error("No authentication token found");
        }

        // Configure axios with the token
        const response = await axios.get(`${API_URL}/clients/${userData.id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (response.data) {
          setProfile(response.data);
          setEditedProfile(response.data);
        }
      }
    } catch (error) {
      console.error("Error loading profile:", error);
      Alert.alert("Error", "Failed to load profile information. Please try again.");
    } finally {
      setLoading(false);
    }
  };

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
        router.push("/settings");
        break;
    }
  };

  const handleEditToggle = () => {
    if (isEditing) {
      // Save changes
      saveProfileChanges();
    } else {
      setIsEditing(true);
    }
  };

  const saveProfileChanges = async () => {
    if (!editedProfile) return;

    try {
      setSaving(true);
      const token = await AsyncStorage.getItem("token");
      if (!token) {
        throw new Error("No authentication token found");
      }

      const response = await axios.put(
        `${API_URL}/clients/${editedProfile.id}`,
        editedProfile,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      if (response.data) {
        setProfile(response.data);
        setIsEditing(false);
        Alert.alert("Success", "Profile updated successfully");
      }
    } catch (error) {
      console.error("Error updating profile:", error);
      Alert.alert("Error", "Failed to update profile. Please try again.");
    } finally {
      setSaving(false);
    }
  };

  const handleInputChange = (field: keyof UserProfile, value: string) => {
    if (editedProfile) {
      setEditedProfile({
        ...editedProfile,
        [field]: value,
      });
    }
  };

  const handleChangeAvatar = () => {
    Alert.alert(
      "Changer la photo de profil",
      "Choisissez une option",
      [
        { text: "Caméra", onPress: () => console.log("Camera selected") },
        { text: "Galerie", onPress: () => console.log("Gallery selected") },
        { text: "Annuler", style: "cancel" },
      ]
    );
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#ff6b35" />
      </View>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      <Header
        title="My Profile"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
        <View style={styles.profileHeader}>
          <View style={styles.avatarContainer}>
            <Image source={{ uri: profile?.autoEcole?.nom }} style={styles.avatar} />
            {isEditing && (
              <TouchableOpacity style={styles.avatarEdit} onPress={handleChangeAvatar}>
                <Icon name="camera" size={20} color="#fff" />
              </TouchableOpacity>
            )}
          </View>
          <View style={styles.headerInfo}>
            <Text style={styles.userName}>{profile?.nom} {profile?.prenom}</Text>
            <Text style={styles.userEmail}>{profile?.email}</Text>
            <View style={styles.licensebadge}>
              <Icon name="card-outline" size={16} color="#667eea" />
              <Text style={styles.licenseText}>{profile?.autoEcole?.nom}</Text>
            </View>
          </View>
          <TouchableOpacity
            style={styles.editButton}
            onPress={handleEditToggle}
            disabled={saving}
          >
            <Text style={styles.editButtonText}>
              {isEditing ? (saving ? "Saving..." : "Save") : "Edit Profile"}
            </Text>
          </TouchableOpacity>
        </View>

        <View style={styles.formContainer}>
          <View style={styles.inputGroup}>
            <Text style={styles.label}>First Name</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.prenom}
              onChangeText={(value) => handleInputChange("prenom", value)}
              editable={isEditing}
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Last Name</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.nom}
              onChangeText={(value) => handleInputChange("nom", value)}
              editable={isEditing}
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Email</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.email}
              onChangeText={(value) => handleInputChange("email", value)}
              editable={isEditing}
              keyboardType="email-address"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Phone</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.telephone}
              onChangeText={(value) => handleInputChange("telephone", value)}
              editable={isEditing}
              keyboardType="phone-pad"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Date of Birth</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.dateNaissance}
              onChangeText={(value) => handleInputChange("dateNaissance", value)}
              editable={isEditing}
              placeholder="YYYY-MM-DD"
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Address</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.adresse}
              onChangeText={(value) => handleInputChange("adresse", value)}
              editable={isEditing}
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>City</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.ville}
              onChangeText={(value) => handleInputChange("ville", value)}
              editable={isEditing}
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Postal Code</Text>
            <TextInput
              style={[styles.input, !isEditing && styles.inputDisabled]}
              value={editedProfile?.codePostal}
              onChangeText={(value) => handleInputChange("codePostal", value)}
              editable={isEditing}
            />
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Driving School</Text>
            <Text style={styles.drivingSchoolText}>
              {profile?.autoEcole?.nom}
            </Text>
          </View>
        </View>
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
  loadingContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  content: {
    flex: 1,
  },
  profileHeader: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    marginHorizontal: 20,
    marginTop: 20,
    marginBottom: 24,
    padding: 20,
    borderRadius: 16,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  avatarContainer: {
    position: "relative",
    marginRight: 16,
  },
  avatar: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: "#f0f0f0",
  },
  avatarEdit: {
    position: "absolute",
    bottom: 0,
    right: 0,
    backgroundColor: "#667eea",
    width: 28,
    height: 28,
    borderRadius: 14,
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 2,
    borderColor: "#fff",
  },
  headerInfo: {
    flex: 1,
  },
  userName: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 4,
  },
  userEmail: {
    fontSize: 14,
    color: "#666",
    marginBottom: 8,
  },
  licenseType: {
    fontSize: 12,
    color: "#667eea",
    backgroundColor: "#f0f2ff",
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    alignSelf: "flex-start",
  },
  editButton: {
    padding: 8,
    borderRadius: 20,
    backgroundColor: "#f0f2ff",
  },
  editButtonText: {
    color: "#667eea",
    fontWeight: "bold",
  },
  formContainer: {
    padding: 20,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    color: "#2c3e50",
    marginBottom: 5,
  },
  input: {
    backgroundColor: "#fff",
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
  },
  inputDisabled: {
    backgroundColor: "#f8f9fa",
    color: "#666",
  },
  drivingSchoolText: {
    fontSize: 16,
    color: "#666",
    padding: 12,
    backgroundColor: "#f8f9fa",
    borderRadius: 8,
    borderWidth: 1,
    borderColor: "#ddd",
  },
  licensebadge: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#f0f2ff",
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    alignSelf: "flex-start",
  },
  licenseText: {
    fontSize: 12,
    color: "#667eea",
    marginLeft: 4,
    fontWeight: "500",
  },
  actionsSection: {
    marginHorizontal: 20,
    marginBottom: 24,
  },
  actionButton: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    paddingHorizontal: 16,
    paddingVertical: 16,
    borderRadius: 12,
    marginBottom: 8,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 3,
    elevation: 2,
  },
  actionButtonText: {
    flex: 1,
    fontSize: 16,
    color: "#333",
    marginLeft: 12,
    fontWeight: "500",
  },
  dangerButton: {
    borderWidth: 1,
    borderColor: "#fee",
  },
  dangerText: {
    color: "#e74c3c",
  },
  bottomPadding: {
    height: 100,
  },
});
