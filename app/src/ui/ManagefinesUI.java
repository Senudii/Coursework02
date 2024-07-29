package ui;

import dao.FineDao;
import Entity.Borrowing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagefinesUI extends JFrame {
    private FineDao fineDao;
    private JTable finesTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    private List<Borrowing> updatedBorrowings;

    public ManagefinesUI(Connection connection) {
        this.connection = connection;
        this.fineDao = new FineDao(connection);
        this.updatedBorrowings = new ArrayList<>();

        setTitle("Manage Fines");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

   private void initUI() {
    JPanel panel = new JPanel(new BorderLayout());

    tableModel = new DefaultTableModel(new String[]{"ID", "Member ID", "Fine", "Payment Status"}, 0);
    finesTable = new JTable(tableModel);
    finesTable.setRowHeight(30);
    finesTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
    finesTable.getColumnModel().getColumn(3).setCellEditor(new StatusCellEditor());

    loadFines();

    JScrollPane scrollPane = new JScrollPane(finesTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    JButton updateButton = new JButton("Update");
    updateButton.addActionListener(e -> updateDatabase());
    buttonPanel.add(updateButton);

    JButton backButton = new JButton("Back");
    backButton.setBackground(new Color(188, 152, 126)); // Pale Taupe color
    backButton.setOpaque(true);
    backButton.setBorderPainted(false);
    backButton.addActionListener(e -> {
        new DashboardUI().setVisible(true);
        this.dispose(); // Close the current window
    });
    buttonPanel.add(backButton);

    panel.add(buttonPanel, BorderLayout.SOUTH);

    add(panel);
}

    private void loadFines() {
        try {
            List<Borrowing> borrowings = fineDao.getAllBorrowingsWithFines();
            for (Borrowing borrowing : borrowings) {
                Object[] rowData = new Object[]{
                    borrowing.getId(),
                    borrowing.getMemberId(),
                    borrowing.getFine(),
                    borrowing.getPaymentStatus()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDatabase() {
        boolean success = true;
        for (Borrowing borrowing : updatedBorrowings) {
            try {
                fineDao.updatePaymentStatus(borrowing.getId(), borrowing.getPaymentStatus());
            } catch (SQLException e) {
                success = false;
                e.printStackTrace();
            }
        }
        updatedBorrowings.clear();
        if (success) {
            JOptionPane.showMessageDialog(this, "Successfully updated!", "Update", JOptionPane.INFORMATION_MESSAGE);
            dispose();  // Close the UI after a successful update
        } else {
            JOptionPane.showMessageDialog(this, "Update failed!", "Update", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        loadFines();
    }

    class StatusCellRenderer extends JLabel implements TableCellRenderer {
        private JComboBox<String> comboBox;

        public StatusCellRenderer() {
            comboBox = new JComboBox<>(new String[]{"Not Completed", "Completed"});
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if ("Completed".equals(value)) {
                setText("Completed");
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            } else {
                comboBox.setSelectedItem(value);
                return comboBox;
            }
        }
    }

    class StatusCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JComboBox<String> comboBox;
        private int currentRow;

        public StatusCellEditor() {
            comboBox = new JComboBox<>(new String[]{"Not Completed", "Completed"});
            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedStatus = (String) comboBox.getSelectedItem();
                    tableModel.setValueAt(selectedStatus, currentRow, 3);
                    int borrowingId = (int) tableModel.getValueAt(currentRow, 0);
                    Borrowing borrowing = new Borrowing();
                    borrowing.setId(borrowingId);
                    borrowing.setPaymentStatus(selectedStatus);
                    updatedBorrowings.add(borrowing);
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return comboBox.getSelectedItem();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            if ("Completed".equals(value)) {
                return new JLabel("Completed");
            } else {
                comboBox.setSelectedItem(value);
                return comboBox;
            }
        }
    }

    public static void main(String[] args) {
        Connection connection = createDatabaseConnection();
        SwingUtilities.invokeLater(() -> new ManagefinesUI(connection).setVisible(true));
    }

    private static Connection createDatabaseConnection() {
        // Your database connection code here
        return null; // Placeholder return
    }
}
