/**
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * MonsterManager.java
 * Manages the CRUD operations that will be used in the application
 */

import java.sql.*;
import java.util.*;

public class MonsterManager {
    private Connection connection;

    /**
     * The constructor establishes a connection with the SQLite database
     * @param dbPath The file path for the database
     */
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

    /**
     * The addMonster method inserts a new monster into the database
     * @param name The name of a monster
     * @param wyvernType Determines if the monster is a Flying, Fanged, or Brute type
     * @param health The amount of health a monster has
     * @param weakness What element the monster is weakest to
     * @param lowWeight The lowest weight encountered
     * @param highWeight The highest weight encountered
     * @return Returns a message letting the user know if their monster was successfully or unsuccessfully
     * added to the database
     */
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

    /**
     * The updateMonster method updates one attribute from a selected monster
     * @param name The name of a monster being selected
     * @param field Which attribute you want to update (name, wyvernType, health, weakness, lowWeight, or highWeight)
     * @param newValue The new value for the selected attribute
     * @return Returns a message for either a successful change or what error didn't allow the update
     */
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

    /**
     * The removeMonster deletes a monster from the database using their name
     * @param name The name of a monster being selected
     * @return Returns a message for success or error of removing the monster
     */
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

    /**
     * getAllMonsters is used to find all monsters in the database to keep the list refreshed
     * @return All monsters in the database
     */
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

    /**
     * getMonsterByName helps find monsters using their names
     * @param name The name of a monster being selected
     * @return Returns the attributes for the monster
     */
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
        return null;
    }

    /**
     * findHeaviestMonster searches for the heaviest monster in the database
     * @return Returns the name and weight of the heaviest monster in the database if available
     */
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
