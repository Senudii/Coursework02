package ui;

import dao.CategoryDao;
import Entity.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class ManagecategoriesUI extends JFrame {
    private CategoryDao categoryDao;
    private JTable categoriesTable;
    private DefaultTableModel tableModel;
    private Connection connection; // Added connection as a class variable

    public ManagecategoriesUI(Connection connection) {
        this.connection = connection; // Initialize the class variable
        this.categoryDao = new CategoryDao(connection);

        setTitle("Manage Categories");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set desired size for the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

private void initUI() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(245, 222, 179)); // Set background color to Wheat


    tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Actions"}, 0);
    categoriesTable = new JTable(tableModel);
    categoriesTable.setRowHeight(30);
     categoriesTable.getTableHeader().setBackground(new Color(209, 190, 168));
        categoriesTable.getTableHeader().setForeground(Color.BLACK); // Set header text color
        categoriesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Set header font
         categoriesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(new Color(227, 218, 201));
                return component;
            }
        });
    categoriesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
    categoriesTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

    loadCategories();

    JScrollPane scrollPane = new JScrollPane(categoriesTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     buttonPanel.setOpaque(false);
    JButton addCategoryButton = new JButton("Add Category");
    addCategoryButton.setBackground(new Color(188, 152, 126)); // Set button color to Pale Taupe
        addCategoryButton.setOpaque(true);
        addCategoryButton.setBorderPainted(false);
    addCategoryButton.addActionListener(e -> openAddCategoryUI());

   
     JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(188, 152, 126)); // Set button color to Pale Taupe
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            new DashboardUI().setVisible(true);
            this.dispose(); // Close the current window
        });
buttonPanel.add(backButton);
    buttonPanel.add(addCategoryButton);
     // Add the back button to the button panel
    panel.add(buttonPanel, BorderLayout.SOUTH);

    add(panel);
}


    private void loadCategories() {
        try {
            List<Category> categories = categoryDao.getAllCategories();
            for (Category category : categories) {
                Object[] rowData = new Object[]{
                    category.getId(),
                    category.getName(),
                    "Actions" // Placeholder for the actions
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddCategoryUI() {
        String categoryName = JOptionPane.showInputDialog(this, "Enter category name:");
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            try {
                categoryDao.addCategory(categoryName.trim());
                reloadCategories();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage());
            }
        }
    }

    private void openUpdateCategoryUI(Category category) {
        String newName = JOptionPane.showInputDialog(this, "Enter new name for category:", category.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            try {
                categoryDao.updateCategory(category.getId(), newName.trim());
                reloadCategories();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage());
            }
        }
    }

    private void deleteCategory(Category category) {
        try {
            categoryDao.deleteCategory(category.getId());
            reloadCategories();
            JOptionPane.showMessageDialog(this, "Category deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage());
        }
    }

    private void reloadCategories() {
        tableModel.setRowCount(0); // Clear existing rows
        loadCategories();
    }

    // ButtonRenderer class
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton updateButton;
        private JButton deleteButton;

        public ButtonRenderer() {
           setLayout(new FlowLayout());
            setBackground(new Color(227, 218, 201));

            updateButton = new JButton("Update");
            updateButton.setBackground(Color.GREEN); // Set color for update button
            updateButton.setOpaque(true);
            updateButton.setBorderPainted(false);

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED); // Set color for delete button
            deleteButton.setOpaque(true);
            deleteButton.setBorderPainted(false);

            add(updateButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // ButtonEditor class
    class ButtonEditor extends DefaultCellEditor {
        private JButton updateButton;
        private JButton deleteButton;
        private JPanel panel;
        private Category currentCategory;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout());
            panel.setBackground(new Color(227, 218, 201));

            updateButton = new JButton("Update");
            updateButton.setBackground(Color.GREEN); // Set color for update button
            updateButton.setOpaque(true);
            updateButton.setBorderPainted(false);

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED); // Set color for delete button
            deleteButton.setOpaque(true);
            deleteButton.setBorderPainted(false);

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openUpdateCategoryUI(currentCategory);
                    reloadCategories(); // Refresh the table after update
                    fireEditingStopped();
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Stop the cell editor before performing the delete action
                    fireEditingStopped();
                    deleteCategory(currentCategory);
                    reloadCategories(); // Refresh the table after deletion
                }
            });

            panel.add(updateButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            try {
                List<Category> categories = categoryDao.getAllCategories();
                currentCategory = categories.get(row);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentCategory;
        }
    }

    public static void main(String[] args) {
        // Assume you have a method to create a connection
        Connection connection = createDatabaseConnection();
        SwingUtilities.invokeLater(() -> new ManagecategoriesUI(connection).setVisible(true));
    }

    // Method to create database connection (you need to implement this)
    private static Connection createDatabaseConnection() {
        // Your database connection code here
        return null; // Placeholder return
    }
}
