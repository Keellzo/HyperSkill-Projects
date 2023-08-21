package budget;

import java.util.*;

public class Main {

    private static double balance = 0.0;
    private static Map<String, List<String>> categorizedPurchases = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] categories = {"Food", "Clothes", "Entertainment", "Other"};

        // Initialize categories in the map
        for (String category : categories) {
            categorizedPurchases.put(category, new ArrayList<>());
        }

        while (true) {
            System.out.println("Choose your action:");
            System.out.println("1) Add income");
            System.out.println("2) Add purchase");
            System.out.println("3) Show list of purchases");
            System.out.println("4) Balance");
            System.out.println("0) Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("\nEnter income:");
                    balance += Double.parseDouble(scanner.nextLine());
                    System.out.println("Income was added!\n");
                    break;
                case 2:
                    while (true) {
                        try {
                            System.out.println("\nChoose the type of purchase");
                            for (int i = 0; i < categories.length; i++) {
                                System.out.println((i + 1) + ") " + categories[i]);
                            }
                            System.out.println("5) Back");
                            categoryChoice = Integer.parseInt(scanner.nextLine());

                            if (categoryChoice >= 1 && categoryChoice <= 5) {
                                break; // exit the loop if a valid choice is made
                            } else {
                                System.out.println("Invalid choice. Please choose between 1 and 5.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                case 3:
                    System.out.println("\nChoose the type of purchases");
                    for (int i = 0; i < categories.length; i++) {
                        System.out.println((i + 1) + ") " + categories[i]);
                    }
                    System.out.println("5) All");
                    System.out.println("6) Back");
                    int showChoice = Integer.parseInt(scanner.nextLine());

                    if (showChoice == 6) {
                        break;
                    }

                    if (showChoice == 5) {
                        double total = 0;
                        for (String cat : categories) {
                            System.out.println(cat + ":");
                            for (String purchase : categorizedPurchases.get(cat)) {
                                System.out.println(purchase);
                                total += Double.parseDouble(purchase.split(" ")[purchase.split(" ").length - 1].substring(1));
                            }
                        }
                        System.out.println("Total sum: $" + String.format("%.2f", total) + "\n");
                    } else {
                        String chosenCategory = categories[showChoice - 1];
                        System.out.println(chosenCategory + ":");
                        if (categorizedPurchases.get(chosenCategory).isEmpty()) {
                            System.out.println("The purchase list is empty\n");
                        } else {
                            double total = 0;
                            for (String purchase : categorizedPurchases.get(chosenCategory)) {
                                System.out.println(purchase);
                                total += Double.parseDouble(purchase.split(" ")[purchase.split(" ").length - 1].substring(1));
                            }
                            System.out.println("Total sum: $" + String.format("%.2f", total) + "\n");
                        }
                    }
                    break;
                case 4:
                    System.out.println("\nBalance: $" + String.format("%.2f", balance) + "\n");
                    break;
                case 0:
                    System.out.println("\nBye!");
                    return;
            }
        }
    }
}
