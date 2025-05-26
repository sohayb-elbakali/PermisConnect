import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  Image,
  Dimensions,
  Platform // Import Platform
} from 'react-native';
import Header from '../components/Header';
import Video from 'react-native-video'; // Import Video

// Conditionally import react-native-pdf only if not on web
let Pdf = null;
if (Platform.OS !== 'web') {
  try {
    Pdf = require('react-native-pdf').default; // Use require for conditional import
  } catch (e) {
    console.error("Failed to load react-native-pdf on non-web platform:", e);
  }
}

const { width, height } = Dimensions.get('window');

interface CourseViewerScreenProps {
  cloudinaryUrl: string;
  fileType: string;
}

export default function CourseViewerScreen({ cloudinaryUrl, fileType }: CourseViewerScreenProps) {
  const renderContent = () => {
    if (!cloudinaryUrl) {
      return <Text>No course material available.</Text>;
    }

    switch (fileType) {
      case 'image':
        return (
          <Image
            source={{ uri: cloudinaryUrl }}
            style={styles.mediaContent} // Define appropriate styles
            resizeMode="contain" // Adjust resizeMode as needed
          />
        );
      case 'video': // Handle video files
        return (
          <Video
            source={{ uri: cloudinaryUrl }} // The video file URL
            style={styles.mediaContent} // Apply similar styling or adjust as needed
            controls={true} // Show video controls
            resizeMode="contain" // Contain the video within the view
          />
        );
      case 'raw': // Handle raw files (like PDFs)
        if (Platform.OS === 'web') {
          return <Text>PDF viewing is not supported on the web.</Text>;
        } else if (Pdf) {
          // react-native-pdf requires a source object with uri
          const source = { uri: cloudinaryUrl, cache: true };
          return (
            <Pdf
              source={source}
              style={styles.pdfContent} // Define appropriate styles for PDF viewer
              onLoadComplete={(numberOfPages,filePath) => {
                console.log(`numberOfPages: ${numberOfPages}`);
              }}
              onPageChanged={(page,numberOfPages) => {
                console.log(`Current page: ${page}`);
              }}
              onError={(error) => {
                console.error('PDF error:', error);
              }}
              // Enable this if you need to scale the PDF to fit the screen width
              // scale={1.0} 
              // minScale={0.5} maxScale={2.0}
            />
          );
        } else {
             return <Text>PDF viewing component not available.</Text>;
        }
      default:
        return <Text>Preview not available for this file type ({fileType}).</Text>;
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      
      {/* You might want a dynamic header title */}
      <Header title="Course Material" /> 

      <View style={styles.contentContainer}>
        {renderContent()}
      </View>
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
  }
}); 