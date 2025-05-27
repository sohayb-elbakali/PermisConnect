import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  TouchableOpacity,
  ScrollView,
  Alert,
  ActivityIndicator,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import Header from "../components/Header";
import Footer from "../components/Footer";
import moniteurService from "../services/moniteurService";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { TimeSlotDisplay } from '../services/moniteurService';

interface Moniteur {
  id: number;
  nom: string;
  prenom: string;
}

interface ConfirmationProps {
  selectedSlot: TimeSlotDisplay;
  onConfirm: () => void;
  onCancel: () => void;
}

const ConfirmationModal: React.FC<ConfirmationProps> = ({
  selectedSlot,
  onConfirm,
  onCancel,
}) => {
  return (
    <View style={styles.modalOverlay}>
      <View style={styles.modalContainer}>
        <Text style={styles.modalTitle}>Confirm Reservation</Text>
        
        <View style={styles.detailRow}>
          <Text style={styles.detailLabel}>Time:</Text>
          <Text style={styles.detailValue}>{selectedSlot.time}</Text>
        </View>
        
        {selectedSlot.instructor && (
          <View style={styles.detailRow}>
            <Text style={styles.detailLabel}>Instructor:</Text>
            <Text style={styles.detailValue}>{selectedSlot.instructor}</Text>
          </View>
        )}
        
        <View style={styles.modalButtons}>
          <TouchableOpacity 
            style={styles.cancelBtn} 
            onPress={onCancel}
            accessibilityLabel="Cancel reservation"
            accessibilityRole="button"
          >
            <Text style={styles.cancelBtnText}>Cancel</Text>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.confirmBtn} 
            onPress={onConfirm}
            accessibilityLabel="Confirm reservation"
            accessibilityRole="button"
          >
            <Text style={styles.confirmBtnText}>Confirm</Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default function CalendarScreen() {
  const [activeTab, setActiveTab] = useState("schedule");
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState<TimeSlotDisplay | null>(null);
  const [timeSlots, setTimeSlots] = useState<TimeSlotDisplay[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [moniteurs, setMoniteurs] = useState<any[]>([]);

  useEffect(() => {
    loadMoniteursAndTimeSlots();
  }, []);

  const loadMoniteursAndTimeSlots = async () => {
    try {
      setLoading(true);
      setError(null);
      // 1. Get selected autoecole
      const selectedAutoEcoleData = await AsyncStorage.getItem('selectedAutoEcole');
      if (!selectedAutoEcoleData) {
        throw new Error('No auto-école selected. Please select an auto-école first.');
      }
      const selectedAutoEcole = JSON.parse(selectedAutoEcoleData);
      // 2. Fetch moniteurs for this autoecole
      const moniteurs = await moniteurService.getMoniteursByAutoEcole(selectedAutoEcole.id);
      if (!moniteurs || moniteurs.length === 0) {
        throw new Error('No moniteurs found for this auto-école.');
      }
      setMoniteurs(moniteurs);
      // 3. Fetch all time slots for all moniteurs
      let allTimeSlots: TimeSlotDisplay[] = [];
      for (const moniteur of moniteurs) {
        try {
          const slots = await moniteurService.getMoniteurTimeSlots(moniteur.id);
          allTimeSlots = allTimeSlots.concat(slots);
        } catch (err) {
          console.error(`Error fetching slots for moniteur ${moniteur.id}:`, err);
        }
      }
      allTimeSlots.sort((a, b) => a.time.localeCompare(b.time));
      setTimeSlots(allTimeSlots);
    } catch (error) {
      setError(error instanceof Error ? error.message : 'Failed to load time slots');
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
    
    import("expo-router").then(({ router }) => {
      switch (tab) {
        case "home":
          router.push("/home");
          break;
        case "schedule":
          // Already on schedule, no action needed
          break;
        case "chat":
          router.push("/chat");
          break;
        case "settings":
          router.push("/settings");
          break;
      }
    });
  };

  const handleSlotPress = (slot: TimeSlotDisplay) => {
    if (slot.available) {
      setSelectedSlot(slot);
      setShowConfirmation(true);
    } else {
      Alert.alert('Unavailable', 'This time slot is already booked.');
    }
  };

  const handleConfirmReservation = async () => {
    if (!selectedSlot) return;

    try {
      setLoading(true);
      // Add your reservation creation logic here
      // await axios.post('http://localhost:8080/api/reservations', {
      //   timeSlotId: selectedSlot.id,
      //   clientId: currentUserId, // Get this from your auth context
      //   dateReservation: selectedDate
      // });

      Alert.alert(
        'Success',
        'Your lesson has been scheduled!',
        [
          {
            text: 'OK',
            onPress: () => {
              setShowConfirmation(false);
              setSelectedSlot(null);
              loadMoniteursAndTimeSlots(); // Refresh all time slots
            }
          }
        ]
      );
    } catch (err) {
      Alert.alert('Error', 'Failed to create reservation');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelConfirmation = () => {
    setShowConfirmation(false);
    setSelectedSlot(null);
  };

  if (showConfirmation && selectedSlot) {
    return (
      <SafeAreaView style={styles.container}>
        <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
        <Header
          title="Schedule"
          onProfilePress={handleProfilePress}
          onNotificationPress={handleNotificationPress}
        />
        <ConfirmationModal
          selectedSlot={selectedSlot}
          onConfirm={handleConfirmReservation}
          onCancel={handleCancelConfirmation}
        />
        <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      <Header
        title="Available Time Slots"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      {loading ? (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#ff6b35" />
        </View>
      ) : error ? (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>{error}</Text>
          <TouchableOpacity 
            style={styles.retryButton}
            onPress={loadMoniteursAndTimeSlots}
            accessibilityLabel="Retry loading time slots"
            accessibilityRole="button"
          >
            <Text style={styles.retryButtonText}>Retry</Text>
          </TouchableOpacity>
        </View>
      ) : timeSlots.length === 0 ? (
        <View style={styles.emptyContainer}>
          <Text style={styles.emptyText}>No time slots available</Text>
        </View>
      ) : (
        <ScrollView style={styles.slotsContainer}>
          {timeSlots.map((slot) => (
            <TouchableOpacity
              key={slot.id}
              style={[
                styles.slotCard,
                !slot.available && styles.unavailableSlot
              ]}
              onPress={() => handleSlotPress(slot)}
              disabled={!slot.available}
              accessibilityLabel={`${slot.time} with ${slot.instructor}, ${slot.available ? 'available' : 'unavailable'}`}
              accessibilityRole="button"
            >
              <View style={styles.slotInfo}>
                <Text style={styles.instructorName}>{slot.instructor}</Text>
                <Text style={[
                  styles.slotTime,
                  !slot.available && styles.unavailableText
                ]}>
                  {slot.time}
                </Text>
              </View>
              <View style={[
                styles.statusBadge,
                slot.available ? styles.availableBadge : styles.unavailableBadge
              ]}>
                <Text style={[
                  styles.statusText,
                  slot.available ? styles.availableText : styles.unavailableStatusText
                ]}>
                  {slot.status}
                </Text>
              </View>
            </TouchableOpacity>
          ))}
        </ScrollView>
      )}

      <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  slotsContainer: {
    flex: 1,
    padding: 15,
  },
  slotCard: {
    backgroundColor: "white",
    borderRadius: 12,
    padding: 15,
    marginBottom: 12,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    elevation: 2,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  unavailableSlot: {
    backgroundColor: "#f5f5f5",
    opacity: 0.7,
  },
  slotInfo: {
    flex: 1,
  },
  slotTime: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#333",
  },
  instructorName: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#2a5298',
    marginBottom: 4,
  },
  unavailableText: {
    color: "#999",
  },
  statusBadge: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 15,
  },
  availableBadge: {
    backgroundColor: "#e8f5e8",
  },
  unavailableBadge: {
    backgroundColor: "#ffe8e8",
  },
  statusText: {
    fontSize: 12,
    fontWeight: "bold",
  },
  availableText: {
    color: "#4CAF50",
  },
  unavailableStatusText: {
    color: "#f44336",
  },
  loadingContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  errorContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 20,
  },
  errorText: {
    color: "#f44336",
    fontSize: 16,
    textAlign: "center",
    marginBottom: 20,
  },
  retryButton: {
    backgroundColor: "#ff6b35",
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  retryButtonText: {
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
  },
  emptyContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 20,
  },
  emptyText: {
    fontSize: 16,
    color: "#666",
    textAlign: "center",
  },
  modalOverlay: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
  },
  modalContainer: {
    backgroundColor: "white",
    borderRadius: 15,
    padding: 25,
    margin: 20,
    minWidth: 300,
    elevation: 5,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  modalTitle: {
    fontSize: 22,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 20,
    color: "#333",
  },
  detailRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 15,
  },
  detailLabel: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#666",
  },
  detailValue: {
    fontSize: 16,
    color: "#333",
  },
  modalButtons: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 25,
  },
  cancelBtn: {
    flex: 1,
    backgroundColor: "#f44336",
    borderRadius: 8,
    padding: 15,
    marginRight: 10,
  },
  confirmBtn: {
    flex: 1,
    backgroundColor: "#4CAF50",
    borderRadius: 8,
    padding: 15,
    marginLeft: 10,
  },
  cancelBtnText: {
    color: "white",
    textAlign: "center",
    fontSize: 16,
    fontWeight: "bold",
  },
  confirmBtnText: {
    color: "white",
    textAlign: "center",
    fontSize: 16,
    fontWeight: "bold",
  },
});
