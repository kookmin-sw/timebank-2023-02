package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model;

public class ManagerLoginResponseData {

    private final String accessToken;

    public ManagerLoginResponseData(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
