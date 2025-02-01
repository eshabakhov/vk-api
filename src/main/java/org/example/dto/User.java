package org.example.dto;

import java.time.LocalDate;

public class User {
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private LocalDate userBirthdayDate;
    private String userCity;
    private String userContacts;

    public User() {

    }

    public User(Long userId) {
        this.userId = userId;
    }

    public User(Long userId, String userFirstName, String userLastName, LocalDate userBirthdayDate, String userCity, String userContacts) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userBirthdayDate = userBirthdayDate;
        this.userCity = userCity;
        this.userContacts = userContacts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public LocalDate getUserBirthdayDate() {
        return userBirthdayDate;
    }

    public void setUserBirthdayDate(LocalDate userBirthdayDate) {
        this.userBirthdayDate = userBirthdayDate;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(String userContacts) {
        this.userContacts = userContacts;
    }
}
