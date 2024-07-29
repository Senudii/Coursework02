package ui;

import dao.BorrowingDao;
import Entity.Borrowing;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UpdateborrowingsUI extends JFrame {
    private BorrowingDao borrowingDao;
    private Borrowing borrowing;

    private JTextField bookIdField;
    private JTextField memberIdField;
    private JTextField borrowedDateField;
    private JTextField dueDateField;
    private JTextField returnedDateField;
    private JTextField fineField;

    public UpdateborrowingsUI(JFrame parentFrame, Connection connection, Borrowing borrowing) {
        this.borrowingDao = new BorrowingDao(connection);
        this.borrowing = borrowing;

        setTitle("Update Borrowing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Book ID:"));
        bookIdField = new JTextField(String.valueOf(borrowing.getBookId()));
        panel.add(bookIdField);

        panel.add(new JLabel("Member ID:"));
        memberIdField = new JTextField(String.valueOf(borrowing.getMemberId()));
        panel.add(memberIdField);

        panel.add(new JLabel("Borrowed Date (YYYY-MM-DD):"));
        borrowedDateField = new JTextField(borrowing.getBorrowedDate().toString());
        panel.add(borrowedDateField);

        panel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        dueDateField = new JTextField(borrowing.getDueDate().toString());
        panel.add(dueDateField);

        panel.add(new JLabel("Returned Date (YYYY-MM-DD):"));
        returnedDateField = new JTextField(borrowing.getReturnedDate() != null ? borrowing.getReturnedDate().toString() : "");
        panel.add(returnedDateField);

        panel.add(new JLabel("Fine:"));
        fineField = new JTextField(String.valueOf(borrowing.getFine()));
        panel.add(fineField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateBorrowing());
        panel.add(updateButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
    }

    private void updateBorrowing() {
        try {
            // Get values from fields
            int bookId = Integer.parseInt(bookIdField.getText());
            int memberId = Integer.parseInt(memberIdField.getText());
            LocalDate borrowedDate = LocalDate.parse(borrowedDateField.getText());
            LocalDate dueDate = LocalDate.parse(dueDateField.getText());
            LocalDate returnedDate = null;
            if (!returnedDateField.getText().isEmpty()) {
                returnedDate = LocalDate.parse(returnedDateField.getText());
            }
            double fine = Double.parseDouble(fineField.getText());

            // Set values to the borrowing object
            borrowing.setBookId(bookId);
            borrowing.setMemberId(memberId);
            borrowing.setBorrowedDate(borrowedDate);
            borrowing.setDueDate(dueDate);
            borrowing.setReturnedDate(returnedDate);
            borrowing.setFine(fine);

            // Update borrowing in the database
            borrowingDao.updateBorrowing(borrowing);

            JOptionPane.showMessageDialog(this, "Borrowing updated successfully!");
            dispose();
        } catch (NumberFormatException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check the entered values.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating borrowing: " + e.getMessage());
        }
    }
}
