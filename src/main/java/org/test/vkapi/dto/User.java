package org.test.vkapi.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name="user_info")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    @Column(name = "user_f_name")
    private String userFirstName;
    @Column(name = "user_l_name")
    private String userLastName;
    @Column(name = "user_b_date")
    private LocalDate userBirthdayDate;
    @Column(name = "user_city")
    private String userCity;
    @Column(name = "user_contacts")
    private String userContacts;
}
