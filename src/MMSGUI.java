/*
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * March 12, 2025
 * MMSGUI.java
 * This application will let the user manager their Monster Hunter journal in multiple ways, including:
 * loading multiple monsters from a text file, manually adding a monster, deleting a monster, updating a monster's
 * attributes, and find which monster is the heaviest. It will return a list of monsters and their attributes
 * when selected.
 */

import javax.swing.*;

public class MMSGUI extends JFrame {
    private MonsterManager monsterManager;
    private DefaultListModel<String> monsterListModel;
    private JList<String> monsterJList;
    private JButton loadButton;
    private JButton removeButton;
    private JButton heaviestButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton exitButton;
    private JTextArea monsterDetails;
    private JPanel buttonPanel;
    private JScrollPane monsterInfo;

    public MMSGUI() {
        setTitle("Monster Management System");
        setContentPane(buttonPanel);
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        monsterListModel = new DefaultListModel<>();
        monsterJList.setModel(monsterListModel);
        monsterJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displayMonsterDetails();
            }
        });

        //Button actions
        loadButton.addActionListener(e -> loadMonsters());
        removeButton.addActionListener(e -> removeSelectedMonster());
        heaviestButton.addActionListener(e -> showHeaviestMonster());
        addButton.addActionListener(e -> addMonsterManually());
        updateButton.addActionListener(e -> updateMonsterDetails());
        exitButton.addActionListener(e -> exitApplication());
    }

    /* displayMonsterDetails
     * Input: Selected monster
     * Output: Full monster attributes
     * Displays selected monster's attributes on the right side of the screen
     */
    private void displayMonsterDetails() {
        String selected = monsterJList.getSelectedValue();
        if (selected != null) {
            Monster monster = monsterManager.getMonsterByName(selected);
            if (monster != null) {
                monsterDetails.setText(monster.toString());
            } else {
                monsterDetails.setText("Monster details not found.");
            }
        }
    }

    /* loadMonsters
     * Input: SQLite file path
     * Output: Adding valid monsters to the list
     * Prompts user for a text file path to add a batch of monsters
     */
    private void loadMonsters() {
        String dbPath = JOptionPane.showInputDialog("Enter the path to the SQLite database:");
        if (dbPath != null && !dbPath.trim().isEmpty()) {
            monsterManager = new MonsterManager(dbPath.trim());
            refreshMonsterList();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid database path.");
        }
    }

    /* refreshMonsterList
     * Input: None
     * Output: Monster list
     * Keeps the list up to date on the GUI's end
     */
    private void refreshMonsterList() {
        monsterListModel.clear();
        for (Monster monster : monsterManager.getAllMonsters()) {
            monsterListModel.addElement(monster.getName());
        }
    }

    /* addMonsterManually
     * Input: Monster attributes
     * Output: Monster added
     * Prompts user for each monster attribute to create their own journal entry, validates their inputs,
     * and uses the information to add the monsters to the app
     */
    private void addMonsterManually() {
        String name = JOptionPane.showInputDialog("Enter Monster Name:");
        String wyvernType = JOptionPane.showInputDialog("Enter Wyvern Type (Flying, Fanged, Brute):");
        String healthStr = JOptionPane.showInputDialog("Enter Health (must be > 0):");
        String weakness = JOptionPane.showInputDialog("Enter Weakness (fire, water, thunder, ice, dragon):");
        String lowWeightStr = JOptionPane.showInputDialog("Enter Low Weight:");
        String highWeightStr = JOptionPane.showInputDialog("Enter High Weight:");

        if (name != null && wyvernType != null && healthStr != null && weakness != null && lowWeightStr != null && highWeightStr != null) {
            try {
                int health = Integer.parseInt(healthStr);
                double lowWeight = Double.parseDouble(lowWeightStr);
                double highWeight = Double.parseDouble(highWeightStr);
                String result = monsterManager.addMonster(name, wyvernType, health, weakness, lowWeight, highWeight);
                JOptionPane.showMessageDialog(this, result);
                refreshMonsterList();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    /* removeSelectedMonster
     * Input: Selected monster
     * Output: Monster removed
     * This method lets you delete a monster from the app by selecting it and then clicking the remove button
     */
    private void removeSelectedMonster() {
        String selectedMonster = monsterJList.getSelectedValue();
        if (selectedMonster != null) {
            String result = monsterManager.removeMonster(selectedMonster);
            JOptionPane.showMessageDialog(this, result);
            refreshMonsterList();
        } else {
            JOptionPane.showMessageDialog(this, "No monster selected.");
        }
    }

    /* showHeaviestMonster
     * Input: Button click
     * Output: Message
     * Clicking the button will display the heaviest monster's name and weight
     */
    private void showHeaviestMonster() {
        String result = monsterManager.findHeaviestMonster();
        JOptionPane.showMessageDialog(this, result);
    }

    /* updateMonsterDetails
     * Input: An attribute to update
     * Output: New attribute
     * This method lets you update individual attributes for a selected monster. It also refreshes the monster list
     * upon successfully updating an attribute
     */
    private void updateMonsterDetails() {
        String selectedMonster = monsterJList.getSelectedValue();
        if (selectedMonster != null) {
            String field = JOptionPane.showInputDialog("Enter the field to update (name, wyvernType, health, weakness, lowWeight, highWeight):");
            String newValue = JOptionPane.showInputDialog("Enter the new value:");
            if (field != null && newValue != null) {
                String result = monsterManager.updateMonster(selectedMonster, field, newValue);
                JOptionPane.showMessageDialog(this, result);
                refreshMonsterList();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No monster selected.");
        }
    }

    /* exitApplication
     * Input: Button press
     * Output: Option pane
     * Clicking the exit button asks the user if they want to exit, if yes then the app closes, if not then they
     * can continue
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MMSGUI());
    }
}