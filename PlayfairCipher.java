import java.util.Scanner;
import java.util.Arrays;

public class PlayfairCipher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter the key (only uppercase letters):");
        String key = scanner.nextLine();
        
        System.out.println("Enter the plaintext (only uppercase letters):");
        String plaintext = scanner.nextLine();
        
        System.out.println("Key: " + key);
        System.out.println("Plaintext: " + plaintext);
        
        String[][] keyMatrix = generateKeyMatrix(key);
        System.out.println("Key Matrix:");
        printMatrix(keyMatrix);
        
        String ciphertext = encrypt(plaintext, keyMatrix);
        if (!ciphertext.isEmpty()) {
            System.out.println("Ciphertext: " + ciphertext);
            
            String decryptedText = decrypt(ciphertext, keyMatrix);
            System.out.println("Decrypted Text: " + decryptedText);
        }
        
        scanner.close();
    }
    
    // Method to generate key matrix
    public static String[][] generateKeyMatrix(String key) {
        // Remove duplicate characters from the key
        key = key.replaceAll("J", "I").toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder keyBuilder = new StringBuilder();
        // Add unique characters from the key
        for (char ch : key.toCharArray()) {
            if (keyBuilder.indexOf(String.valueOf(ch)) == -1) {
                keyBuilder.append(ch);
            }
        }
        // Add remaining unique characters from the alphabet
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch == 'J') continue;
            if (keyBuilder.indexOf(String.valueOf(ch)) == -1) {
                keyBuilder.append(ch);
            }
        }
        
        String keyString = keyBuilder.toString();
        String[][] keyMatrix = new String[5][5];
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                keyMatrix[i][j] = String.valueOf(keyString.charAt(index));
                index++;
            }
        }
        return keyMatrix;
    }
    
    // Method to print key matrix
    public static void printMatrix(String[][] matrix) {
        for (String[] row : matrix) {
            System.out.println(String.join(", ", row));
        }
    }
    
    // Method to find row of a character in key matrix
    public static int getRow(String[][] matrix, char ch) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].charAt(0) == ch) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    // Method to find column of a character in key matrix
    public static int getColumn(String[][] matrix, char ch) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].charAt(0) == ch) {
                    return j;
                }
            }
        }
        return -1;
    }
    
    // Method to encrypt plaintext
    public static String encrypt(String plaintext, String[][] keyMatrix) {
        StringBuilder ciphertext = new StringBuilder();
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "").replaceAll("J", "I");
        plaintext = plaintext.replaceAll(" ", ""); // Ignore spaces
        int length = plaintext.length();
        for (int i = 0; i < length; i += 2) {
            char ch1 = plaintext.charAt(i);
            char ch2 = (i + 1 < length) ? plaintext.charAt(i + 1) : 'X';
            if (ch1 == ch2) {
                ch2 = getPaddingChar(ch1);
                i--; // Repeat current character with padding character
            }
            int row1 = getRow(keyMatrix, ch1);
            int col1 = getColumn(keyMatrix, ch1);
            int row2 = getRow(keyMatrix, ch2);
            int col2 = getColumn(keyMatrix, ch2);
            if (row1 == -1 || row2 == -1 || col1 == -1 || col2 == -1) {
                System.err.println("Error: Characters not found in key matrix.");
                return "";
            }
            if (row1 == row2) { // Same row
                ciphertext.append(keyMatrix[row1][(col1 + 1) % 5]);
                ciphertext.append(keyMatrix[row2][(col2 + 1) % 5]);
            } else if (col1 == col2) { // Same column
                ciphertext.append(keyMatrix[(row1 + 1) % 5][col1]);
                ciphertext.append(keyMatrix[(row2 + 1) % 5][col2]);
            } else { // Different row and column
                ciphertext.append(keyMatrix[row1][col2]);
                ciphertext.append(keyMatrix[row2][col1]);
            }
        }
        return ciphertext.toString();
    }
    
    // Method to decrypt ciphertext
    public static String decrypt(String ciphertext, String[][] keyMatrix) {
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char ch1 = ciphertext.charAt(i);
            char ch2 = ciphertext.charAt(i + 1);
            int row1 = getRow(keyMatrix, ch1);
            int col1 = getColumn(keyMatrix, ch1);
            int row2 = getRow(keyMatrix, ch2);
            int col2 = getColumn(keyMatrix, ch2);
            if (row1 == -1 || row2 == -1 || col1 == -1 || col2 == -1) {
                System.err.println("Error: Characters not found in key matrix.");
                return "";
            }
            if (row1 == row2) { // Same row
                plaintext.append(keyMatrix[row1][(col1 + 4) % 5]);
                plaintext.append(keyMatrix[row2][(col2 + 4) % 5]);
            } else if (col1 == col2) { // Same column
                plaintext.append(keyMatrix[(row1 + 4) % 5][col1]);
                plaintext.append(keyMatrix[(row2 + 4) % 5][col2]);
            } else { // Different row and column
                plaintext.append(keyMatrix[row1][col2]);
                plaintext.append(keyMatrix[row2][col1]);
            }
        }
        return plaintext.toString();
    }
    
    // Method to get padding character
    public static char getPaddingChar(char ch) {
        if (ch == 'X') return 'Q';
        else if (ch == 'Q') return 'Z';
        else return 'X';
    }
}
