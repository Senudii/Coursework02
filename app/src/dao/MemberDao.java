package dao;

import Entity.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {
    private Connection connection;

    public MemberDao(Connection connection) {
        this.connection = connection;
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setEmail(resultSet.getString("email"));
                members.add(member);
            }
        }
        return members;
    }

    public void addMember(String name, String email) throws SQLException {
        String query = "INSERT INTO members (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

    public void updateMember(int id, String name, String email) throws SQLException {
        String query = "UPDATE members SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, id);
            statement.executeUpdate();
        }
    }

    public void deleteMember(int id) throws SQLException {
        String query = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
