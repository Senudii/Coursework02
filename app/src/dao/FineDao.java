package dao;

import Entity.Borrowing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDao {
    private Connection connection;

    public FineDao(Connection connection) {
        this.connection = connection;
    }

    public List<Borrowing> getAllBorrowingsWithFines() throws SQLException {
        List<Borrowing> borrowings = new ArrayList<>();
        String query = "SELECT * FROM borrowings WHERE fine > 0.0";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(resultSet.getInt("id"));
                borrowing.setBookId(resultSet.getInt("book_id"));
                borrowing.setMemberId(resultSet.getInt("member_id"));
              
                Date returnedDate = resultSet.getDate("returned_date");
                borrowing.setFine(resultSet.getDouble("fine"));
                borrowing.setPaymentStatus(resultSet.getString("payment_status"));
                borrowings.add(borrowing);
            }
        }
        return borrowings;
    }

    public void updatePaymentStatus(int id, String status) throws SQLException {
        String query = "UPDATE borrowings SET payment_status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }
}
