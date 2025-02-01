package org.test.vkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.test.vkapi.dto.User;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userFirstName = :user_f_name, " +
                                "u.userLastName = :user_l_name, " +
                                "u.userBirthdayDate = :user_b_date, " +
                                "u.userCity = :user_city, " +
                                "u.userContacts = :user_contacts " +
                                "WHERE u.userId = :user_id")
    int updateUser(@Param("user_id") Long id,
                   @Param("user_f_name") String firstName,
                   @Param("user_l_name") String lastName,
                   @Param("user_b_date") LocalDate birthdayDate,
                   @Param("user_city") String city,
                   @Param("user_contacts") String contacts);
}
