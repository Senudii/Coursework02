package ui;

import dao.MemberDao;
import Entity.Member;

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

public class ManagemembersUI extends JFrame {
    private MemberDao memberDao;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public ManagemembersUI(Connection connection) {
        this.connection = connection;
        this.memberDao = new MemberDao(connection);

        setTitle("Manage Members");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
         panel.setBackground(new Color(245, 222, 179)); 

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Actions"}, 0);
        membersTable = new JTable(tableModel);
        membersTable.setRowHeight(30);
        membersTable.getTableHeader().setBackground(new Color(209, 190, 168));
        membersTable.getTableHeader().setForeground(Color.BLACK); // Set header text color
        membersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Set header font
         membersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(new Color(227, 218, 201));
                return component;
            }
        });
        membersTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        membersTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        loadMembers();

        JScrollPane scrollPane = new JScrollPane(membersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
         buttonPanel.setOpaque(false);
        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.setBackground(new Color(188, 152, 126)); // Set button color to Pale Taupe
        addMemberButton.setOpaque(true);
        addMemberButton.setBorderPainted(false);
        addMemberButton.addActionListener(e -> openAddMemberUI());
 JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(188, 152, 126)); // Set button color to Pale Taupe
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            new DashboardUI().setVisible(true);
            this.dispose(); // Close the current window
        });
buttonPanel.add(backButton);
    buttonPanel.add(addMemberButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadMembers() {
        try {
            List<Member> members = memberDao.getAllMembers();
            for (Member member : members) {
                Object[] rowData = new Object[]{
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    "Actions"
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddMemberUI() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        Object[] message = {
            "Name:", nameField,
            "Email:", emailField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Enter member details", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            if (!name.isEmpty() && !email.isEmpty()) {
                try {
                    memberDao.addMember(name, email);
                    reloadMembers();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error adding member: " + e.getMessage());
                }
            }
        }
    }

    private void openUpdateMemberUI(Member member) {
        JTextField nameField = new JTextField(member.getName());
        JTextField emailField = new JTextField(member.getEmail());
        Object[] message = {
            "Name:", nameField,
            "Email:", emailField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Update member details", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();
            if (!newName.isEmpty() && !newEmail.isEmpty()) {
                try {
                    memberDao.updateMember(member.getId(), newName, newEmail);
                    reloadMembers();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating member: " + e.getMessage());
                }
            }
        }
    }

    private void deleteMember(Member member) {
        try {
            memberDao.deleteMember(member.getId());
            reloadMembers();
            JOptionPane.showMessageDialog(this, "Member deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting member: " + e.getMessage());
        }
    }

    private void reloadMembers() {
        tableModel.setRowCount(0);
        loadMembers();
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton updateButton;
        private JButton deleteButton;

        public ButtonRenderer() {
           setLayout(new FlowLayout());
            setBackground(new Color(227, 218, 201));
            updateButton = new JButton("Update");
            updateButton.setBackground(Color.GREEN);
            updateButton.setOpaque(true);
            updateButton.setBorderPainted(false);

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED);
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

    class ButtonEditor extends DefaultCellEditor {
        private JButton updateButton;
        private JButton deleteButton;
        private JPanel panel;
        private Member currentMember;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout());
            panel.setBackground(new Color(227, 218, 201));

            updateButton = new JButton("Update");
            updateButton.setBackground(Color.GREEN);
            updateButton.setOpaque(true);
            updateButton.setBorderPainted(false);

            deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED);
            deleteButton.setOpaque(true);
            deleteButton.setBorderPainted(false);

            updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openUpdateMemberUI(currentMember);
                    reloadMembers();
                    fireEditingStopped();
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    deleteMember(currentMember);
                    reloadMembers();
                }
            });

            panel.add(updateButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            try {
                List<Member> members = memberDao.getAllMembers();
                currentMember = members.get(row);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentMember;
        }
    }

    public static void main(String[] args) {
        // Assume you have a method to create a connection
        Connection connection = createDatabaseConnection();
        SwingUtilities.invokeLater(() -> new ManagemembersUI(connection).setVisible(true));
    }

    private static Connection createDatabaseConnection() {
        // Your database connection code here
        return null; // Placeholder return
    }
}
