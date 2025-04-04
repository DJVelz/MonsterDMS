/**
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
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

    /**
     * displayMonsterDetails displays all the attributes from the selected monster on the GUI on
     * the right side of the screen after selecting that monster's name on the left
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

    /**
     * Uses the MonsterManager constructor to establish a connection to a SQLite database
     * through the use of a button
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

    /**
     * refreshMonsterList keeps the list of monsters on the left refreshed in case a monster is
     * added, updated, or removed
     */
    private void refreshMonsterList() {
        monsterListModel.clear();
        for (Monster monster : monsterManager.getAllMonsters()) {
            monsterListModel.addElement(monster.getName());
        }
    }

    /**
     * addMonsterManually gives the user pop-up prompts to add a monster using addMonster from
     * the MonsterManager class
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

    /**
     * removeSelected monster uses removeMonster from the MonsterManager class to delete a
     * selected monster from the database
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

    /**
     * showHeaviestMonster uses findHeaviestMonster to display the name and weight of the heaviest
     * monster to the user
     */
    private void showHeaviestMonster() {
        String result = monsterManager.findHeaviestMonster();
        JOptionPane.showMessageDialog(this, result);
    }

    /**
     * updateMonsterDetails prompts the user with what attribute they want to update via
     * updateMonster from MonsterManager
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

    /**
     *
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

    /**
     * The main method starts the application
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MMSGUI());
    }
}