package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import kookmin.software.capstone2023.timebank.domain.model.Gender;

import java.time.LocalDate;

public class UserUpdateRequestData {

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(max = 20, message = "이름은 20자 이하로 입력해주세요.")
    private final String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 11자리 숫자만 입력 가능합니다.")
    private final String phoneNumber;

    private final Gender gender;
    private final LocalDate birthday;

    public UserUpdateRequestData(String name, String phoneNumber, Gender gender, LocalDate birthday) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
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
}
