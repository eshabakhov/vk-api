package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.User;
import org.example.exception.UserUpdateException;
import org.example.mapper.ResultSetUserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class UserDAO {

    public long count() {
        String sqlSelectCountUsers = "SELECT count(*) FROM user_info";
        long countUsers = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectCountUsers)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetUserMapper mapper = new ResultSetUserMapper();
            while (resultSet.next()) {
                countUsers = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return countUsers;
    }

    public List<User> getUsersPageable(int page, int pageSize) {
        String sqlSelectUsersPagination = "SELECT * FROM user_info u " +
                "ORDER BY u.user_id " +
                "LIMIT ? OFFSET (? - 1) * ?";

        List<User> userList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectUsersPagination)) {
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, page);
            preparedStatement.setInt(3, pageSize);

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetUserMapper mapper = new ResultSetUserMapper();
            while (resultSet.next()) {
                userList.add(mapper.map(resultSet));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return userList;
    }

    public void updateUsers(User user) {
        StringBuilder sqlUpdateUsers = new StringBuilder("UPDATE user_info SET ");
        List<Object> parameters = new ArrayList<>();
        if (Objects.nonNull(user.getUserFirstName())) {
            sqlUpdateUsers.append("user_f_name = ?, ");
            parameters.add(user.getUserFirstName());
        }
        if (Objects.nonNull(user.getUserLastName())) {
            sqlUpdateUsers.append("user_l_name = ?, ");
            parameters.add(user.getUserLastName());
        }
        if (Objects.nonNull(user.getUserBirthdayDate())) {
            sqlUpdateUsers.append("user_b_date = ?, ");
            parameters.add(user.getUserBirthdayDate());
        }
        if (Objects.nonNull(user.getUserCity())) {
            sqlUpdateUsers.append("user_city = ?, ");
            parameters.add(user.getUserCity());
        }
        if (Objects.nonNull(user.getUserContacts())) {
            sqlUpdateUsers.append("user_contacts = ? ");
            parameters.add(user.getUserContacts());
        }

        if (parameters.isEmpty()) {
            log.info("Нет данных для обновления");
            return;
        }

        sqlUpdateUsers.setLength(sqlUpdateUsers.length() - 2);
        sqlUpdateUsers.append(" WHERE user_id = ?");
        parameters.add(user.getUserId());

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateUsers.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated <= 0)
                throw new UserUpdateException(String.format("При обновлении пользователя %d произошла ошибка", user.getUserId()));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

    }
}
