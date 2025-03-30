/*
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * Manages the CRUD operations that will be used in the application
 */

import java.sql.*;
import java.util.*;

public class MonsterManager {
    private Connection connection;

    // Constructor
    public MonsterManager(String dbPath) {
        try {
            String url = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to establish connection.");
        }
    }

    // Add a Monster to the Database
    public String addMonster(String name, String wyvernType, int health, String weakness, double lowWeight, double highWeight) {
        String sql = "INSERT INTO monsters(name, wyvernType, health, weakness, lowWeight, highWeight) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, wyvernType);
            pstmt.setInt(3, health);
            pstmt.setString(4, weakness);
            pstmt.setDouble(5, lowWeight);
            pstmt.setDouble(6, highWeight);
            pstmt.executeUpdate();
            return "Monster added successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding monster: " + e.getMessage();
        }
    }

    // Remove a Monster from the Database
    public String removeMonster(String name) {
        String sql = "DELETE FROM monsters WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            return "Monster removed successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error removing monster: " + e.getMessage();
        }
    }

    // Update a Monster's Attribute
    public String updateMonster(String name, String field, String newValue) {
        String sql = "UPDATE monsters SET " + field + " = ? WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            return "Monster updated successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating monster: " + e.getMessage();
        }
    }

    // Get All Monsters from Database
    public List<Monster> getAllMonsters() {
        List<Monster> monsters = new ArrayList<>();
        String sql = "SELECT * FROM monsters";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                String wyvernType = rs.getString("wyvernType");
                int health = rs.getInt("health");
                String weakness = rs.getString("weakness");
                double lowWeight = rs.getDouble("lowWeight");
                double highWeight = rs.getDouble("highWeight");

                monsters.add(new Monster(name, wyvernType, health, weakness, lowWeight, highWeight));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monsters;
    }

    // Get Monster by Name from Database
    public Monster getMonsterByName(String name) {
        String sql = "SELECT * FROM monsters WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String wyvernType = rs.getString("wyvernType");
                    int health = rs.getInt("health");
                    String weakness = rs.getString("weakness");
                    double lowWeight = rs.getDouble("lowWeight");
                    double highWeight = rs.getDouble("highWeight");

                    return new Monster(name, wyvernType, health, weakness, lowWeight, highWeight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if monster is not found
    }

    // Get Heaviest Monster from Database
    public String findHeaviestMonster() {
        String sql = "SELECT name, highWeight FROM monsters ORDER BY highWeight DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String name = rs.getString("name");
                double weight = rs.getDouble("highWeight");
                return "The heaviest monster is " + name + " with a weight of " + weight + " kg.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No monsters found.";
    }
}

/*
    private List<Monster> monsters = new ArrayList<>();
    private DefaultListModel<String> monsterListModel;
    public List<Monster> getAllMonsters() {
        return monsters;
    }

    /* loadFromFile
     * Input: File path
     * Output: String
     * This method prompts the user for a file path, checks if the information can be used properly, and if everything
     * is correct adds a batch of monsters to our app
     *
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

    /* addMonster
     * Input: Monster attributes
     * Output: String
     * This method collects monster attributes to manually create a monster
     *
    public String addMonster(String name, String wyvernType, int health, String weakness, double lowWeight, double highWeight) {
        if (!isUniqueName(name)) return "Error: Name must be unique.";
        if (!isValidWyvernType(wyvernType)) return "Error: Invalid wyvern type.";
        if (health <= 0) return "Error: Health must be greater than 0.";
        if (!isValidWeakness(weakness)) return "Error: Invalid weakness.";
        if (lowWeight < 0 || highWeight < lowWeight) return "Error: Invalid weight values.";

        monsters.add(new Monster(name, wyvernType, health, weakness, lowWeight, highWeight));
        monsterListModel.addElement(name);
        return "Monster added successfully!";
    }

    /* updateMonster
     * Input: Attribute to update
     * Output: String
     * Allows the user to update a given monster's attributes
     *
    public String updateMonster(Monster monster, String field, String newValue) {
        try {
            switch (field.toLowerCase()) {
                case "name":
                    if (newValue == null || newValue.trim().isEmpty())
                        return "Error: Name cannot be empty.";
                    if (!newValue.equals(monster.getName()) && !isUniqueName(newValue))
                        return "Error: A monster with that name already exists.";
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

    /* removeMonster
     * Input: Monster name
     * Output: String
     * Deletes a given monster
     *
    public String removeMonster(String name) {
        boolean removed = monsters.removeIf(m -> m.getName().equalsIgnoreCase(name));
        if (removed) {
            monsterListModel.removeElement(name);
            return "Monster removed!";
        } else {
            return "Error: Monster not found.";
        }
    }

    /* findHeaviestMonster
     * Input: None
     * Output: String
     * Finds and displays the heaviest monster's name and weight
     *
    public String findHeaviestMonster() {
        if (monsters.isEmpty()) return "No monsters available.";
        Monster heaviest = Collections.max(monsters, Comparator.comparingDouble(Monster::getHighWeight));
        return "Heaviest Monster: " + heaviest.getName() + " - Weight: " + heaviest.getHighWeight();
    }

    /* getMonsterByName
     * Input: None
     * Output: Monster
     * Finds a monster by name to help other methods select a specific monster
     *
    public Monster getMonsterByName(String name) {
        for (Monster m : monsters) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    /* isUniqueName, isValidWyvernType, isValidWeakness
     * Helper methods to validate monster attributes
     *
    private boolean isUniqueName(String name) {
        return monsters.stream().noneMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    private boolean isValidWyvernType(String type) {
        return type.equalsIgnoreCase("Flying") || type.equalsIgnoreCase("Fanged") || type.equalsIgnoreCase("Brute");
    }

    private boolean isValidWeakness(String weakness) {
        return Arrays.asList("fire", "water", "thunder", "ice", "dragon").contains(weakness.toLowerCase());
    }
}*/