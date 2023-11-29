package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

import kookmin.software.capstone2023.timebank.domain.model.Gender;

import java.time.LocalDate;

public class CurrentUserResponseData {

    private final Long id;
    private final String name;
    private final String phoneNumber;
    private final Gender gender;
    private final LocalDate birthday;
    private final AccountResponseData account;

    public CurrentUserResponseData(Long id, String name, String phoneNumber, Gender gender, LocalDate birthday, AccountResponseData account) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public AccountResponseData getAccount() {
        return account;
    }
}
