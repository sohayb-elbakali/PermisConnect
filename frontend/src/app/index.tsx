import { Redirect } from 'expo-router';
import { AuthProvider } from '../contexts/AuthContext';

export default function App() {
  return (
    <AuthProvider>
      <Redirect href="/login" />
    </AuthProvider>
  );
}
