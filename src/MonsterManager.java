/*
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * Manages the CRUD operations that will be used in the application
 */

import java.sql.*;
import java.util.*;

public class MonsterManager {
    private Connection connection;

    //Constructor
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

    /* addMonster
     * Input: Monster attributes
     * Output: String
     * This method collects monster attributes to manually create a monster
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

    /* updateMonster
     * Input: Attribute to update
     * Output: String
     * Allows the user to update a given monster's attributes
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

    /* removeMonster
     * Input: Monster name
     * Output: String
     * Deletes a given monster
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

    /* getMonsterByName
     * Input: None
     * Output: Monster
     * Finds a monster by name to help other methods select a specific monster
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

    /* findHeaviestMonster
     * Input: None
     * Output: String
     * Finds and displays the heaviest monster's name and weight
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
