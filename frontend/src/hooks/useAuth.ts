import { useState } from "react";
import { useAuth as useAuthContext } from "../contexts/AuthContext";

const useAuth = () => {
  const { login, logout, user } = useAuthContext();
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const handleLogin = async (email: string, password: string): Promise<boolean> => {
    try {
      setLoading(true);
      setError(null);
      await login(email, password);
      setLoading(false);
      return true;
    } catch (err) {
      setLoading(false);
      setError(err instanceof Error ? err.message : 'An unknown error occurred');
      return false;
    }
  };

  const handleLogout = () => {
    logout();
  };

  return {
    handleLogin,
    handleLogout,
    error,
    loading,
    user,
  };
};

export default useAuth;
