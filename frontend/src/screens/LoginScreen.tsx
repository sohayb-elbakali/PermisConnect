import React, { useState } from "react";
import { StyleSheet, Text, View } from "react-native";
import Button from "../components/Button";
import TextField from "../components/TextField";
import useAuth from "../hooks/useAuth";
import { validateEmail } from "../utils/validation";

const LoginScreen = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailError, setEmailError] = useState<string | undefined>(undefined);
  const [passwordError, setPasswordError] = useState<string | undefined>(undefined);
  
  const { handleLogin, error, loading } = useAuth();

  const validateForm = (): boolean => {
    let isValid = true;
    
    // Validate email
    if (!email) {
      setEmailError("Email is required");
      isValid = false;
    } else if (!validateEmail(email)) {
      setEmailError("Please enter a valid email address");
      isValid = false;
    } else {
      setEmailError(undefined);
    }
    
    // Validate password
    if (!password) {
      setPasswordError("Password is required");
      isValid = false;
    } else if (password.length < 6) {
      setPasswordError("Password must be at least 6 characters");
      isValid = false;
    } else {
      setPasswordError(undefined);
    }
    
    return isValid;
  };

  const onLogin = async () => {
    if (validateForm()) {
      const success = await handleLogin(email, password);
      if (success) {
        // Use the expo-router for navigation
        import('expo-router').then(({ router }) => {
          router.replace('/home');
        });
      }
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Login</Text>
      <TextField
        label="Email"
        value={email}
        onChangeText={(text) => {
          setEmail(text);
          if (emailError) setEmailError(undefined);
        }}
        errorMessage={emailError}
      />
      <TextField
        label="Password"
        value={password}
        onChangeText={(text) => {
          setPassword(text);
          if (passwordError) setPasswordError(undefined);
        }}
        secureTextEntry
        errorMessage={passwordError}
      />
      
      {error && <Text style={styles.errorText}>{error}</Text>}
      
      <Button 
        title="Login" 
        onPress={onLogin} 
        isLoading={loading}
        disabled={loading}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    padding: 16,
    backgroundColor: "#f5f5f5",
  },
  title: {
    fontSize: 28,
    fontWeight: "bold",
    marginBottom: 24,
    textAlign: "center",
    color: "#333",
  },
  errorText: {
    color: "red",
    textAlign: "center",
    marginVertical: 8,
  },
});

export default LoginScreen;
