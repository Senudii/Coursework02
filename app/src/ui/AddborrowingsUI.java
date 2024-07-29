package ui;

import dao.BorrowingDao;
import Entity.Borrowing;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddborrowingsUI extends JDialog {
    private JTextField bookIdField;
    private JTextField memberIdField;
    private JTextField borrowedDateField;
    private JTextField dueDateField;
    private JTextField returnedDateField;
    private JTextField fineField;
    private BorrowingDao borrowingDao;

    public AddborrowingsUI(Frame owner, Connection connection) {
        super(owner, "Add New Borrowing", true);
        this.borrowingDao = new BorrowingDao(connection);

        setLayout(new GridLayout(7, 2, 5, 5));
        setSize(400, 300);
        setLocationRelativeTo(owner);

        add(new JLabel("Book ID:"));
        bookIdField = new JTextField();
        add(bookIdField);

        add(new JLabel("Member ID:"));
        memberIdField = new JTextField();
        add(memberIdField);

        add(new JLabel("Borrowed Date (yyyy-mm-dd):"));
        borrowedDateField = new JTextField();
        add(borrowedDateField);

        add(new JLabel("Due Date (yyyy-mm-dd):"));
        dueDateField = new JTextField();
        add(dueDateField);

        add(new JLabel("Returned Date (yyyy-mm-dd):"));
        returnedDateField = new JTextField();
        add(returnedDateField);

        add(new JLabel("Fine:"));
        fineField = new JTextField();
        add(fineField);

        JButton addButton = new JButton("Add Borrowing");
        addButton.addActionListener(e -> addBorrowing());
        add(addButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setVisible(true);
    }

    private void addBorrowing() {
        try {
            Borrowing borrowing = new Borrowing();
            borrowing.setBookId(Integer.parseInt(bookIdField.getText()));
            borrowing.setMemberId(Integer.parseInt(memberIdField.getText()));
            borrowing.setBorrowedDate(LocalDate.parse(borrowedDateField.getText()));
            borrowing.setDueDate(LocalDate.parse(dueDateField.getText()));
            borrowing.setReturnedDate(returnedDateField.getText().isEmpty() ? null : LocalDate.parse(returnedDateField.getText()));
            borrowing.setFine(Double.parseDouble(fineField.getText()));

            borrowingDao.addBorrowing(borrowing);
            JOptionPane.showMessageDialog(this, "Borrowing added successfully!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding borrowing: " + ex.getMessage());
        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }
}
