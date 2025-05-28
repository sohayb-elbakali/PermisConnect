module.exports = {
  preset: "jest-expo",
  setupFilesAfterEnv: ["@testing-library/jest-native/extend-expect"],
  transformIgnorePatterns: [
    "node_modules/(?!(jest-)?react-native|@react-native|@react-navigation|@react-native-community|expo(nent)?|@expo(nent)?/.*|@expo-google-fonts/.*|react-clone-referenced-element|@unimodules|unimodules|sentry-expo|native-base|@sentry/.*)",
  ],
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json", "node"],
  testPathIgnorePatterns: ["/node_modules/", "/dist/"],
};
