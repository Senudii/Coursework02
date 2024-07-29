package ui;

import dao.BookDao;
import Entity.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class AddBookUI extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<String> categoryComboBox;
    private JTextField editionField;
    private JTextArea descriptionArea;
    private BookDao bookDao;

    public AddBookUI(Frame owner, Connection connection) {
        super(owner, "Add New Book", true);
        this.bookDao = new BookDao(connection);

        setLayout(new GridLayout(6, 2)); // Adjusted layout for more fields
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>(getCategories());
        JLabel editionLabel = new JLabel("Edition:");
        editionField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(3, 20);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new AddBookAction());

        add(titleLabel);
        add(titleField);
        add(authorLabel);
        add(authorField);
        add(categoryLabel);
        add(categoryComboBox);
        add(editionLabel);
        add(editionField);
        add(descriptionLabel);
        add(new JScrollPane(descriptionArea)); // Use JScrollPane for text area
        add(new JLabel()); // Filler
        add(addButton);
    }

    private String[] getCategories() {
        // In a real-world application, you would fetch categories from the database.
        // For simplicity, I'm using hardcoded values.
        return new String[]{"Fiction", "Non-Fiction", "Science", "History"};
    }

    private class AddBookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                int categoryId = categoryComboBox.getSelectedIndex() + 1; // Example: Using index + 1 as category ID
                String edition = editionField.getText();
                String description = descriptionArea.getText();

                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setCategoryId(categoryId);
                book.setEdition(edition);
                book.setDescription(description);

                bookDao.addBook(book);
                JOptionPane.showMessageDialog(AddBookUI.this, "Book added successfully!");
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(AddBookUI.this, "Error adding book: " + ex.getMessage());
            }
        }
    }
}
