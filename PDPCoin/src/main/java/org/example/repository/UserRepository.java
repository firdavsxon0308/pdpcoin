package org.example.repository;

import org.example.jdbc.CustomDataSource;
import org.example.modul.User;
import org.example.states.BotState;
import org.example.states.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final String findAllUsers = "SELECT * FROM user";
    private final String findById = "SELECT * FROM botusers WHERE chat_id = ?";
    private final String findBotStateById = "SELECT bot_state FROM botusers WHERE chat_id = ?";
    private final String findBotRole = "SELECT role FROM botusers WHERE chat_id = ?";
    private final String updateById = "UPDATE user SET fullname=?, role=?, bot_state=?, grade = ? WHERE id=?";
    private final String insertUser =
            "INSERT INTO botusers (chat_id, bot_state) VALUES(?,?)";

    public List<User> findAllUser(long chatId) {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(findAllUsers);
            while (resultSet.next()) {
                User user = new User(chatId, null, null, null, BotState.START);
                user.setChatId(resultSet.getLong("chat_id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setBotState(BotState.valueOf(resultSet.getString("bot_state")));
                user.setGrade(String.valueOf(resultSet.getInt("grade")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public User getUserById(long chatId) {
        User user = new User();
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(findById, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setObject(1, chatId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user.setChatId(resultSet.getLong("chat_id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setBotState((BotState) resultSet.getObject("bot_state"));
                user.setGrade(String.valueOf(resultSet.getInt("grade")));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public BotState getUserBotState(long chatId) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(findBotStateById, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setObject(1, chatId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return BotState.valueOf(resultSet.getString("bot_state"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Role getUserRole(long chatId) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(findBotRole)
        ) {
            ps.setLong(1, chatId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return Role.valueOf(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean createUser(User user) {
        int i = 0;
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setObject(1, user.getChatId());
            ps.setObject(2, user.getBotState().name());
            i = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i > 0;
    }

    public boolean updateBotState(long chatId, BotState botState) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE botusers SET bot_state = ? WHERE chat_id = ?",
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setObject(1, botState);
            ps.setObject(2, chatId);
            return ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getUpdateById(Long id, User user) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(updateById)
        ) {
            ps.setObject(1, user.getFullName());
            ps.setObject(2, user.getRole());
            ps.setObject(3, user.getBotState());
            ps.setObject(4, user.getGrade());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
