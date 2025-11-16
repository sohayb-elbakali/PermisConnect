````markdown
# PermisConnect Frontend

Cross-platform mobile and web application for the PermisConnect driving school platform, built with Expo and React Native. Provides an intuitive interface for students and instructors to manage courses, tests, reservations, and communications.

## Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [Available Scripts](#available-scripts)
- [Features](#features)
- [Configuration](#configuration)
- [Development](#development)
- [Testing](#testing)
- [Building for Production](#building-for-production)

## Overview

The PermisConnect frontend is a modern, cross-platform application that runs on:
- 📱 **iOS** devices (iPhone, iPad)
- 🤖 **Android** devices (phones, tablets)
- 🌐 **Web** browsers

Built with Expo and React Native, it provides a seamless user experience across all platforms with a single codebase.

## Technology Stack

- **Framework**: Expo ~53.0 / React Native 0.79
- **Language**: TypeScript 5.8
- **UI Library**: React 19.0
- **Navigation**: Expo Router 5.0 / React Navigation 7
- **HTTP Client**: Axios 1.9
- **State Management**: React Context API
- **Storage**: AsyncStorage
- **Media**: Expo Image, React Native Video
- **PDF Viewer**: React Native PDF, React PDF
- **UI Components**: React Native Elements, Vector Icons
- **Animations**: React Native Reanimated 3.17
- **Testing**: Jest 29, React Testing Library
- **Linting**: ESLint 9

## Project Structure

```
frontend/
├── src/
│   ├── app/                      # Expo Router screens
│   │   ├── _layout.tsx           # Root layout
│   │   ├── index.tsx             # Landing page
│   │   ├── login.tsx             # Login screen
│   │   ├── register.tsx          # Registration screen
│   │   ├── home.tsx              # Home dashboard
│   │   ├── courses.tsx           # Courses list
│   │   ├── course-viewer.tsx    # Course content viewer
│   │   ├── test-blanc-viewer.tsx # Practice test viewer
│   │   ├── calendar.tsx          # Lesson calendar
│   │   ├── chat.tsx              # Chat interface
│   │   ├── payment.tsx           # Payment processing
│   │   ├── profile.tsx           # User profile
│   │   └── settings.tsx          # App settings
│   ├── components/               # Reusable UI components
│   │   ├── Button.tsx
│   │   ├── TextField.tsx
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   ├── NotificationBar.tsx
│   │   ├── ProgressCircle.tsx
│   │   └── __tests__/            # Component tests
│   ├── screens/                  # Screen components
│   │   ├── AutoEcoleSelectionScreen.tsx
│   │   ├── AutoEcoleDetailsScreen.tsx
│   │   ├── CoursesScreen.tsx
│   │   ├── CourseViewerScreen.tsx
│   │   ├── TestBlancViewerScreen.tsx
│   │   ├── CalendarScreen.tsx
│   │   ├── ChatScreen.tsx
│   │   ├── PaymentScreen.tsx
│   │   └── ProfileScreen.tsx
│   ├── services/                 # API services
│   │   ├── api.ts                # Axios instance
│   │   ├── authService.ts        # Authentication
│   │   ├── autoEcoleService.ts   # Auto-école API
│   │   ├── courseService.ts      # Courses API
│   │   ├── testService.ts        # Tests API
│   │   ├── reservationService.ts # Reservations API
│   │   └── paymentService.ts     # Payments API
│   ├── contexts/                 # React contexts
│   │   └── AuthContext.tsx       # Auth state management
│   ├── hooks/                    # Custom React hooks
│   │   └── useAuth.ts            # Auth hook
│   ├── navigation/               # Navigation configuration
│   │   └── index.tsx
│   ├── constants/                # App constants
│   │   └── Colors.ts
│   ├── utils/                    # Utility functions
│   │   └── validation.tsx
│   ├── assets/                   # Static assets
│   │   ├── fonts/
│   │   └── images/
│   ├── config.ts                 # App configuration
│   └── scripts/
│       └── reset-project.js
├── app.json                      # Expo configuration
├── babel.config.js               # Babel configuration
├── tsconfig.json                 # TypeScript configuration
├── eslint.config.js              # ESLint configuration
├── jest.config.js                # Jest configuration
├── package.json
├── Dockerfile
└── README.md
```

## Getting Started

### Prerequisites

- **Node.js**: 18+ or 20+ (LTS recommended)
- **npm** or **yarn**
- **Expo CLI**: Installed globally or use npx
- **Expo Go** app: For testing on physical devices (iOS/Android)
- **Xcode**: For iOS development (macOS only)
- **Android Studio**: For Android development

### Installation

1. **Install dependencies:**
   ```bash
   npm install
   ```

   Or with yarn:
   ```bash
   yarn install
   ```

2. **Configure the backend API URL** in `src/config.ts`:
   ```typescript
   export const API_BASE_URL = 'http://localhost:8080/api';
   ```

## Running the Application

### Development Mode

1. **Start the Expo development server:**
   ```bash
   npm start
   ```

   This will open Expo DevTools in your browser at `http://localhost:8081`.

2. **Run on different platforms:**

   **iOS Simulator** (macOS only):
   ```bash
   npm run ios
   ```

   **Android Emulator**:
   ```bash
   npm run android
   ```

   **Web Browser**:
   ```bash
   npm run web
   ```

   **Physical Device**:
   - Install Expo Go from App Store (iOS) or Play Store (Android)
   - Scan the QR code displayed in the terminal or browser

### Docker Mode

Run with Docker Compose (from project root):
```bash
docker-compose up frontend
```

Access Expo DevTools at [http://localhost:8081](http://localhost:8081).

## Available Scripts

```bash
# Start development server
npm start

# Start with cache cleared
npm start -- --clear

# Run on iOS simulator
npm run ios

# Run on Android emulator
npm run android

# Run on web browser
npm run web

# Run linter
npm run lint

# Run tests
npm test

# Run tests in watch mode
npm test -- --watch

# Reset project (clean state)
npm run reset-project
```

## Features

### For Students
- 📝 **User Registration & Login**: Secure authentication
- 🏫 **Auto-École Selection**: Choose and view driving school details
- 📚 **Course Library**: Access theory courses and materials
- 🎥 **Course Viewer**: Watch video courses with multi-format support
- 📄 **PDF Viewer**: Read course documents
- 🎯 **Practice Tests**: Take tests blancs with instant feedback
- 📊 **Progress Tracking**: Monitor learning progress with visual analytics
- 📅 **Lesson Calendar**: View and book driving lessons
- 💬 **Chat**: Communicate with instructors in real-time
- 💳 **Payment Processing**: Secure payment integration
- 🔔 **Notifications**: Receive important updates
- 👤 **Profile Management**: Update personal information
- ⚙️ **Settings**: Customize app preferences

### For Instructors
- 📋 **Student Management**: View student information and progress
- 📆 **Schedule Management**: Manage lesson schedules
- 💬 **Communication**: Chat with students
- 📈 **Analytics**: Track student performance

## Configuration

### API Configuration (`src/config.ts`)

```typescript
export const config = {
  API_BASE_URL: process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080/api',
  STRIPE_PUBLIC_KEY: process.env.EXPO_PUBLIC_STRIPE_KEY || '',
  APP_NAME: 'PermisConnect',
  TIMEOUT: 10000,
};
```

### Environment Variables

Create a `.env` file (optional):
```env
EXPO_PUBLIC_API_URL=http://localhost:8080/api
EXPO_PUBLIC_STRIPE_KEY=pk_test_...
```

### Expo Configuration (`app.json`)

```json
{
  "expo": {
    "name": "PermisConnect",
    "slug": "permisconnect",
    "version": "1.0.0",
    "orientation": "portrait",
    "icon": "./assets/images/icon.png",
    "splash": {
      "image": "./assets/images/splash.png",
      "resizeMode": "contain",
      "backgroundColor": "#ffffff"
    },
    "ios": {
      "supportsTablet": true,
      "bundleIdentifier": "com.permisconnect.app"
    },
    "android": {
      "adaptiveIcon": {
        "foregroundImage": "./assets/images/adaptive-icon.png",
        "backgroundColor": "#ffffff"
      },
      "package": "com.permisconnect.app"
    },
    "web": {
      "favicon": "./assets/images/favicon.png"
    }
  }
}
```

## Development

### Adding a New Screen

1. Create screen file in `src/screens/`:
   ```typescript
   // src/screens/NewScreen.tsx
   import React from 'react';
   import { View, Text } from 'react-native';

   export default function NewScreen() {
     return (
       <View>
         <Text>New Screen</Text>
       </View>
     );
   }
   ```

2. Create route in `src/app/`:
   ```typescript
   // src/app/new-screen.tsx
   import NewScreen from '../screens/NewScreen';
   export default NewScreen;
   ```

### Adding a New API Service

```typescript
// src/services/newService.ts
import api from './api';

export const newService = {
  async getData() {
    const response = await api.get('/endpoint');
    return response.data;
  },
  
  async postData(data: any) {
    const response = await api.post('/endpoint', data);
    return response.data;
  },
};
```

### Using Authentication Context

```typescript
import { useAuth } from '../hooks/useAuth';

function MyComponent() {
  const { user, login, logout, isAuthenticated } = useAuth();
  
  // Use auth state and methods
}
```

## Testing

### Run All Tests
```bash
npm test
```

### Run Tests in Watch Mode
```bash
npm test -- --watch
```

### Run Tests with Coverage
```bash
npm test -- --coverage
```

### Test Structure
- **Unit Tests**: `src/components/__tests__/`
- **Integration Tests**: Test complete user flows
- **Snapshot Tests**: Test component rendering

### Example Test
```typescript
import { render, screen } from '@testing-library/react-native';
import Button from '../Button';

describe('Button', () => {
  it('renders correctly', () => {
    render(<Button title="Click Me" />);
    expect(screen.getByText('Click Me')).toBeTruthy();
  });
});
```

## Building for Production

### iOS Build (macOS only)
```bash
# Build for App Store
eas build --platform ios

# Local build
expo build:ios
```

### Android Build
```bash
# Build for Play Store
eas build --platform android

# Local build (APK)
expo build:android -t apk

# Local build (AAB)
expo build:android -t app-bundle
```

### Web Build
```bash
# Build for web deployment
npx expo export:web

# Output in `dist/` directory
```

## Debugging

### Using React Native Debugger
1. Install [React Native Debugger](https://github.com/jhen0409/react-native-debugger)
2. Enable Debug Mode in Expo DevTools
3. Open React Native Debugger

### Using Expo DevTools
- Shake device or press `Cmd+D` (iOS) / `Cmd+M` (Android)
- Select "Debug Remote JS"
- View logs in browser console

### Common Issues

**Metro Bundler Cache Issues**:
```bash
npm start -- --clear
```

**Module Not Found**:
```bash
rm -rf node_modules
npm install
```

**iOS Pod Installation Issues**:
```bash
cd ios
pod install
cd ..
```

## Code Style

### TypeScript
- Use TypeScript for type safety
- Define interfaces for data structures
- Use proper typing for components and functions

### ESLint
Run linter:
```bash
npm run lint
```

Auto-fix issues:
```bash
npm run lint -- --fix
```

### Formatting
- Use consistent indentation (2 spaces)
- Follow React/React Native best practices
- Keep components small and focused

## Performance Optimization

- Use `React.memo` for expensive components
- Implement virtualization for long lists (`FlatList`)
- Optimize images with proper sizing
- Use `useMemo` and `useCallback` hooks
- Lazy load screens and components

## Resources

- [Expo Documentation](https://docs.expo.dev/)
- [React Native Documentation](https://reactnative.dev/)
- [React Navigation](https://reactnavigation.org/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

## Contributing

1. Follow the project code style
2. Write tests for new features
3. Update documentation
4. Use meaningful commit messages
5. Test on multiple platforms before submitting

---

For more information, see the [main project README](../README.md) or contact the development team.

````
