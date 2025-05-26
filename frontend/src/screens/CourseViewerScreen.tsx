import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  Image,
  Dimensions,
  Platform,
  TouchableOpacity,
  Alert,
  ActivityIndicator
} from 'react-native';
import Header from '../components/Header';
import { Video } from 'react-native-video'; // Import Video
import axios from 'axios'; // Import axios
import AsyncStorage from '@react-native-async-storage/async-storage'; // Import AsyncStorage
import { API_URL } from '../config'; // Import API_URL
import { useRouter, useLocalSearchParams } from 'expo-router';
import { useAuth } from '../contexts/AuthContext';
import Pdf from 'react-native-pdf';

const { width, height } = Dimensions.get('window');

type CourseViewerParams = {
  cloudinaryUrl: string;
  fileType: string;
  courseId: string;
  courseType: string;
};

export default function CourseViewerScreen() {
  const params = useLocalSearchParams<CourseViewerParams>();
  const { cloudinaryUrl, fileType, courseId, courseType } = params;
  const { user } = useAuth();
  const router = useRouter();
  const [isCompleted, setIsCompleted] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  console.log('CourseViewerScreen params:', { cloudinaryUrl, fileType, courseId, courseType });

  useEffect(() => {
    console.log('useEffect running with user:', user, 'courseId:', courseId, 'courseType:', courseType);
    // Only check completion status for trackable course types
    if (user?.id && courseId && (courseType === 'PUBLIC' || courseType === 'PRIVATE')) {
      checkCompletionStatus();
    } else {
      // If not a trackable course type or missing info, assume no completion tracking needed
      // Still set loading to false to render content and buttons
      console.log('Not a trackable course type or missing info, setting isLoading false');
      setIsLoading(false);
    }
  }, [user?.id, courseId, courseType]);

  const checkCompletionStatus = async () => {
    console.log('Checking completion status for user:', user?.id);
    try {
      // This endpoint currently gives overall theoretical progress, not individual course completion.
      // To accurately check individual course completion, the backend API would need to be updated.
      // For now, we'll assume if the user has viewed *any* theoretical courses, the check is complete.
      // This logic needs refinement based on the actual backend implementation for individual course tracking.
      const response = await axios.get(`${API_URL}/api/courses/progress/theoretical/${user?.id}`);
      console.log('Completion status response data:', response.data);
      const viewedCourses = response.data.viewedTheoreticalCourses;

      // *** IMPORTANT: This logic is temporary and needs backend support for individual course completion check ***
      // For now, it just checks if the user has viewed > 0 theoretical courses in total.
      // It does NOT check if the *current* course has been viewed.
      // A proper implementation would require a backend endpoint like `/api/courses/{courseId}/completionStatus/{clientId}`
      const completed = viewedCourses > 0; 
      console.log('Is course completed based on API response (CURRENTLY INACCURATE FOR INDIVIDUAL COURSE):', completed);
      setIsCompleted(completed);

    } catch (error) {
      console.error('Error checking completion status:', error);
      Alert.alert('Error', 'Failed to check course completion status. Progress tracking may not be accurate.');
      setIsCompleted(false); // Assume not completed on error
    } finally {
      console.log('checkCompletionStatus finished, setting isLoading false');
      setIsLoading(false);
    }
  };

  const recordCourseView = async () => {
    console.log('Attempting to record course view');
    const isTrackableCourse = courseType === 'PUBLIC' || courseType === 'PRIVATE';

    // Only attempt to record view if it's a trackable course type
    if (!isTrackableCourse) {
         Alert.alert('Info', 'Completion tracking is only available for theoretical courses.');
         // We might still want to navigate back here or just let the user view
         // router.back();
         return; // Exit if not a trackable course type
    }

    if (!user?.id || !courseId) {
         Alert.alert('Error', 'User or Course ID not available.');
         return; // Exit if necessary data is missing
    }

    try {
      console.log('Sending POST request to record view', { clientId: user.id, courseId: parseInt(courseId) });
      setIsLoading(true);
      await axios.post(`${API_URL}/api/courses/view`, {
        clientId: user.id,
        courseId: parseInt(courseId)
      });
      console.log('Course view recorded successfully');
      Alert.alert('Success', 'Course marked as completed!');
      router.back();
    } catch (error) {
      console.error('Error recording course view:', error);
      // Check for specific error like 401 Unauthorized
      if (axios.isAxiosError(error) && error.response?.status === 401) {
           Alert.alert('Error', 'Unauthorized. Please login again.');
           // Optionally redirect to login
           // router.replace('/login');
      } else {
           Alert.alert('Error', 'Failed to mark course as completed.');
      }
    } finally {
      console.log('recordCourseView finished, setting isLoading false');
      setIsLoading(false);
    }
  };

  const handleComplete = async () => {
    console.log('handleComplete pressed');
    if (!isLoading) { // Prevent multiple clicks while loading
        await recordCourseView();
    }
  };

  const handleNotYet = () => {
    console.log('handleNotYet pressed');
    router.back(); // Simply go back without marking as completed
  };

  const renderContent = () => {
    if (!cloudinaryUrl) {
      return <Text style={styles.message}>No course material available.</Text>;
    }

    switch (fileType) {
      case 'image':
        return (
          <Image
            source={{ uri: cloudinaryUrl }}
            style={styles.mediaContent}
            resizeMode="contain"
          />
        );
      case 'video':
        return (
          <Video
            source={{ uri: cloudinaryUrl }}
            style={styles.mediaContent}
            controls={true}
            resizeMode="contain"
          />
        );
      case 'raw':
        if (Platform.OS === 'web') {
          return <Text style={styles.message}>PDF viewing is not supported on web. Please use the mobile app.</Text>;
        }
        // Added null check for Pdf component import
        if (!Pdf) {
             return <Text style={styles.message}>PDF viewing component not available.</Text>;
        }
        const source = { uri: cloudinaryUrl, cache: true };
        return (
          <Pdf
            source={source}
            style={styles.pdfContent}
            onLoadComplete={(numberOfPages, filePath) => {
              console.log(`Number of pages: ${numberOfPages}`);
            }}
            onPageChanged={(page, numberOfPages) => {
              console.log(`Current page: ${page}`);
            }}
            onError={(error) => {
              console.error('PDF error:', error);
              Alert.alert('Error', 'Failed to load PDF. Please try again later.');
            }}
            // Enable this if you need to scale the PDF to fit the screen width
            // scale={1.0}
            // minScale={0.5} maxScale={2.0}
          />
        );
      default:
        return <Text style={styles.message}>Preview not available for this file type ({fileType}).</Text>;
    }
  };

  const isTrackableCourse = courseType === 'PUBLIC' || courseType === 'PRIVATE';
  console.log('Is trackable course:', isTrackableCourse, 'Course type:', courseType);
  console.log('Current isLoading state:', isLoading);
  console.log('Current isCompleted state:', isCompleted);

  const showButtonContainer = !isLoading; // Decide whether to render based on loading
  console.log('Rendering button container:', showButtonContainer);

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      {/* You might want a dynamic header title */}
      <Header title="Course Material" /> 

      <View style={styles.contentContainer}>
        {renderContent()}
        {isLoading && ( // Show loading indicator over content if loading
           <View style={styles.loadingOverlay}>
               <ActivityIndicator size="large" color="#ff6b35" />
           </View>
        )}
      </View>
      
      {/* Add completion buttons - always rendered when not loading */}
      { showButtonContainer && (
        <View style={styles.buttonContainer}>
          <TouchableOpacity
            style={[styles.button, isCompleted && isTrackableCourse ? styles.completedButton : styles.notYetButton]}
            onPress={handleComplete}
            // Disable if already completed (and trackable) or loading
            disabled={(isCompleted && isTrackableCourse) || isLoading}
          >
            <Text style={styles.buttonText}>{isLoading ? 'Loading...' : isCompleted && isTrackableCourse ? 'Completed' : 'Terminé'}</Text>
          </TouchableOpacity>
          
          <TouchableOpacity
            style={[styles.button, styles.notYetButton]}
            onPress={handleNotYet}
            disabled={isLoading}
          >
            <Text style={styles.buttonText}>Pas encore</Text>
          </TouchableOpacity>
        </View>
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  contentContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 10,
    paddingBottom: 80, // Add padding at the bottom equal to button container height
    position: 'relative', // Needed for absolute positioning of loading overlay
  },
  loadingOverlay: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(255, 255, 255, 0.7)',
      justifyContent: 'center',
      alignItems: 'center',
      zIndex: 1, // Ensure it's above content
  },
  mediaContent: {
    width: width - 20, // Example: take full width minus padding
    height: height * 0.7, // Example: take 70% of screen height
    // Add other styles as needed
  },
  pdfContent: {
      flex: 1, // PDF viewer usually needs flex: 1 to take available space
      width: width, // PDF viewer width
      height: height, // PDF viewer height
  },
  message: {
    flex: 1,
    textAlign: 'center',
    marginTop: 20,
    fontSize: 16,
    color: '#666',
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 20,
    backgroundColor: '#f0f0f0', // Added a light grey background
    borderTopWidth: 1,
    borderTopColor: '#eee',
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    // Define a fixed height if needed, e.g., height: 70,
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 30,
    borderRadius: 8,
    minWidth: 120,
    alignItems: 'center',
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
  },
  completedButton: {
    backgroundColor: '#4CAF50',
  },
  notYetButton: {
    backgroundColor: '#FFA726',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
}); 