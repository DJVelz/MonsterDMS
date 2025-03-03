/*
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * Main class - Displays a menu that allows the user to manage a Monster Hunter inspired DMS
 * The user can input monsters manually or from a file, remove specific monsters, update their attributes,
 * and find the heaviest monster in their database.
 */

import java.util.Scanner;

public class MMS {
    public static void main(String[] args) {
        MonsterManager manager = new MonsterManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMonster Management System");
            System.out.println("1. Load Monsters from File");
            System.out.println("2. Display All Monsters");
            System.out.println("3. Add a Monster");
            System.out.println("4. Update a Monster");
            System.out.println("5. Remove a Monster");
            System.out.println("6. Find Heaviest Monster");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine();
                    System.out.println(manager.loadFromFile(filePath));
                }
                case 2 -> System.out.println(manager.displayMonsters());
                case 3 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter wyvern type (Flying, Fanged, Brute): ");
                    String wyvernType = scanner.nextLine();
                    System.out.print("Enter health: ");
                    int health = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter weakness (fire, water, thunder, ice, dragon): ");
                    String weakness = scanner.nextLine();
                    System.out.print("Enter low weight: ");
                    double lowWeight = scanner.nextDouble();
                    System.out.print("Enter high weight: ");
                    double highWeight = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline

                    System.out.println(manager.addMonster(name, wyvernType, health, weakness, lowWeight, highWeight));
                }
                case 4 -> {
                    System.out.print("Enter monster name to update: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter field to update (wyvernType, health, weakness, lowWeight, highWeight): ");
                    String field = scanner.nextLine();
                    System.out.print("Enter new value: ");
                    String newValue = scanner.nextLine();
                    System.out.println(manager.updateMonster(name, field, newValue));
                }
                case 5 -> {
                    System.out.print("Enter monster name to remove: ");
                    String name = scanner.nextLine();
                    System.out.println(manager.removeMonster(name));
                }
                case 6 -> System.out.println(manager.findHeaviestMonster());
                case 7 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice, please enter a number between 1 and 7.");
            }
        }
    }
}
