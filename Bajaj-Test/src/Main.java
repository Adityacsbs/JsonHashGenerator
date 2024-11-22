import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        // Check if the file path is provided as an argument
        if (args.length < 1) {
            System.out.println("Usage: java ReadFileFromCommandLine <file-path>");
            return;
        }

        String roleNumber = args[0];
        String filePath = args[1]; // First argument is the file path
        String destinationValue = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the entire file into a single string
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line.trim());
            }

            String jsonString = jsonBuilder.toString();

            // Find the value of the "destination" key manually
            String key = "\"destination\"";


            int keyIndex = jsonString.indexOf(key);
            if (keyIndex != -1) {
                int startIndex = jsonString.indexOf(":", keyIndex) + 1;
                int endIndex = jsonString.indexOf(",", startIndex); // Handles keys followed by commas
                if (endIndex == -1) { // Handles the last key in the JSON object
                    endIndex = jsonString.indexOf("}", startIndex);
                }

                 destinationValue = jsonString.substring(startIndex, endIndex).trim();

                // Remove quotes from the value, if present
                if (destinationValue.startsWith("\"") && destinationValue.endsWith("\"")) {
                    destinationValue = destinationValue.substring(1, destinationValue.length() - 1);
                }

                System.out.println("Destination: " + destinationValue);
            } else {
                System.out.println("\"destination\" key not found in the JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        String randomString = generateRandomString(8);

        String result = roleNumber+destinationValue+randomString;

        String finalResult = hashString(result);

        System.out.println(finalResult);

        finalResult = finalResult+";"+randomString;

        System.out.println(finalResult);


    }

    // Method to generate a random alphanumeric string
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    // Method to hash a string using SHA-256
    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert hash bytes to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Hashing algorithm not available.", e);
        }
    }
}