import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    // Multiple options for validation
    public String getStringInput(String prompt, String... options) {
        String input;
        while (true) { //continuous loop until valid input is received
            System.out.print(prompt);
            input = scanner.nextLine().toLowerCase();

            boolean valid = false;
            for (String option : options) {
                if (input.equals(option)) {
                    valid = true;
                    break;
                }
            }
            if (valid) {
                return input;
            } else {
                System.out.println("Invalid input. Please enter one of the following: " + String.join(", ", options) + ".");
            }
        }
    }

    // String Validation with no arguments
    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    //  Single character validation
    public String getSymbolInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Invalid input. Please enter exactly one character.");
            }
        } while (input.length() != 1);
        return input;
    }

    // Integer input validation  with a min max range
    public int getIntInput(String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Input must be between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
