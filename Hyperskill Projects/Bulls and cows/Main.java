package bullscows;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input the length of the secret code:");
        int n;
        try {
            n = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a valid integer.");
            return;
        }

        if (n <= 0) {
            System.out.println("Error: Length of the secret code must be greater than 0.");
            return;
        }

        System.out.println("Input the number of possible symbols in the code:");
        int m;
        try {
            m = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a valid integer.");
            return;
        }



        if (n > m) {
            System.out.println("Error: It's not possible to generate a code with a length of " + n + " with " + m + " unique symbols.");
            return;
        } else if (m > 36) {
            System.out.println("Error: Maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        }

        String secretCode = generateSecretCode(n, m);

        if (secretCode != null) {
            char[] secretCodeArray = secretCode.toCharArray();
            int bulls = 0, cows, turn = 1;

            while (bulls != secretCodeArray.length) {
                System.out.println("Turn " + turn + ":");
                String input = scanner.nextLine();

                // Validate the length of input
                if (input.length() != secretCodeArray.length) {
                    System.out.println("Error: Input length must be " + secretCodeArray.length + " characters.");
                    continue;
                }

                char[] inputArray = input.toCharArray();

                int[] secretFrequency = new int[36];
                int[] guessFrequency = new int[36];

                bulls = 0;

                // Count bulls and mark positions
                boolean[] isBull = new boolean[secretCodeArray.length];
                for (int i = 0; i < secretCodeArray.length; i++) {
                    if (inputArray[i] == secretCodeArray[i]) {
                        bulls++;
                        isBull[i] = true;
                    } else {
                        isBull[i] = false;
                    }
                }

                boolean invalidCharacter = false;

                // Count frequency of characters excluding bulls
                for (int i = 0; i < secretCodeArray.length; i++) {
                    if (!isBull[i]) {
                        int secretIdx = charToIndex(secretCodeArray[i]);
                        int guessIdx = charToIndex(inputArray[i]);

                        if (guessIdx < 0 || guessIdx >= m) {
                            System.out.println("Error: Invalid character '" + inputArray[i] + "'.");
                            invalidCharacter = true;
                            break;
                        }

                        secretFrequency[secretIdx]++;
                        guessFrequency[guessIdx]++;
                    }
                }

                if (invalidCharacter) {
                    continue;
                }

                // Calculate cows
                cows = 0;
                for (int i = 0; i < 36; i++) {
                    int commonOccurrences = Math.min(secretFrequency[i], guessFrequency[i]);
                    cows += commonOccurrences;
                }

                // display result based on bulls and cows
                if (bulls == 0 && cows == 0) {
                    System.out.println("Grade: None.");
                } else if (cows == 0) {
                    System.out.println("Grade: " + bulls + " bull(s).");
                } else if (bulls == 0) {
                    System.out.println("Grade: " + cows + " cow(s).");
                } else {
                    System.out.println("Grade: " + bulls + " bull(s) and " + cows + " cow(s).");
                }
                turn++;
            }
            System.out.println("Congratulations! You guessed the secret code.");
        }
    }

    public static int charToIndex(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        } else if (ch >= 'a' && ch <= 'z') {
            return ch - 'a' + 10;
        } else {
            return -1; // invalid character
        }
    }

    public static String generateSecretCode(int length, int numOfSymbols) {
        StringBuilder secretCodeBuilder = new StringBuilder();
        List<Character> characters = new ArrayList<>();
        String set = "0123456789abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < numOfSymbols; i++) {
            characters.add(set.charAt(i));
        }

        Collections.shuffle(characters);

        for (int i = 0; i < length; i++) {
            secretCodeBuilder.append(characters.get(i));
        }

        String charsUsed;
        if (numOfSymbols <= 10) {
            charsUsed = "0-" + set.charAt(numOfSymbols - 1);
        } else {
            charsUsed = "0-9, a-" + set.charAt(numOfSymbols - 1);
        }

        System.out.println("The secret is prepared: " + "*".repeat(length) + " (" + charsUsed + ").");
        System.out.println("Okay, let's start a game!");
        return secretCodeBuilder.toString();
    }

}
