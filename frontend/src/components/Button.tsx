import React from "react";
import { ActivityIndicator, StyleSheet, Text, TouchableOpacity, ViewStyle, TextStyle } from "react-native";
import { Colors } from '../constants/Colors';

export interface ButtonProps {
  title: string;
  onPress: () => void;
  type?: 'primary' | 'secondary';
  loading?: boolean;
  disabled?: boolean;
  style?: ViewStyle;
  textStyle?: TextStyle;
}

const Button: React.FC<ButtonProps> = ({
  title,
  onPress,
  type = 'primary',
  loading = false,
  disabled = false,
  style,
  textStyle
}) => {
  return (
    <TouchableOpacity
      style={[
        styles.button,
        type === 'secondary' ? styles.secondaryButton : styles.primaryButton,
        disabled && styles.disabledButton,
        style
      ]}
      onPress={onPress}
      disabled={disabled || loading}
    >
      {loading ? (
        <ActivityIndicator color={type === 'primary' ? '#fff' : '#007AFF'} />
      ) : (
        <Text style={[
          styles.text,
          type === 'secondary' ? styles.secondaryText : styles.primaryText,
          textStyle
        ]}>
          {title}
        </Text>
      )}
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    padding: 16,
    borderRadius: 8,
    alignItems: "center",
    justifyContent: "center",
    marginVertical: 8,
  },
  primaryButton: {
    backgroundColor: "#007AFF",
  },
  secondaryButton: {
    backgroundColor: "transparent",
    borderWidth: 1,
    borderColor: "#007AFF",
  },
  disabledButton: {
    opacity: 0.5,
  },
  text: {
    fontSize: 16,
    fontWeight: "600",
  },
  primaryText: {
    color: "#fff",
  },
  secondaryText: {
    color: "#007AFF",
  },
});

export default Button;
