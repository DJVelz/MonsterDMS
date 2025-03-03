import java.io.*;
import java.util.*;

class MonsterManager {
    private List<Monster> monsters = new ArrayList<>();

    // Load monsters from a text file
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
                    monsters.add(new Monster(name, wyvernType, health, weakness, lowWeight, highWeight));
                }
            }
            return "Monsters loaded successfully!";
        } catch (IOException | NumberFormatException e) {
            return "Error loading file: Invalid data format.";
        }
    }

    // Display all monsters
    public String displayMonsters() {
        if (monsters.isEmpty()) return "No monsters found.";
        StringBuilder sb = new StringBuilder();
        for (Monster m : monsters) sb.append(m).append("\n");
        return sb.toString();
    }

    // Add a monster
    public String addMonster(String name, String wyvernType, int health, String weakness, double lowWeight, double highWeight) {
        if (!isUniqueName(name)) return "Error: Name must be unique.";
        if (!isValidWyvernType(wyvernType)) return "Error: Invalid wyvern type.";
        if (health <= 0) return "Error: Health must be greater than 0.";
        if (!isValidWeakness(weakness)) return "Error: Invalid weakness.";
        if (lowWeight < 0 || highWeight < lowWeight) return "Error: Invalid weight values.";

        monsters.add(new Monster(name, wyvernType, health, weakness, lowWeight, highWeight));
        return "Monster added successfully!";
    }

    // Update a monster's attribute
    public String updateMonster(String name, String field, String newValue) {
        for (Monster m : monsters) {
            if (m.getName().equalsIgnoreCase(name)) {
                try {
                    switch (field.toLowerCase()) {
                        case "wyverntype":
                            if (!isValidWyvernType(newValue)) return "Error: Invalid wyvern type.";
                            m.setWyvernType(newValue);
                            break;
                        case "health":
                            int health = Integer.parseInt(newValue);
                            if (health <= 0) return "Error: Health must be greater than 0.";
                            m.setHealth(health);
                            break;
                        case "weakness":
                            if (!isValidWeakness(newValue)) return "Error: Invalid weakness.";
                            m.setWeakness(newValue);
                            break;
                        case "lowweight":
                            double lowWeight = Double.parseDouble(newValue);
                            if (lowWeight < 0 || lowWeight > m.getHighWeight()) return "Error: Invalid low weight.";
                            m.setLowWeight(lowWeight);
                            break;
                        case "highweight":
                            double highWeight = Double.parseDouble(newValue);
                            if (highWeight < m.getLowWeight()) return "Error: High weight must be >= low weight.";
                            m.setHighWeight(highWeight);
                            break;
                        default:
                            return "Error: Invalid field.";
                    }
                    return "Monster updated successfully!";
                } catch (NumberFormatException e) {
                    return "Error: Invalid number format.";
                }
            }
        }
        return "Error: Monster not found.";
    }

    // Remove a monster
    public String removeMonster(String name) {
        return monsters.removeIf(m -> m.getName().equalsIgnoreCase(name)) ? "Monster removed!" : "Error: Monster not found.";
    }

    // Find the heaviest monster
    public String findHeaviestMonster() {
        if (monsters.isEmpty()) return "No monsters available.";
        Monster heaviest = Collections.max(monsters, Comparator.comparingDouble(Monster::getHighWeight));
        return "Heaviest Monster: " + heaviest.getName() + " - Weight: " + heaviest.getHighWeight();
    }

    // Validation Helpers
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
