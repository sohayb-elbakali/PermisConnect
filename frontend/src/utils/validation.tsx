export function validateEmail(email: string): boolean {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(String(email).toLowerCase());
}

export function validatePassword(password: string): boolean {
  return password.length >= 6; // Example: password must be at least 6 characters long
}

export function validateLogin(email: string, password: string): boolean {
  return validateEmail(email) && validatePassword(password);
}
