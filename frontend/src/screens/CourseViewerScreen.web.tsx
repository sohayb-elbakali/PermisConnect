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

// Helper to convert Google Drive view links to direct download links
function getDirectDriveLink(url: string): string {
  const match = url.match(/\/file\/d\/([^/]+)\//);
  if (match && match[1]) {
    return `https://drive.google.com/uc?export=download&id=${match[1]}`;
  }
  return url;
}

// Helper to check if a link is a Google Drive preview/view link
function isGoogleDriveViewLink(url: string): boolean {
  return /drive\.google\.com\/file\/d\/.+\/(view|preview)/.test(url);
}

// Helper to check if a Cloudinary URL is a PDF image (first page as image)
function isCloudinaryPdfImage(url: string, fileType: string): boolean {
  // If fileType is 'raw' but the URL ends with .png or .jpg, treat as image
  return (
    fileType === 'raw' &&
    (url.endsWith('.png') || url.endsWith('.jpg') || url.endsWith('.jpeg'))
  );
}

// Helper to get the first page of a Cloudinary PDF as an image
function getCloudinaryPdfImage(url: string): string {
  if (url.includes('/image/upload/') && url.endsWith('.pdf')) {
    // Insert transformation for first page (pg_1) and convert to jpg
    return url.replace('/upload/', '/upload/pg_1/').replace('.pdf', '.pdf.jpg');
  }
  return url;
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

    // If PDF was uploaded as image, treat as image using Cloudinary transformation
    if (fileType === 'raw' && cloudinaryUrl.endsWith('.pdf')) {
      const imageUrl = getCloudinaryPdfImage(cloudinaryUrl);
      return (
        <Image
          source={{ uri: imageUrl }}
          style={styles.mediaContent}
          resizeMode="contain"
        />
      );
    }

    // If PDF was uploaded as image, treat as image
    if (isCloudinaryPdfImage(cloudinaryUrl, fileType)) {
      return (
        <Image
          source={{ uri: cloudinaryUrl }}
          style={styles.mediaContent}
          resizeMode="contain"
        />
      );
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
        // If it's a Google Drive view/preview link, use an iframe
        if (isGoogleDriveViewLink(cloudinaryUrl)) {
          // Ensure it ends with /preview for best embedding
          let embedUrl = cloudinaryUrl.replace(/\/view(\?.*)?$/, '/preview');
          return (
            <iframe
              src={embedUrl}
              width="100%"
              height="480"
              allow="autoplay"
              style={{ border: 'none' }}
              title="Google Drive Video"
            ></iframe>
          );
        }
        // Otherwise, use ReactPlayer
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
            >
              <Page pageNumber={pageNumber} renderTextLayer={false} renderAnnotationLayer={false} />
            </Document>
            {numPages && (
              <View style={styles.pagination}>
                <Text>Page {pageNumber} of {numPages}</Text>
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