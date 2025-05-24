import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  TouchableOpacity,
  ScrollView,
  Alert,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import Header from "../components/Header";
import Footer from "../components/Footer";

interface TimeSlot {
  id: string;
  time: string;
  available: boolean;
  instructor?: string;
}

interface ConfirmationProps {
  selectedDate: string;
  selectedSlot: TimeSlot;
  onConfirm: () => void;
  onCancel: () => void;
}

const ConfirmationModal: React.FC<ConfirmationProps> = ({
  selectedDate,
  selectedSlot,
  onConfirm,
  onCancel,
}) => {
  return (
    <View style={styles.modalOverlay}>
      <View style={styles.modalContainer}>
        <Text style={styles.modalTitle}>Confirm Reservation</Text>
        
        <View style={styles.detailRow}>
          <Text style={styles.detailLabel}>Date:</Text>
          <Text style={styles.detailValue}>{selectedDate}</Text>
        </View>
        
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
          <TouchableOpacity style={styles.cancelBtn} onPress={onCancel}>
            <Text style={styles.cancelBtnText}>Cancel</Text>
          </TouchableOpacity>
          
          <TouchableOpacity style={styles.confirmBtn} onPress={onConfirm}>
            <Text style={styles.confirmBtnText}>Confirm</Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

export default function CalendarScreen() {
  const [activeTab, setActiveTab] = useState("schedule");
  const [selectedDate, setSelectedDate] = useState("");
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [showTimeSlots, setShowTimeSlots] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState<TimeSlot | null>(null);

  // Sample time slots
  const timeSlots: TimeSlot[] = [
    { id: '1', time: '09:00 - 10:00', available: true, instructor: 'John Doe' },
    { id: '2', time: '10:00 - 11:00', available: true, instructor: 'Jane Smith' },
    { id: '3', time: '11:00 - 12:00', available: false, instructor: 'Mike Johnson' },
    { id: '4', time: '14:00 - 15:00', available: true, instructor: 'Sarah Wilson' },
    { id: '5', time: '15:00 - 16:00', available: true, instructor: 'Tom Brown' },
    { id: '6', time: '16:00 - 17:00', available: true, instructor: 'Lisa Davis' },
  ];

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

  const generateCalendarDays = () => {
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();
    
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDate = new Date(firstDay);
    startDate.setDate(startDate.getDate() - firstDay.getDay());
    
    const days = [];
    const today = new Date();
    
    for (let i = 0; i < 42; i++) {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);
      
      const isCurrentMonth = date.getMonth() === month;
      const isToday = 
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear();
      
      const dateString = date.toISOString().split('T')[0];
      const isSelected = selectedDate === dateString;
      
      days.push({
        date,
        dateString,
        day: date.getDate(),
        isCurrentMonth,
        isToday,
        isSelected,
      });
    }
    
    return days;
  };

  const handleDatePress = (dateString: string) => {
    setSelectedDate(dateString);
    setShowTimeSlots(true);
  };

  const handleSlotPress = (slot: TimeSlot) => {
    if (slot.available) {
      setSelectedSlot(slot);
      setShowConfirmation(true);
    } else {
      Alert.alert('Unavailable', 'This time slot is already booked.');
    }
  };

  const handleConfirmReservation = () => {
    Alert.alert(
      'Success',
      'Your lesson has been scheduled!',
      [
        {
          text: 'OK',
          onPress: () => {
            setShowConfirmation(false);
            setShowTimeSlots(false);
            setSelectedSlot(null);
          }
        }
      ]
    );
  };

  const handleCancelConfirmation = () => {
    setShowConfirmation(false);
    setSelectedSlot(null);
  };

  const navigateMonth = (direction: 'prev' | 'next') => {
    const newMonth = new Date(currentMonth);
    if (direction === 'prev') {
      newMonth.setMonth(newMonth.getMonth() - 1);
    } else {
      newMonth.setMonth(newMonth.getMonth() + 1);
    }
    setCurrentMonth(newMonth);
  };

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

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
          selectedDate={selectedDate}
          selectedSlot={selectedSlot}
          onConfirm={handleConfirmReservation}
          onCancel={handleCancelConfirmation}
        />
        <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
      </SafeAreaView>
    );
  }

  if (showTimeSlots) {
    return (
      <SafeAreaView style={styles.container}>
        <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
        
        <Header
          title="Available Times"
          onProfilePress={handleProfilePress}
          onNotificationPress={handleNotificationPress}
        />

        <View style={styles.slotsHeader}>
          <TouchableOpacity 
            style={styles.backButton}
            onPress={() => setShowTimeSlots(false)}
          >
            <Icon name="arrow-back" size={24} color="#ff6b35" />
            <Text style={styles.backText}>Back</Text>
          </TouchableOpacity>
          <Text style={styles.selectedDateText}>{selectedDate}</Text>
        </View>

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
            >
              <View style={styles.slotInfo}>
                <Text style={[
                  styles.slotTime,
                  !slot.available && styles.unavailableText
                ]}>
                  {slot.time}
                </Text>
                <Text style={[
                  styles.instructorText,
                  !slot.available && styles.unavailableText
                ]}>
                  Instructor: {slot.instructor}
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
                  {slot.available ? 'Available' : 'Booked'}
                </Text>
              </View>
            </TouchableOpacity>
          ))}
        </ScrollView>

        <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />

      <Header
        title="Schedule"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <View style={styles.calendarContainer}>
        {/* Calendar Header */}
        <View style={styles.calendarHeader}>
          <TouchableOpacity onPress={() => navigateMonth('prev')}>
            <Icon name="chevron-back" size={24} color="#ff6b35" />
          </TouchableOpacity>
          
          <Text style={styles.monthYear}>
            {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
          </Text>
          
          <TouchableOpacity onPress={() => navigateMonth('next')}>
            <Icon name="chevron-forward" size={24} color="#ff6b35" />
          </TouchableOpacity>
        </View>

        {/* Day Headers */}
        <View style={styles.dayHeaders}>
          {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day) => (
            <Text key={day} style={styles.dayHeader}>
              {day}
            </Text>
          ))}
        </View>

        {/* Calendar Grid */}
        <View style={styles.calendarGrid}>
          {generateCalendarDays().map((day, index) => (
            <TouchableOpacity
              key={index}
              style={[
                styles.dayCell,
                day.isToday && styles.todayCell,
                day.isSelected && styles.selectedCell,
                !day.isCurrentMonth && styles.otherMonthCell,
              ]}
              onPress={() => day.isCurrentMonth && handleDatePress(day.dateString)}
              disabled={!day.isCurrentMonth}
            >
              <Text
                style={[
                  styles.dayText,
                  day.isToday && styles.todayText,
                  day.isSelected && styles.selectedText,
                  !day.isCurrentMonth && styles.otherMonthText,
                ]}
              >
                {day.day}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      <Text style={styles.instructionText}>
        Tap on a date to view available time slots
      </Text>

      <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  calendarContainer: {
    marginHorizontal: 20,
    marginTop: 20,
    backgroundColor: "#fff",
    borderRadius: 15,
    padding: 20,
    elevation: 3,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
  },
  calendarHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },
  monthYear: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
  },
  dayHeaders: {
    flexDirection: "row",
    justifyContent: "space-around",
    marginBottom: 10,
  },
  dayHeader: {
    fontSize: 14,
    fontWeight: "bold",
    color: "#666",
    textAlign: "center",
    width: 40,
  },
  calendarGrid: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  dayCell: {
    width: "14.28%",
    height: 40,
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 5,
  },
  todayCell: {
    backgroundColor: "#fff0ed",
    borderRadius: 20,
  },
  selectedCell: {
    backgroundColor: "#ff6b35",
    borderRadius: 20,
  },
  otherMonthCell: {
    opacity: 0.3,
  },
  dayText: {
    fontSize: 16,
    color: "#333",
  },
  todayText: {
    color: "#ff6b35",
    fontWeight: "bold",
  },
  selectedText: {
    color: "#fff",
    fontWeight: "bold",
  },
  otherMonthText: {
    color: "#ccc",
  },
  instructionText: {
    textAlign: "center",
    color: "#666",
    marginTop: 20,
    fontSize: 16,
    paddingHorizontal: 20,
  },
  slotsHeader: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 20,
    paddingVertical: 15,
  },
  backButton: {
    flexDirection: "row",
    alignItems: "center",
  },
  backText: {
    marginLeft: 8,
    color: "#ff6b35",
    fontSize: 16,
  },
  selectedDateText: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#333",
  },
  slotsContainer: {
    flex: 1,
    paddingHorizontal: 20,
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
  instructorText: {
    fontSize: 14,
    color: "#666",
    marginTop: 4,
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
