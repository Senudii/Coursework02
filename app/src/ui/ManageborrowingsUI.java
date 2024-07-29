package ui;

import dao.BorrowingDao;
import Entity.Borrowing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ManageborrowingsUI extends JFrame {
    private BorrowingDao borrowingDao;
    private JTable borrowingsTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public ManageborrowingsUI(Connection connection) {
        this.connection = connection;
        this.borrowingDao = new BorrowingDao(connection);

        setTitle("Manage Borrowings");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 222, 179)); // Set background color to Wheat

        tableModel = new DefaultTableModel(new String[]{"Book ID", "Member ID", "Borrowed Date", "Due Date", "Returned Date", "Fine", "Actions"}, 0);
        borrowingsTable = new JTable(tableModel);
        borrowingsTable.setRowHeight(40); // Increase row height

        // Set table header color to Dark Vanilla
        borrowingsTable.getTableHeader().setBackground(new Color(209, 190, 168));
        borrowingsTable.getTableHeader().setForeground(Color.BLACK); // Set header text color
        borrowingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Set header font

        // Set table row background color to Bone
        borrowingsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(new Color(227, 218, 201));
                return component;
            }
        });

        borrowingsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        borrowingsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        loadBorrowings();

        JScrollPane scrollPane = new JScrollPane(borrowingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false); // Transparent background to blend with main panel

        JButton addBorrowingButton = new JButton("Add Borrowing");
        addBorrowingButton.setBackground(new Color(188, 152, 126)); // Set button color to Pale Taupe
        addBorrowingButton.setOpaque(true);
        addBorrowingButton.setBorderPainted(false);
        addBorrowingButton.addActionListener(e -> openAddBorrowingUI());

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(188, 152, 126)); // Set button color to Pale Taupe
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            new DashboardUI().setVisible(true);
            this.dispose(); // Close the current window
        });

        buttonPanel.add(backButton);
        buttonPanel.add(addBorrowingButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadBorrowings() {
        try {
            List<Borrowing> borrowings = borrowingDao.getAllBorrowings();
            for (Borrowing borrowing : borrowings) {
                Object[] rowData = new Object[]{
                        borrowing.getBookId(),
                        borrowing.getMemberId(),
                        borrowing.getBorrowedDate(),
                        borrowing.getDueDate(),
                        borrowing.getReturnedDate(),
                        borrowing.getFine(),
                        "Actions" // Placeholder for actions
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddBorrowingUI() {
        AddborrowingsUI addBorrowingUI = new AddborrowingsUI(this, connection);
        addBorrowingUI.setVisible(true);
        reloadBorrowings();
    }

    private void openUpdateBorrowingUI(Borrowing borrowing) {
        String returnedDateStr = JOptionPane.showInputDialog(this, "Enter returned date (yyyy-mm-dd):", borrowing.getReturnedDate() != null ? borrowing.getReturnedDate().toString() : "");
        String fineStr = JOptionPane.showInputDialog(this, "Enter fine amount:", borrowing.getFine());

        try {
            if (returnedDateStr != null && !returnedDateStr.isEmpty()) {
                borrowing.setReturnedDate(LocalDate.parse(returnedDateStr));
            } else {
                borrowing.setReturnedDate(null);
            }
            borrowing.setFine(Double.parseDouble(fineStr));
            borrowingDao.updateBorrowing(borrowing);
            reloadBorrowings();
            JOptionPane.showMessageDialog(this, "Borrowing updated successfully!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating borrowing: " + e.getMessage());
        }
    }

    private void deleteBorrowing(Borrowing borrowing) {
        try {
            borrowingDao.deleteBorrowing(borrowing.getId());
            reloadBorrowings();
            JOptionPane.showMessageDialog(this, "Borrowing deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting borrowing: " + e.getMessage());
        }
    }

    private void reloadBorrowings() {
        tableModel.setRowCount(0); // Clear existing rows
        loadBorrowings();
    }

    // ButtonRenderer class
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton updateButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout());
            setBackground(new Color(227, 218, 201)); // Set panel background to Bone

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
        private Borrowing currentBorrowing;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout());
            panel.setBackground(new Color(227, 218, 201)); // Set panel background to Bone

            updateButton = new JButton("Update");
            updateButton.setBackground(Color.GREEN); // Set color for update button
            updateButton.setOpaque(true);
            updateButton.setBorderPainted(false);

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED); // Set color for delete button
            deleteButton.setOpaque(true);
            deleteButton.setBorderPainted(false);

            updateButton.addActionListener(e -> {
                openUpdateBorrowingUI(currentBorrowing);
                reloadBorrowings(); // Refresh the table after update
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                deleteBorrowing(currentBorrowing);
                reloadBorrowings(); // Refresh the table after deletion
                fireEditingStopped();
            });

            panel.add(updateButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            try {
                List<Borrowing> borrowings = borrowingDao.getAllBorrowings();
                currentBorrowing = borrowings.get(row);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentBorrowing;
        }
    }
}
