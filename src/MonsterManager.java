/*
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * Manages the CRUD operations that will be used in the application
 */

import javax.swing.*;
import java.io.*;
import java.util.*;

class MonsterManager {
    private List<Monster> monsters = new ArrayList<>();
    private DefaultListModel<String> monsterListModel;

    public MonsterManager(DefaultListModel<String> model) {
        this.monsterListModel = model;
    }

    // Load monsters from a file and update the UI
    public String loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return "Error: File not found.";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String name = data[0].trim();
                    String wyvernType = data[1].trim();
                    int health = Integer.parseInt(data[2].trim());
                    String weakness = data[3].trim();
                    double lowWeight = Double.parseDouble(data[4].trim());
                    double highWeight = Double.parseDouble(data[5].trim());

                    if (addMonster(name, wyvernType, health, weakness, lowWeight, highWeight).contains("successfully")) {
                        continue;
                    }
                }
            }
            return "Monsters loaded successfully!";
        } catch (IOException | NumberFormatException e) {
            return "Error loading file: Invalid data format.";
        }
    }

    // Add a new monster and update the list model
    public String addMonster(String name, String wyvernType, int health, String weakness, double lowWeight, double highWeight) {
        if (!isUniqueName(name)) return "Error: Name must be unique.";
        if (!isValidWyvernType(wyvernType)) return "Error: Invalid wyvern type.";
        if (health <= 0) return "Error: Health must be greater than 0.";
        if (!isValidWeakness(weakness)) return "Error: Invalid weakness.";
        if (lowWeight < 0 || highWeight < lowWeight) return "Error: Invalid weight values.";

        monsters.add(new Monster(name, wyvernType, health, weakness, lowWeight, highWeight));
        monsterListModel.addElement(name); // Update the UI list
        return "Monster added successfully!";
    }

    // Update a monster's attribute
    public String updateMonster(Monster monster, String field, String newValue) {
        try {
            switch (field.toLowerCase()) {
                case "name":
                    if (newValue == null || newValue.trim().isEmpty()) return "Error: Name cannot be empty.";
                    if (!isUniqueName(newValue)) return "Error: A monster with that name already exists.";
                    monster.setName(newValue);  // Update the name
                    break;
                case "wyverntype":
                    if (!isValidWyvernType(newValue)) return "Error: Invalid wyvern type.";
                    monster.setWyvernType(newValue);
                    break;
                case "health":
                    int health = Integer.parseInt(newValue);
                    if (health <= 0) return "Error: Health must be greater than 0.";
                    monster.setHealth(health);
                    break;
                case "weakness":
                    if (!isValidWeakness(newValue)) return "Error: Invalid weakness.";
                    monster.setWeakness(newValue);
                    break;
                case "lowweight":
                    double lowWeight = Double.parseDouble(newValue);
                    if (lowWeight < 0 || lowWeight > monster.getHighWeight()) return "Error: Invalid low weight.";
                    monster.setLowWeight(lowWeight);
                    break;
                case "highweight":
                    double highWeight = Double.parseDouble(newValue);
                    if (highWeight < monster.getLowWeight()) return "Error: High weight must be >= low weight.";
                    monster.setHighWeight(highWeight);
                    break;
                default:
                    return "Error: Invalid field.";
            }
            return "Monster updated successfully!";
        } catch (NumberFormatException e) {
            return "Error: Invalid number format.";
        }
    }

    // Remove a monster and update UI
    public String removeMonster(String name) {
        boolean removed = monsters.removeIf(m -> m.getName().equalsIgnoreCase(name));
        if (removed) {
            monsterListModel.removeElement(name);
            return "Monster removed!";
        } else {
            return "Error: Monster not found.";
        }
    }

    // Find the heaviest monster
    public String findHeaviestMonster() {
        if (monsters.isEmpty()) return "No monsters available.";
        Monster heaviest = Collections.max(monsters, Comparator.comparingDouble(Monster::getHighWeight));
        return "Heaviest Monster: " + heaviest.getName() + " - Weight: " + heaviest.getHighWeight();
    }

    // Return a monster by name
    public Monster getMonsterByName(String name) {
        for (Monster m : monsters) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    // Validation helper methods
    private boolean isUniqueName(String name) {
        return monsters.stream().noneMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    private boolean isValidWyvernType(String type) {
        return type.equalsIgnoreCase("Flying") || type.equalsIgnoreCase("Fanged") || type.equalsIgnoreCase("Brute");
    }

    private boolean isValidWeakness(String weakness) {
        return Arrays.asList("fire", "water", "thunder", "ice", "dragon").contains(weakness.toLowerCase());
    }
}