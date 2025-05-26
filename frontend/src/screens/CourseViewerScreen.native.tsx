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

// Import react-native-pdf as it's for native platforms
import Pdf from 'react-native-pdf';

const { width, height } = Dimensions.get('window');

interface CourseViewerScreenProps {
  cloudinaryUrl: string;
  fileType: string;
}

// Helper to convert Google Drive view links to direct download links
function getDirectDriveLink(url: string): string {
  const match = url.match(/\/file\/d\/([^/]+)\//);
  if (match && match[1]) {
    return `https://drive.google.com/uc?export=download&id=${match[1]}`;
  }
  return url;
}

export default function CourseViewerScreen({ cloudinaryUrl, fileType }: CourseViewerScreenProps) {
  console.log('CourseViewerScreen (Native) received:', { cloudinaryUrl, fileType });

  const renderContent = () => {
    if (!cloudinaryUrl) {
      return <Text>No course material available.</Text>;
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
            source={{ uri: getDirectDriveLink(cloudinaryUrl) }}
            style={styles.mediaContent}
            controls={true}
            resizeMode="contain"
          />
        );
      case 'raw': // Handle raw files (like PDFs) on native
          // react-native-pdf requires a source object with uri
          const source = { uri: getDirectDriveLink(cloudinaryUrl), cache: true };
          return (
            <Pdf
              source={source}
              style={styles.pdfContent}
              onLoadComplete={(numberOfPages, filePath) => {
                console.log(`numberOfPages: ${numberOfPages}`);
              }}
              onPageChanged={(page, numberOfPages) => {
                console.log(`Current page: ${page}`);
              }}
              onError={(error) => {
                console.error('PDF error:', error);
              }}
            />
          );
      default:
        return <Text>Preview not available for this file type ({fileType}).</Text>;
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />

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
    width: width - 20,
    height: height * 0.7,
  },
  pdfContent: {
      flex: 1,
      width: width,
      height: height,
  }
}); 