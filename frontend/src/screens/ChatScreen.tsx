import React, { useState, useEffect, useRef } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  ActivityIndicator,
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
import Footer from "../components/Footer";
import Header from "../components/Header";
import { router } from "expo-router";

interface Message {
  id: number;
  text: string;
  sender: "user" | "ai";
  timestamp: string;
}

export default function Chat() {
  const [activeTab, setActiveTab] = useState<string>('chat');
  const [messages, setMessages] = useState<Message[]>([
    {
      id: 1,
      text: "Bonjour ! Je suis votre assistant PermisConnect. Comment puis-je vous aider avec vos questions sur le permis de conduire ?",
      sender: "ai",
      timestamp: new Date().toISOString(),
    },
  ]);
  const [newMessage, setNewMessage] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const scrollViewRef = useRef<ScrollView>(null);

  const scrollToBottom = () => {
    scrollViewRef.current?.scrollToEnd({ animated: true });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleProfilePress = () => {
    console.log("Profile pressed");
  };

  const handleNotificationPress = () => {
    console.log("Notification pressed");
  };

  const handleBottomNavPress = (tab: string) => {
    console.log(`Navigating to: ${tab}`); // Debug log
    setActiveTab(tab);
    
    switch (tab) {
      case "home":
        router.push("/home");
        break;
      case "schedule":
        router.push("/calendar");
        break;
      case "chat":
        // Already on chat, no navigation needed
        console.log("Already on chat screen");
        break;
      case "settings":
        router.push("/settings");
      
        break;
    }
  };

  const handleSendMessage = async () => {
    if (!newMessage.trim()) return;

    const userMessage: Message = {
      id: Date.now(),
      text: newMessage,
      sender: "user",
      timestamp: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setNewMessage("");
    setIsLoading(true);

    // Simulate AI response with realistic delay
    setTimeout(() => {
      const responses = [
        "C'est une excellente question ! Pour le code de la route, il est important de bien connaître les règles de priorité.",
        "Je vous recommande de réviser ce point dans votre manuel. Voulez-vous que je vous explique davantage ?",
        "Cette question revient souvent dans l'examen. La réponse dépend du contexte de circulation.",
        "N'hésitez pas à me poser d'autres questions sur le permis de conduire !",
        "Pour cette situation, vous devez respecter la signalisation en place et adapter votre vitesse.",
      ];

      const aiMessage: Message = {
        id: Date.now() + 1,
        text: responses[Math.floor(Math.random() * responses.length)],
        sender: "ai",
        timestamp: new Date().toISOString(),
      };

      setMessages((prev) => [...prev, aiMessage]);
      setIsLoading(false);
    }, 1500);
  };

  const formatTimestamp = (timestamp: string): string => {
    return new Date(timestamp).toLocaleTimeString("fr-FR", {
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const clearChat = () => {
    setMessages([
      {
        id: 1,
        text: "Conversation effacée. Comment puis-je vous aider ?",
        sender: "ai",
        timestamp: new Date().toISOString(),
      },
    ]);
  };

  const TypingIndicator = () => (
    <View style={styles.typingContainer}>
      <View style={styles.typingDot} />
      <View style={[styles.typingDot, { opacity: 0.7 }]} />
      <View style={[styles.typingDot, { opacity: 0.4 }]} />
    </View>
  );

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      <Header
        title="Assistant PermisConnect"
        onProfilePress={handleProfilePress}
        onNotificationPress={handleNotificationPress}
      />

      <View style={styles.chatHeader}>
        <View style={styles.headerContent}>
          <Text style={styles.headerSubtitle}>
            Posez vos questions sur le code de la route
          </Text>
        </View>
        <TouchableOpacity style={styles.clearButton} onPress={clearChat}>
          <Text style={styles.clearButtonText}>Effacer</Text>
        </TouchableOpacity>
      </View>

      <ScrollView 
        ref={scrollViewRef}
        style={styles.chatMessages}
        contentContainerStyle={styles.messagesContainer}
        showsVerticalScrollIndicator={false}
      >
        {messages.map((message) => (
          <View
            key={message.id}
            style={[
              styles.message,
              message.sender === "user" ? styles.userMessage : styles.aiMessage,
            ]}
          >
            <View
              style={[
                styles.messageContent,
                message.sender === "user"
                  ? styles.userMessageContent
                  : styles.aiMessageContent,
              ]}
            >
              <Text style={[
                styles.messageText,
                message.sender === "user" && styles.userMessageText
              ]}>
                {message.text}
              </Text>
              <Text style={[
                styles.timestamp,
                message.sender === "user" && styles.userTimestamp
              ]}>
                {formatTimestamp(message.timestamp)}
              </Text>
            </View>
          </View>
        ))}

        {isLoading && (
          <View style={[styles.message, styles.aiMessage]}>
            <View style={[styles.messageContent, styles.aiMessageContent]}>
              <TypingIndicator />
            </View>
          </View>
        )}
      </ScrollView>

      <View style={styles.chatInput}>
        <TextInput
          style={styles.input}
          value={newMessage}
          onChangeText={setNewMessage}
          placeholder="Tapez votre question..."
          editable={!isLoading}
          multiline
          maxLength={500}
        />
        <TouchableOpacity
          style={[
            styles.sendButton,
            (!newMessage.trim() || isLoading) && styles.sendButtonDisabled,
          ]}
          onPress={handleSendMessage}
          disabled={!newMessage.trim() || isLoading}
        >
          {isLoading ? (
            <ActivityIndicator color="#fff" size="small" />
          ) : (
            <Icon name="send" size={20} color="#fff" />
          )}
        </TouchableOpacity>
      </View>

      <Footer activeTab={activeTab} onTabPress={handleBottomNavPress} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f8f9fa",
  },
  chatHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 20,
    paddingVertical: 15,
    backgroundColor: "#667eea",
  },
  headerContent: {
    flex: 1,
  },
  headerSubtitle: {
    color: "white",
    fontSize: 16,
    opacity: 0.9,
  },
  clearButton: {
    backgroundColor: "rgba(255, 255, 255, 0.2)",
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
  },
  clearButtonText: {
    color: "white",
    fontSize: 14,
    fontWeight: "500",
  },
  chatMessages: {
    flex: 1,
    backgroundColor: "#fff",
  },
  messagesContainer: {
    padding: 20,
    paddingBottom: 100,
  },
  message: {
    marginBottom: 16,
    maxWidth: "80%",
  },
  userMessage: {
    alignSelf: "flex-end",
  },
  aiMessage: {
    alignSelf: "flex-start",
  },
  messageContent: {
    padding: 12,
    borderRadius: 18,
  },
  userMessageContent: {
    backgroundColor: "#667eea",
    borderBottomRightRadius: 6,
  },
  aiMessageContent: {
    backgroundColor: "#f1f3f4",
    borderBottomLeftRadius: 6,
  },
  messageText: {
    fontSize: 16,
    lineHeight: 20,
    color: "#333",
  },
  userMessageText: {
    color: "#fff",
  },
  timestamp: {
    fontSize: 12,
    opacity: 0.7,
    marginTop: 4,
    color: "#666",
  },
  userTimestamp: {
    color: "#fff",
    opacity: 0.8,
  },
  typingContainer: {
    flexDirection: "row",
    alignItems: "center",
    gap: 4,
  },
  typingDot: {
    width: 8,
    height: 8,
    backgroundColor: "#999",
    borderRadius: 4,
  },
  chatInput: {
    flexDirection: "row",
    padding: 20,
    backgroundColor: "#f8f9fa",
    borderTopWidth: 1,
    borderTopColor: "#e9ecef",
    alignItems: "flex-end",
    marginBottom: 80,
  },
  input: {
    flex: 1,
    borderWidth: 2,
    borderColor: "#e9ecef",
    borderRadius: 25,
    paddingHorizontal: 16,
    paddingVertical: 12,
    fontSize: 16,
    backgroundColor: "#fff",
    maxHeight: 100,
    marginRight: 12,
  },
  sendButton: {
    backgroundColor: "#667eea",
    width: 45,
    height: 45,
    borderRadius: 22.5,
    justifyContent: "center",
    alignItems: "center",
  },
  sendButtonDisabled: {
    backgroundColor: "#bdc3c7",
  },
});
