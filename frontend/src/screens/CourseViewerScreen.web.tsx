import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  Image,
  Dimensions,
  Linking // Import Linking for web to open PDF in new tab
} from 'react-native';
import Header from '../components/Header';
import Video from 'react-native-video'; // Import Video (assuming react-native-video has web support or a web-compatible peer dependency)

const { width, height } = Dimensions.get('window');

interface CourseViewerScreenProps {
  cloudinaryUrl: string;
  fileType: string;
}

export default function CourseViewerScreen({ cloudinaryUrl, fileType }: CourseViewerScreenProps) {
  console.log('CourseViewerScreen (Web) received:', { cloudinaryUrl, fileType }); // Add log

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
        // Note: react-native-video's web support might require specific setup or a different library
        return (
          <Video
            source={{ uri: cloudinaryUrl }}
            style={styles.mediaContent}
            controls={true}
            resizeMode="contain"
          />
        );
      case 'raw': // Handle raw files (like PDFs) on web
        // On web, we can provide a link to open the PDF in a new tab
        return (
          <View>
            <Text style={styles.unsupportedText}>Preview not available for this file type on the web.</Text>
            <Text style={styles.downloadLink} onPress={() => Linking.openURL(cloudinaryUrl)}>Click here to view the document</Text>
          </View>
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
  unsupportedText: {
    fontSize: 16,
    color: '#666',
    marginBottom: 10,
  },
  downloadLink: {
    fontSize: 16,
    color: '#007BFF', // Link color
    textDecorationLine: 'underline',
  },
}); 