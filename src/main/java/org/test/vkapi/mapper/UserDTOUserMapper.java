package org.test.vkapi.mapper;

import org.test.vkapi.date.DateValidator;
import org.test.vkapi.date.DateValidatorUsingLocalDate;
import org.test.vkapi.dto.User;
import org.test.vkapi.dto.UserDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;

public class UserDTOUserMapper implements Mapper<UserDTO, User>{
    private final DateTimeFormatter formatterWithYear = DateTimeFormatter.ofPattern("d.M.yyyy");
    private final DateTimeFormatter formatterWithoutYear = new DateTimeFormatterBuilder()
                                                            .appendPattern("dd.MM")
                                                            .parseDefaulting(ChronoField.YEAR, 0)
                                                            .toFormatter();
    private final DateValidator validatorWithYear = new DateValidatorUsingLocalDate(formatterWithYear);
    private final DateValidator validatorWithoutYear = new DateValidatorUsingLocalDate(formatterWithoutYear);
    @Override
    public User map(UserDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setUserId(userDTO.getId());
        user.setUserFirstName(userDTO.getFirstName());
        user.setUserLastName(userDTO.getLastName());
        if (Objects.nonNull(userDTO.getUserCity())) {
            user.setUserCity(userDTO.getUserCity().getTitle());
        }
        if (Objects.nonNull(userDTO.getUserContacts())) {
            user.setUserContacts(userDTO.getUserContacts().toString());
        }
        String bdate = userDTO.getBdate();
        if (Objects.nonNull(bdate) && validatorWithYear.isValid(bdate)) {
            LocalDate localDate = LocalDate.parse(bdate, formatterWithYear);
            user.setUserBirthdayDate(localDate);
        }
        if (Objects.nonNull(bdate) && validatorWithoutYear.isValid(bdate)) {
            LocalDate localDate = LocalDate.parse(bdate, formatterWithoutYear);
            user.setUserBirthdayDate(localDate);
        }
        return user;
    }
}
