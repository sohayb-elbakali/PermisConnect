# PermisConnect Frontend

This is the frontend of the PermisConnect application, built with Expo (React Native). It provides the user interface for students and instructors to interact with the driving school platform.

## Features

- User authentication and registration
- Course and lesson management
- Progress tracking
- Notifications and more

## Getting Started (Development)

1. **Install dependencies:**

   ```sh
   npm install
   ```

2. **Start the Expo development server:**

   ```sh
   npm start
   ```

   or

   ```sh
   npx expo start
   ```

3. **Open the app:**
   - Use the Expo Go app on your phone to scan the QR code, or
   - Run on an emulator/simulator.

## Running with Docker

1. **Build and start with Docker Compose (from project root):**

   ```sh
   docker-compose up --build
   ```

2. **Access the Expo DevTools:**
   - Visit [http://localhost:8081](http://localhost:8081) in your browser.
   - Scan the QR code with Expo Go to open the app on your device.

## Project Structure

- `src/` - App source code
- `Dockerfile` - For containerizing the frontend

## Environment Variables

- Make sure to configure API URLs to point to the backend (see `.env` or config files as needed).

---
For more details, see the main project README or contact the maintainers.
