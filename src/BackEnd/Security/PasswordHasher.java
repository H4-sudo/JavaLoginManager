package BackEnd.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    public static String[] hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] salt = generateSalt();
            messageDigest.update(salt);
            byte[] hashedPassword = messageDigest.digest(password.getBytes());

            String saltString = Base64.getEncoder().encodeToString(salt);
            String hashedPasswordString = bytesToHex(hashedPassword);
            return new String[]{saltString, hashedPasswordString};

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validatePassword(String enteredPassword, String storedPassword, String storedSalt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] salt = Base64.getDecoder().decode(storedSalt);
            messageDigest.update(salt);
            byte[] hashedPassword = messageDigest.digest(enteredPassword.getBytes());
            String hashedPasswordString = bytesToHex(hashedPassword);
            return hashedPasswordString.equals(storedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
