package dao;

import Entity.Borrowing;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDao {
    private Connection connection;

    public BorrowingDao(Connection connection) {
        this.connection = connection;
    }

    public List<Borrowing> getAllBorrowings() throws SQLException {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(rs.getInt("id"));
                borrowing.setBookId(rs.getInt("book_id"));
                borrowing.setMemberId(rs.getInt("member_id"));
                borrowing.setBorrowedDate(rs.getDate("borrowed_date").toLocalDate());
                borrowing.setDueDate(rs.getDate("due_date").toLocalDate());
                borrowing.setReturnedDate(rs.getDate("returned_date") != null ? rs.getDate("returned_date").toLocalDate() : null);
                borrowing.setFine(rs.getDouble("fine"));
                borrowings.add(borrowing);
            }
        }

        return borrowings;
    }

    public void addBorrowing(Borrowing borrowing) throws SQLException {
        String sql = "INSERT INTO borrowings (book_id, member_id, borrowed_date, due_date, returned_date, fine) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, borrowing.getBookId());
            stmt.setInt(2, borrowing.getMemberId());
            stmt.setDate(3, Date.valueOf(borrowing.getBorrowedDate()));
            stmt.setDate(4, Date.valueOf(borrowing.getDueDate()));
            stmt.setDate(5, borrowing.getReturnedDate() != null ? Date.valueOf(borrowing.getReturnedDate()) : null);
            stmt.setDouble(6, borrowing.getFine());

            stmt.executeUpdate();
        }
    }

    public void updateBorrowing(Borrowing borrowing) throws SQLException {
        String sql = "UPDATE borrowings SET book_id = ?, member_id = ?, borrowed_date = ?, due_date = ?, returned_date = ?, fine = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, borrowing.getBookId());
            stmt.setInt(2, borrowing.getMemberId());
            stmt.setDate(3, Date.valueOf(borrowing.getBorrowedDate()));
            stmt.setDate(4, Date.valueOf(borrowing.getDueDate()));
            stmt.setDate(5, borrowing.getReturnedDate() != null ? Date.valueOf(borrowing.getReturnedDate()) : null);
            stmt.setDouble(6, borrowing.getFine());
            stmt.setInt(7, borrowing.getId());

            stmt.executeUpdate();
        }
    }

    public void deleteBorrowing(int id) throws SQLException {
        String sql = "DELETE FROM borrowings WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
