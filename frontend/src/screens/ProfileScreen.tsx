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
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { router } from "expo-router";
import { useAuth } from "../hooks/useAuth";

interface UserProfile {
  id: string;
  name: string;
  email: string;
  phone: string;
  dateOfBirth: string;
  address: string;
  city: string;
  postalCode: string;
  licenseType: string;
  instructorName: string;
  avatar?: string;
  theoreticalTestDate?: string;
  practicalTestDate?: string;
  emergencyContact: {
    name: string;
    phone: string;
    relationship: string;
  };
}

export default function ProfileScreen() {
  const [activeTab, setActiveTab] = useState<string>('home');
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [profile, setProfile] = useState<UserProfile>({
    id: "1",
    name: "Jean Dupont",
    email: "jean.dupont@email.com",
    phone: "+33 6 12 34 56 78",
    dateOfBirth: "1995-03-15",
    address: "123 Rue de la Paix",
    city: "Paris",
    postalCode: "75001",
    licenseType: "Permis B",
    instructorName: "Marie Martin",
    avatar: "https://via.placeholder.com/120",
    theoreticalTestDate: "2024-02-15",
    practicalTestDate: "2024-03-20",
    emergencyContact: {
      name: "Marie Dupont",
      phone: "+33 6 98 76 54 32",
      relationship: "Épouse",
    },
  });

  const { user } = useAuth();

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
      Alert.alert(
        "Sauvegarder les modifications",
        "Voulez-vous sauvegarder les modifications apportées à votre profil ?",
        [
          { text: "Annuler", style: "cancel" },
          { 
            text: "Sauvegarder", 
            onPress: () => {
              setIsEditing(false);
              Alert.alert("Succès", "Profil mis à jour avec succès !");
            }
          },
        ]
      );
    } else {
      setIsEditing(true);
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

  const updateProfile = (field: keyof UserProfile, value: any) => {
    if (field === 'emergencyContact') {
      setProfile(prev => ({
        ...prev,
        emergencyContact: { ...prev.emergencyContact, ...value }
      }));
    } else {
      setProfile(prev => ({ ...prev, [field]: value }));
    }
  };

  const ProfileField = ({ 
    label, 
    value, 
    field, 
    keyboardType = "default",
    multiline = false 
  }: {
    label: string;
    value: string;
    field: keyof UserProfile;
    keyboardType?: "default" | "email-address" | "phone-pad";
    multiline?: boolean;
  }) => (
    <View style={styles.fieldContainer}>
      <Text style={styles.fieldLabel}>{label}</Text>
      {isEditing ? (
        <TextInput
          style={[styles.fieldInput, multiline && styles.multilineInput]}
          value={value}
          onChangeText={(text) => updateProfile(field, text)}
          keyboardType={keyboardType}
          multiline={multiline}
          numberOfLines={multiline ? 3 : 1}
        />
      ) : (
        <Text style={styles.fieldValue}>{value}</Text>
      )}
    </View>
  );

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      <Header
        title="Mon Profil"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
        {/* Profile Header */}
        <View style={styles.profileHeader}>
          <View style={styles.avatarContainer}>
            <Image source={{ uri: profile.avatar }} style={styles.avatar} />
            {isEditing && (
              <TouchableOpacity style={styles.avatarEdit} onPress={handleChangeAvatar}>
                <Icon name="camera" size={20} color="#fff" />
              </TouchableOpacity>
            )}
          </View>
          <View style={styles.headerInfo}>
            <Text style={styles.userName}>{profile.name}</Text>
            <Text style={styles.userEmail}>{profile.email}</Text>
            <View style={styles.licensebadge}>
              <Icon name="card-outline" size={16} color="#667eea" />
              <Text style={styles.licenseText}>{profile.licenseType}</Text>
            </View>
          </View>
          <TouchableOpacity style={styles.editButton} onPress={handleEditToggle}>
            <Icon name={isEditing ? "checkmark" : "create-outline"} size={20} color="#667eea" />
          </TouchableOpacity>
        </View>

        {/* Personal Information */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Icon name="person-outline" size={20} color="#667eea" />
            <Text style={styles.sectionTitle}>Informations personnelles</Text>
          </View>
          <View style={styles.sectionContent}>
            <ProfileField
              label="Nom complet"
              value={profile.name}
              field="name"
            />
            <ProfileField
              label="Email"
              value={profile.email}
              field="email"
              keyboardType="email-address"
            />
            <ProfileField
              label="Téléphone"
              value={profile.phone}
              field="phone"
              keyboardType="phone-pad"
            />
            <ProfileField
              label="Date de naissance"
              value={profile.dateOfBirth}
              field="dateOfBirth"
            />
          </View>
        </View>

        {/* Address Information */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Icon name="location-outline" size={20} color="#667eea" />
            <Text style={styles.sectionTitle}>Adresse</Text>
          </View>
          <View style={styles.sectionContent}>
            <ProfileField
              label="Adresse"
              value={profile.address}
              field="address"
              multiline
            />
            <View style={styles.row}>
              <View style={styles.halfField}>
                <ProfileField
                  label="Ville"
                  value={profile.city}
                  field="city"
                />
              </View>
              <View style={styles.halfField}>
                <ProfileField
                  label="Code postal"
                  value={profile.postalCode}
                  field="postalCode"
                />
              </View>
            </View>
          </View>
        </View>

        {/* Driving Information */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Icon name="car-outline" size={20} color="#667eea" />
            <Text style={styles.sectionTitle}>Informations de conduite</Text>
          </View>
          <View style={styles.sectionContent}>
            <ProfileField
              label="Type de permis"
              value={profile.licenseType}
              field="licenseType"
            />
            <ProfileField
              label="Instructeur"
              value={profile.instructorName}
              field="instructorName"
            />
            {profile.theoreticalTestDate && (
              <ProfileField
                label="Date examen théorique"
                value={profile.theoreticalTestDate}
                field="theoreticalTestDate"
              />
            )}
            {profile.practicalTestDate && (
              <ProfileField
                label="Date examen pratique"
                value={profile.practicalTestDate}
                field="practicalTestDate"
              />
            )}
          </View>
        </View>

        {/* Emergency Contact */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Icon name="medical-outline" size={20} color="#667eea" />
            <Text style={styles.sectionTitle}>Contact urgence</Text>
          </View>
          <View style={styles.sectionContent}>
            <View style={styles.fieldContainer}>
              <Text style={styles.fieldLabel}>Nom</Text>
              {isEditing ? (
                <TextInput
                  style={styles.fieldInput}
                  value={profile.emergencyContact.name}
                  onChangeText={(text) => updateProfile('emergencyContact', { name: text })}
                />
              ) : (
                <Text style={styles.fieldValue}>{profile.emergencyContact.name}</Text>
              )}
            </View>
            <View style={styles.fieldContainer}>
              <Text style={styles.fieldLabel}>Téléphone</Text>
              {isEditing ? (
                <TextInput
                  style={styles.fieldInput}
                  value={profile.emergencyContact.phone}
                  onChangeText={(text) => updateProfile('emergencyContact', { phone: text })}
                  keyboardType="phone-pad"
                />
              ) : (
                <Text style={styles.fieldValue}>{profile.emergencyContact.phone}</Text>
              )}
            </View>
            <View style={styles.fieldContainer}>
              <Text style={styles.fieldLabel}>Relation</Text>
              {isEditing ? (
                <TextInput
                  style={styles.fieldInput}
                  value={profile.emergencyContact.relationship}
                  onChangeText={(text) => updateProfile('emergencyContact', { relationship: text })}
                />
              ) : (
                <Text style={styles.fieldValue}>{profile.emergencyContact.relationship}</Text>
              )}
            </View>
          </View>
        </View>

        {/* Actions */}
        <View style={styles.actionsSection}>
          <TouchableOpacity style={styles.actionButton}>
            <Icon name="download-outline" size={20} color="#667eea" />
            <Text style={styles.actionButtonText}>Télécharger mon dossier</Text>
            <Icon name="chevron-forward" size={16} color="#999" />
          </TouchableOpacity>
          
          <TouchableOpacity style={styles.actionButton}>
            <Icon name="share-outline" size={20} color="#667eea" />
            <Text style={styles.actionButtonText}>Partager mes informations</Text>
            <Icon name="chevron-forward" size={16} color="#999" />
          </TouchableOpacity>
          
          <TouchableOpacity style={[styles.actionButton, styles.dangerButton]}>
            <Icon name="trash-outline" size={20} color="#e74c3c" />
            <Text style={[styles.actionButtonText, styles.dangerText]}>Supprimer mon compte</Text>
            <Icon name="chevron-forward" size={16} color="#e74c3c" />
          </TouchableOpacity>
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
  section: {
    marginBottom: 24,
  },
  sectionHeader: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 20,
    marginBottom: 12,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: "600",
    color: "#333",
    marginLeft: 8,
  },
  sectionContent: {
    backgroundColor: "#fff",
    marginHorizontal: 20,
    borderRadius: 12,
    padding: 16,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 3,
    elevation: 2,
  },
  fieldContainer: {
    marginBottom: 16,
  },
  fieldLabel: {
    fontSize: 14,
    fontWeight: "500",
    color: "#666",
    marginBottom: 6,
  },
  fieldValue: {
    fontSize: 16,
    color: "#333",
    paddingVertical: 8,
  },
  fieldInput: {
    fontSize: 16,
    color: "#333",
    borderWidth: 1,
    borderColor: "#e0e0e0",
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
    backgroundColor: "#fafafa",
  },
  multilineInput: {
    height: 80,
    textAlignVertical: "top",
  },
  row: {
    flexDirection: "row",
    gap: 12,
  },
  halfField: {
    flex: 1,
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
