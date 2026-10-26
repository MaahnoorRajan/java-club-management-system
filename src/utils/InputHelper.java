package utils;

import java.util.Scanner;

/**
 * Utility Class: InputHelper
 *
 * Provides helper methods for reading and validating console input.
 * Encapsulation: scanner is private and shared across calls.
 * Exception Handling: wraps NumberFormatException in user-friendly messages.
 */
public class InputHelper {

    // Encapsulation: private shared scanner instance
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a non-empty string from the user.
     * Loops until valid input is provided.
     * @param prompt prompt message displayed to user.
     * @return valid non-empty string.
     */
    public static String readNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  [!] Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads a positive integer from the user.
     * Exception Handling: catches NumberFormatException for invalid input.
     * @param prompt prompt message.
     * @return valid positive integer.
     */
    public static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value > 0) return value;
                System.out.println("  [!] Value must be positive (> 0). Try again.");
            } catch (NumberFormatException e) {
                // Exception Handling: input was not a valid integer
                System.out.println("  [!] Invalid number. Please enter a positive integer.");
            }
        }
    }

    /**
     * Reads an integer within a specific range (for menu selection).
     * @param prompt   prompt message.
     * @param min      minimum value (inclusive).
     * @param max      maximum value (inclusive).
     * @return valid integer in [min, max].
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) return value;
                System.out.println("  [!] Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number. Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

    /**
     * Reads a string (can be empty, returns empty string if user just presses Enter).
     * Used for optional update fields.
     * @param prompt prompt message.
     * @return user input (possibly empty).
     */
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts user to confirm an action (y/n).
     * @param message confirmation message.
     * @return true if user entered 'y' or 'yes'.
     */
    public static boolean confirm(String message) {
        System.out.print(message + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * Pauses and waits for the user to press Enter.
     */
    public static void pressEnterToContinue() {
        System.out.print("\n  Press [Enter] to continue...");
        scanner.nextLine();
    }
}
