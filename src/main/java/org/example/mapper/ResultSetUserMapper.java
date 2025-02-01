package org.example.mapper;

import org.example.dto.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ResultSetUserMapper implements Mapper<ResultSet, User> {
    private final String USER_ID = "user_id";
    private final String USER_FIRST_NAME = "user_f_name";
    private final String USER_LAST_NAME = "user_l_name";
    private final String USER_BIRTHDAY_DATE = "user_b_date";
    private final String USER_CITY = "user_city";
    private final String USER_CONTACTS = "user_contacts";

    private final DateTimeFormatter formatterWithYear = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public User map(ResultSet resultSet) {
        if (resultSet == null) return null;
        User user = new User();
        try {
            if (Objects.nonNull(resultSet.getObject(USER_ID))) {
                user.setUserId(resultSet.getLong(USER_ID));
            }
            if (Objects.nonNull(resultSet.getObject(USER_FIRST_NAME))) {
                user.setUserFirstName(resultSet.getString(USER_FIRST_NAME));
            }
            if (Objects.nonNull(resultSet.getObject(USER_LAST_NAME))) {
                user.setUserLastName(resultSet.getString(USER_LAST_NAME));
            }
            if (Objects.nonNull(resultSet.getObject(USER_BIRTHDAY_DATE))) {
                user.setUserBirthdayDate(LocalDate.parse(resultSet.getString(USER_BIRTHDAY_DATE), formatterWithYear));
            }
            if (Objects.nonNull(resultSet.getObject(USER_CITY))) {
                user.setUserCity(resultSet.getString(USER_CITY));
            }if (Objects.nonNull(resultSet.getObject(USER_CONTACTS))) {
                user.setUserContacts(resultSet.getString(USER_CONTACTS));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return user;
    }
}
