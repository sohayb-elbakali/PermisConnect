import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  StatusBar,
  Image,
  Dimensions,
  Linking
} from 'react-native';
import Header from '../components/Header';
// import Video from 'react-native-video'; // react-native-video is not ideal for web, will use react-player

// Import web-compatible libraries
import { Document, Page, pdfjs } from 'react-pdf';
import ReactPlayer from 'react-player';

// Configure react-pdf worker source
pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.min.js`;

const { width, height } = Dimensions.get('window');

interface CourseViewerScreenProps {
  cloudinaryUrl: string;
  fileType: string;
}

export default function CourseViewerScreen({ cloudinaryUrl, fileType }: CourseViewerScreenProps) {
  const [numPages, setNumPages] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);
  const [pdfError, setPdfError] = useState<string | null>(null); // State for PDF errors

  function onDocumentLoadSuccess({ numPages }: { numPages: number }) {
    setNumPages(numPages);
    setPageNumber(1); // Start with the first page
    setPdfError(null); // Clear any previous errors
  }

  function onDocumentLoadError(error: any) {
    console.error('react-pdf error:', error);
    setPdfError('Failed to load PDF document.');
  }

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
        // Use react-player for web video support
        return (
          <View style={styles.videoContainer}> {/* Container for aspect ratio */}
            <ReactPlayer
              url={cloudinaryUrl}
              controls={true}
              width='100%'
              height='100%'
              config={{
                file: { attributes: { controlsList: 'nodownload' } },
              }}
              // Add onError if needed
            />
          </View>
        );
      case 'raw': // Handle raw files (like PDFs) on web
        if (pdfError) {
          return <Text style={styles.errorText}>{pdfError}</Text>;
        }
        return (
          <View style={styles.pdfContainer}> {/* Container for PDF viewer */}
            <Document
              file={cloudinaryUrl}
              onLoadSuccess={onDocumentLoadSuccess}
              onLoadError={onDocumentLoadError}
              // Remove options that are not supported or needed for basic display
            >
              {/* Render a single page */}
              <Page pageNumber={pageNumber} renderTextLayer={false} renderAnnotationLayer={false} />
            </Document>
            {numPages && (
              <View style={styles.pagination}>
                <Text>Page {pageNumber} of {numPages}</Text>
                {/* Add pagination controls if desired */}
              </View>
            )}
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
    // Remove PDF specific styles from here
  },
  videoContainer: {
    width: '100%',
    aspectRatio: 16 / 9, // Common video aspect ratio
  },
  pdfContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'flex-start',
  },
  pagination: {
    marginTop: 10,
  },
  errorText: {
    color: 'red',
    fontSize: 16,
  },
}); 