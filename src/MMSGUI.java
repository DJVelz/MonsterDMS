import javax.swing.*;
import java.awt.*;

public class MMSGUI extends JFrame {
    private MonsterManager monsterManager;
    private DefaultListModel<String> monsterListModel;
    private JList<String> monsterJList;
    private JButton loadButton, removeButton, heaviestButton;
    private JTextArea monsterDetails;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton updateButton;

    public MMSGUI() {
        setTitle("Monster Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        monsterListModel = new DefaultListModel<>();
        monsterJList = new JList<>(monsterListModel);
        monsterJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        monsterJList.addListSelectionListener(e -> displayMonsterDetails());

        monsterDetails = new JTextArea(10, 30);
        monsterDetails.setEditable(false);

        loadButton = new JButton("Load Monsters");
        removeButton = new JButton("Remove Selected");
        heaviestButton = new JButton("Find Heaviest");
        addButton = new JButton("Add Monster");
        updateButton = new JButton("Update Monster");

        // Initialize MonsterManager
        monsterManager = new MonsterManager(monsterListModel);

        // Button actions
        loadButton.addActionListener(e -> loadMonsters());
        removeButton.addActionListener(e -> removeSelectedMonster());
        heaviestButton.addActionListener(e -> showHeaviestMonster());
        addButton.addActionListener(e -> addMonsterManually());
        updateButton.addActionListener(e -> updateMonsterDetails());

        // Layout setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(heaviestButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);

        JScrollPane listScrollPane = new JScrollPane(monsterJList);
        JScrollPane detailsScrollPane = new JScrollPane(monsterDetails);

        add(listScrollPane, BorderLayout.WEST);
        add(detailsScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);
    }

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

    private void loadMonsters() {
        String filePath = JOptionPane.showInputDialog("Enter file path:");
        if (filePath != null && !filePath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, monsterManager.loadFromFile(filePath));
        }
    }

    private void addMonsterManually() {
        // Collect monster details from the user
        String name = JOptionPane.showInputDialog("Enter Monster Name:");
        String wyvernType = JOptionPane.showInputDialog("Enter Wyvern Type (Flying, Fanged, Brute):");
        String healthStr = JOptionPane.showInputDialog("Enter Health (must be > 0):");
        String weakness = JOptionPane.showInputDialog("Enter Weakness (fire, water, thunder, ice, dragon):");
        String lowWeightStr = JOptionPane.showInputDialog("Enter Low Weight:");
        String highWeightStr = JOptionPane.showInputDialog("Enter High Weight:");

        // Validate inputs
        if (name == null || wyvernType == null || healthStr == null || weakness == null || lowWeightStr == null || highWeightStr == null) {
            JOptionPane.showMessageDialog(this, "Monster creation canceled.");
            return;
        }

        try {
            int health = Integer.parseInt(healthStr);
            double lowWeight = Double.parseDouble(lowWeightStr);
            double highWeight = Double.parseDouble(highWeightStr);

            // Call the MonsterManager method to add the monster
            String result = monsterManager.addMonster(name, wyvernType, health, weakness, lowWeight, highWeight);
            JOptionPane.showMessageDialog(this, result);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number input. Please try again.");
        }
    }

    private void removeSelectedMonster() {
        String selected = monsterJList.getSelectedValue();
        if (selected != null) {
            JOptionPane.showMessageDialog(this, monsterManager.removeMonster(selected));
            monsterListModel.removeElement(selected);  // Update the list visually
        }
    }

    private void updateMonsterDetails() {
        // Get the selected monster from the JList
        String selected = monsterJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a monster to update.");
            return;
        }

        Monster selectedMonster = monsterManager.getMonsterByName(selected);
        if (selectedMonster == null) {
            JOptionPane.showMessageDialog(this, "Monster not found.");
            return;
        }

        // Ask for the field to update (wyvernType, health, weakness, etc.)
        String field = JOptionPane.showInputDialog("Enter the field to update (name, wyverntype, health, weakness, lowweight, highweight):");
        if (field == null || field.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid field.");
            return;
        }

        // Ask for the new value for the selected field
        String newValue = JOptionPane.showInputDialog("Enter the new value for " + field + ":");
        if (newValue == null || newValue.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid value.");
            return;
        }

        // Call the updateMonster method from MonsterManager with the selected monster
        String result = monsterManager.updateMonster(selectedMonster, field, newValue);
        JOptionPane.showMessageDialog(this, result);

    }


    private void showHeaviestMonster() {
        JOptionPane.showMessageDialog(this, monsterManager.findHeaviestMonster());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MMSGUI::new);
    }
}
