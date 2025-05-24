import React from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Animated,
  Dimensions,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";

interface Notification {
  id: string;
  title: string;
  message: string;
  time: string;
  type: "info" | "success" | "warning" | "lesson" | "reminder";
  read: boolean;
}

interface NotificationBarProps {
  isVisible: boolean;
  onClose: () => void;
  slideAnim: Animated.Value;
}

const NotificationBar: React.FC<NotificationBarProps> = ({
  isVisible,
  onClose,
  slideAnim,
}) => {
  // Sample notifications data
  const notifications: Notification[] = [
    {
      id: "1",
      title: "Leçon confirmée",
      message:
        "Votre leçon de conduite du 15 janvier à 14h00 est confirmée avec l'instructeur Marie Dupont.",
      time: "Il y a 2h",
      type: "lesson",
      read: false,
    },
    {
      id: "2",
      title: "Rappel d'examen",
      message:
        "N'oubliez pas votre examen théorique prévu demain à 10h00. Bonne chance !",
      time: "Il y a 5h",
      type: "reminder",
      read: false,
    },
    {
      id: "3",
      title: "Nouveau cours disponible",
      message:
        "Le cours 'Conduite en ville' est maintenant disponible dans votre programme.",
      time: "Hier",
      type: "info",
      read: true,
    },
    {
      id: "4",
      title: "Félicitations !",
      message:
        "Vous avez terminé le module 'Code de la route - Priorités' avec 95% de réussite !",
      time: "Il y a 2 jours",
      type: "success",
      read: true,
    },
    {
      id: "5",
      title: "Attention",
      message:
        "Votre prochaine leçon de conduite aura lieu dans un véhicule différent (Peugeot 208).",
      time: "Il y a 3 jours",
      type: "warning",
      read: true,
    },
  ];

  const getNotificationIcon = (type: string) => {
    switch (type) {
      case "lesson":
        return "car-outline";
      case "reminder":
        return "time-outline";
      case "success":
        return "checkmark-circle-outline";
      case "warning":
        return "warning-outline";
      default:
        return "information-circle-outline";
    }
  };

  const getNotificationColor = (type: string) => {
    switch (type) {
      case "lesson":
        return "#4CAF50";
      case "reminder":
        return "#FF9800";
      case "success":
        return "#4CAF50";
      case "warning":
        return "#f44336";
      default:
        return "#2196F3";
    }
  };

  const markAsRead = (id: string) => {
    // Here you would update the notification status
    console.log(`Marking notification ${id} as read`);
  };

  const clearAllNotifications = () => {
    console.log("Clearing all notifications");
    // Here you would clear all notifications
  };

  if (!isVisible) return null;

  return (
    <Animated.View
      style={[
        styles.overlay,
        {
          transform: [{ translateY: slideAnim }],
        },
      ]}
    >
      <View style={styles.notificationBar}>
        {/* Header */}
        <View style={styles.header}>
          <View style={styles.headerLeft}>
            <Icon name="notifications" size={24} color="#333" />
            <Text style={styles.headerTitle}>Notifications</Text>
            <View style={styles.badge}>
              <Text style={styles.badgeText}>
                {notifications.filter((n) => !n.read).length}
              </Text>
            </View>
          </View>
          <View style={styles.headerActions}>
            <TouchableOpacity
              style={styles.clearButton}
              onPress={clearAllNotifications}
            >
              <Text style={styles.clearButtonText}>Tout effacer</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.closeButton} onPress={onClose}>
              <Icon name="close" size={24} color="#666" />
            </TouchableOpacity>
          </View>
        </View>

        {/* Notifications List */}
        <ScrollView
          style={styles.notificationsList}
          showsVerticalScrollIndicator={false}
        >
          {notifications.length === 0 ? (
            <View style={styles.emptyState}>
              <Icon name="notifications-off-outline" size={48} color="#ccc" />
              <Text style={styles.emptyText}>Aucune notification</Text>
              <Text style={styles.emptySubtext}>
                Vous êtes à jour avec toutes vos notifications
              </Text>
            </View>
          ) : (
            notifications.map((notification) => (
              <TouchableOpacity
                key={notification.id}
                style={[
                  styles.notificationItem,
                  !notification.read && styles.unreadNotification,
                ]}
                onPress={() => markAsRead(notification.id)}
              >
                <View style={styles.notificationContent}>
                  <View style={styles.notificationLeft}>
                    <View
                      style={[
                        styles.iconContainer,
                        {
                          backgroundColor: `${getNotificationColor(
                            notification.type
                          )}15`,
                        },
                      ]}
                    >
                      <Icon
                        name={getNotificationIcon(notification.type)}
                        size={20}
                        color={getNotificationColor(notification.type)}
                      />
                    </View>
                    <View style={styles.textContainer}>
                      <Text style={styles.notificationTitle}>
                        {notification.title}
                      </Text>
                      <Text style={styles.notificationMessage}>
                        {notification.message}
                      </Text>
                      <Text style={styles.notificationTime}>
                        {notification.time}
                      </Text>
                    </View>
                  </View>
                  {!notification.read && <View style={styles.unreadDot} />}
                </View>
              </TouchableOpacity>
            ))
          )}
        </ScrollView>

        {/* Footer */}
        <View style={styles.footer}>
          <TouchableOpacity style={styles.viewAllButton}>
            <Text style={styles.viewAllText}>
              Voir toutes les notifications
            </Text>
            <Icon name="arrow-forward" size={16} color="#667eea" />
          </TouchableOpacity>
        </View>
      </View>
    </Animated.View>
  );
};

const styles = StyleSheet.create({
  overlay: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    zIndex: 1000,
  },
  notificationBar: {
    position: "absolute",
    top: 0,
    left: 0,
    right: 0,
    height: Dimensions.get("window").height * 0.7,
    backgroundColor: "#fff",
    borderBottomLeftRadius: 20,
    borderBottomRightRadius: 20,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 6,
    elevation: 8,
  },
  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 20,
    paddingTop: 60,
    paddingBottom: 20,
    borderBottomWidth: 1,
    borderBottomColor: "#f0f0f0",
  },
  headerLeft: {
    flexDirection: "row",
    alignItems: "center",
    flex: 1,
  },
  headerTitle: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#333",
    marginLeft: 12,
  },
  badge: {
    backgroundColor: "#ff6b35",
    borderRadius: 10,
    minWidth: 20,
    height: 20,
    justifyContent: "center",
    alignItems: "center",
    marginLeft: 8,
  },
  badgeText: {
    color: "#fff",
    fontSize: 12,
    fontWeight: "bold",
  },
  headerActions: {
    flexDirection: "row",
    alignItems: "center",
  },
  clearButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    marginRight: 12,
  },
  clearButtonText: {
    color: "#667eea",
    fontSize: 14,
    fontWeight: "500",
  },
  closeButton: {
    padding: 4,
  },
  notificationsList: {
    flex: 1,
    paddingHorizontal: 20,
  },
  emptyState: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    paddingVertical: 60,
  },
  emptyText: {
    fontSize: 18,
    fontWeight: "600",
    color: "#666",
    marginTop: 16,
  },
  emptySubtext: {
    fontSize: 14,
    color: "#999",
    marginTop: 8,
    textAlign: "center",
  },
  notificationItem: {
    backgroundColor: "#fff",
    borderRadius: 12,
    padding: 16,
    marginVertical: 6,
    borderWidth: 1,
    borderColor: "#f0f0f0",
  },
  unreadNotification: {
    backgroundColor: "#f8f9ff",
    borderColor: "#e3e8ff",
  },
  notificationContent: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start",
  },
  notificationLeft: {
    flexDirection: "row",
    flex: 1,
  },
  iconContainer: {
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: "center",
    alignItems: "center",
    marginRight: 12,
  },
  textContainer: {
    flex: 1,
  },
  notificationTitle: {
    fontSize: 16,
    fontWeight: "600",
    color: "#333",
    marginBottom: 4,
  },
  notificationMessage: {
    fontSize: 14,
    color: "#666",
    lineHeight: 20,
    marginBottom: 8,
  },
  notificationTime: {
    fontSize: 12,
    color: "#999",
  },
  unreadDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: "#ff6b35",
    marginTop: 8,
  },
  footer: {
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderTopWidth: 1,
    borderTopColor: "#f0f0f0",
  },
  viewAllButton: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    paddingVertical: 12,
  },
  viewAllText: {
    fontSize: 16,
    color: "#667eea",
    fontWeight: "500",
    marginRight: 8,
  },
});

export default NotificationBar;
