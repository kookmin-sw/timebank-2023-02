package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

public class UserUpdatePasswordRequestData {

    private final String password;

    public UserUpdatePasswordRequestData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
