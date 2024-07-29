package ui;

import dao.BookDao;
import Entity.Book;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdatebookUI extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<String> categoryComboBox;
    private JTextField editionField;
    private JTextArea descriptionArea;
    private BookDao bookDao;
    private Book book;

    public UpdatebookUI(Frame owner, Connection connection, Book book) {
        super(owner, "Update Book", true);
        this.bookDao = new BookDao(connection);
        this.book = book;

        setLayout(new GridLayout(6, 2));
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(book.getTitle());
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField(book.getAuthor());
        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>(getCategories());
        categoryComboBox.setSelectedIndex(book.getCategoryId() - 1);
        JLabel editionLabel = new JLabel("Edition:");
        editionField = new JTextField(book.getEdition());
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(book.getDescription());

        JButton updateButton = new JButton("Update Book");
        updateButton.addActionListener(e -> updateBook());

        add(titleLabel);
        add(titleField);
        add(authorLabel);
        add(authorField);
        add(categoryLabel);
        add(categoryComboBox);
        add(editionLabel);
        add(editionField);
        add(descriptionLabel);
        add(new JScrollPane(descriptionArea));
        add(new JLabel()); // Filler
        add(updateButton);
    }

    private String[] getCategories() {
        // In a real-world application, you would fetch categories from the database.
        // For simplicity, I'm using hardcoded values.
        return new String[]{"Fiction", "Non-Fiction", "Science", "History"};
    }

    private void updateBook() {
        try {
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setCategoryId(categoryComboBox.getSelectedIndex() + 1);
            book.setEdition(editionField.getText());
            book.setDescription(descriptionArea.getText());

            bookDao.updateBook(book);
            JOptionPane.showMessageDialog(this, "Book updated successfully!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
        }
    }
}
