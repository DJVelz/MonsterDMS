/*
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * Tests the CRUD operations and the heaviest monster methods
 *

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MonsterManagerTest {
    private MonsterManager manager;

    @BeforeEach
    void setUp() {
        manager = new MonsterManager();
        manager.addMonster("Nargacuga", "Fanged", -500, "water", 80.0, 120.0);
        manager.addMonster("Tigrex", "Brute", 2700, "ice", 200.0, 250.0);
    }

    @Test
    @DisplayName("Add Monster")
    void testAddValidMonster() {
        assertEquals("Monster added successfully!",
                manager.addMonster("Rathalos", "Flying", 3000, "fire", 100.5, 150.0));
    }

    @Test
    @DisplayName("Remove monster")
    void testRemoveExistingMonster() {
        manager.addMonster("Zinogre", "Fanged", 2500, "thunder", 90.0, 130.0);
        assertEquals("Monster removed!",
                manager.removeMonster("Zinogre"), "Error: Monster not found.");

    }

    @Test
    @DisplayName("Fail remove monster")
    void testRemoveNonExistingMonster() {
        assertEquals("Monster removed!",
                manager.removeMonster("Kirin"), "Error: Monster not found.");
    }

    @Test
    @DisplayName("Update monster")
    void testUpdateMonsterValid() {
        assertEquals("Monster updated successfully!",
                manager.updateMonster("Tigrex", "health", "3000"));
    }

    @Test
    @DisplayName("Fail update monster")
    void testUpdateMonsterInvalid() {
        assertEquals("Monster updated successfully!",
                manager.updateMonster("Tigrex", "color", "blue"),"Error: Invalid field.");
    }

    @Test
    @DisplayName("Find heaviest monster")
    void testFindHeaviestMonster() {
        manager.addMonster("Rathalos", "Flying", 3000, "fire", 100.5, 150.0);
        manager.addMonster("Brachydios", "Brute", 2800, "water", 150.0, 200.0);
        assertEquals("Heaviest Monster: Brachydios - Weight: 200.0",
                manager.findHeaviestMonster(), "No monsters available.");
    }

    @Test
    @DisplayName("Loading a text file")
    void testLoadValidFile() {
        assertEquals("Monsters loaded successfully!",
                manager.loadFromFile("C:/Users/derec/Downloads/20monsters.txt"), "Error: File not found.");
    }

    @Test
    @DisplayName("Loading invalid file")
    void testLoadInvalidFile() {
        assertEquals("Monsters loaded successfully!",
                manager.loadFromFile("non_existent_file.txt"),"Error: File not found.");
    }
}
*/