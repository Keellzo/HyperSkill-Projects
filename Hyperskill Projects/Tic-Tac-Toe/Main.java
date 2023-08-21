package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[] chars = "         ".toCharArray();

        printTable(chars);

        boolean xTurn = true;

        while (!checkGameEnd(chars)) {

            try {
                System.out.print("Enter the coordinates: ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();

                if (row < 1 || row > 3 || col < 1 || col > 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else {
                    int index = 3 * (row - 1) + (col - 1);
                    if (chars[index] != ' ') {
                        System.out.println("This cell is occupied! Choose another one!");
                    } else {
                        chars[index] = xTurn ? 'X' : 'O';
                        xTurn = !xTurn;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
                scanner.nextLine(); // Consume the invalid input
            }

            printTable(chars);
        }

        if (checkWin(chars, 'X')) {
            System.out.println("X wins");
        } else if (checkWin(chars, 'O')) {
            System.out.println("O wins");
        } else {
            System.out.println("Draw");
        }
    }

    private static void printTable(char[] chars) {
        System.out.println("---------");
        System.out.println("| " + chars[0] + " " + chars[1] + " " + chars[2] + " |");
        System.out.println("| " + chars[3] + " " + chars[4] + " " + chars[5] + " |");
        System.out.println("| " + chars[6] + " " + chars[7] + " " + chars[8] + " |");
        System.out.println("---------");
    }

    private static boolean checkWin(char[] chars, char player) {
        return (chars[0] == player && chars[1] == player && chars[2] == player) ||
                (chars[3] == player && chars[4] == player && chars[5] == player) ||
                (chars[6] == player && chars[7] == player && chars[8] == player) ||
                (chars[0] == player && chars[3] == player && chars[6] == player) ||
                (chars[1] == player && chars[4] == player && chars[7] == player) ||
                (chars[2] == player && chars[5] == player && chars[8] == player) ||
                (chars[0] == player && chars[4] == player && chars[8] == player) ||
                (chars[2] == player && chars[4] == player && chars[6] == player);
    }

    private static boolean checkGameEnd(char[] chars) {
        if (checkWin(chars, 'X')) {
            printTable(chars);
            System.out.println("X wins");
            return true;
        } else if (checkWin(chars, 'O')) {
            printTable(chars);
            System.out.println("O wins");
            return true;
        } else if (new String(chars).indexOf(' ') == -1) {
            printTable(chars);
            System.out.println("Draw");
            return true;
        }
        return false;
    }
}
