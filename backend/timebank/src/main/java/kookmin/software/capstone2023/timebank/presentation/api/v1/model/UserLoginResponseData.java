package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

public class UserLoginResponseData {

    private final String accessToken;

    public UserLoginResponseData(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
