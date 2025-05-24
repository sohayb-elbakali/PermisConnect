import React from "react";
import { View, Text, StyleSheet, ViewStyle } from "react-native";
import Svg, { Circle } from "react-native-svg";
import Icon from "react-native-vector-icons/Ionicons";

// Define props interface for ProgressCircle component
interface ProgressCircleProps {
  size: number;
  strokeWidth: number;
  progress: number;
  color: string;
  icon: string;
  label: string;
  style?: ViewStyle;
}

const ProgressCircle: React.FC<ProgressCircleProps> = ({
  size,
  strokeWidth,
  progress,
  color,
  icon,
  label,
  style,
}) => {
  const radius = (size - strokeWidth) / 2;
  const circumference = radius * 2 * Math.PI;
  const strokeDashoffset = circumference - (progress / 100) * circumference;

  // Split label into two lines if too long
  const formattedLabel = label.length > 10 
    ? label.split(' ').map(word => word.toUpperCase())
    : [label];

  return (
    <View style={[styles.container, style]}>
      <Svg width={size} height={size}>
        <Circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke="#E6E6E6"
          strokeWidth={strokeWidth}
          fill="transparent"
        />
        <Circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke={color}
          strokeWidth={strokeWidth}
          strokeDasharray={`${circumference} ${circumference}`}
          strokeDashoffset={strokeDashoffset}
          strokeLinecap="round"
          fill="transparent"
          transform={`rotate(-90, ${size / 2}, ${size / 2})`}
        />
      </Svg>
      <View style={styles.content}>
        <Icon name={icon} size={28} color={color} />
        {formattedLabel.map((part, index) => (
          <Text 
            key={index} 
            style={[styles.label, { color, marginTop: index === 0 ? 6 : 0 }]}
          >
            {part}
          </Text>
        ))}
        <Text style={[styles.progress, { color }]}>{progress}%</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: "center",
    alignItems: "center",
  },
  content: {
    position: "absolute",
    justifyContent: "center",
    alignItems: "center",
    width: "80%",
  },
  label: {
    fontSize: 10,
    fontWeight: "bold",
    textAlign: "center",
  },
  progress: {
    fontSize: 16,
    fontWeight: "bold",
    marginTop: 4,
  },
});

export default ProgressCircle;
