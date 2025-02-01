package org.example.mapper;

import org.example.date.DateValidator;
import org.example.date.DateValidatorUsingLocalDate;
import org.example.dto.User;
import org.json.JSONObject;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class JSONUserMapper implements Mapper<JSONObject, User> {
    private final String ID = "id";
    private final String FIRST_NAME = "first_name";
    private final String LAST_NAME = "last_name";
    private final String BIRTHDAY_DATE = "bdate";
    private final String CITY = "city";
    private final String CONTACTS = "contacts";

    private final DateTimeFormatter formatterWithYear = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter formatterWithoutYear = new DateTimeFormatterBuilder()
                                                                    .appendPattern("dd.MM")
                                                                    .parseDefaulting(ChronoField.YEAR, 3000)
                                                                    .toFormatter();
    private final DateValidator validatorWithYear = new DateValidatorUsingLocalDate(formatterWithYear);
    private final DateValidator validatorWithoutYear = new DateValidatorUsingLocalDate(formatterWithoutYear);
    @Override
    public User map(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        User user = new User();
        if (!jsonObject.isNull(ID)) {
            user.setUserId(jsonObject.getLong(ID));
        }
        if (!jsonObject.isNull(FIRST_NAME)) {
            user.setUserFirstName(jsonObject.getString(FIRST_NAME));
        }
        if (!jsonObject.isNull(LAST_NAME)) {
            user.setUserLastName(jsonObject.getString(LAST_NAME));
        }
        if (!jsonObject.isNull(BIRTHDAY_DATE)) {
            String bdate = jsonObject.getString(BIRTHDAY_DATE);
            if (validatorWithYear.isValid(bdate)) {
                LocalDate localDate = LocalDate.parse(bdate, formatterWithYear);
                user.setUserBirthdayDate(localDate);
            }
            if (validatorWithoutYear.isValid(bdate)) {
                LocalDate localDate = LocalDate.parse(bdate, formatterWithoutYear);
                user.setUserBirthdayDate(localDate);
            }
        }
        if (!jsonObject.isNull(CITY)) {
            JSONUserCityMapper jsonUserCityMapper = new JSONUserCityMapper();
            user.setUserCity(jsonUserCityMapper.map(jsonObject.getJSONObject(CITY)));
        }
        if (!jsonObject.isNull(CONTACTS)) {
            user.setUserContacts(jsonObject.getJSONObject(CONTACTS).toString());
        }
        return user;
    }
}
