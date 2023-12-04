package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

public class PasswordVerificationResponseData {

    private final String resResultDesc;
    private final String resResultCode;

    public PasswordVerificationResponseData(String resResultDesc, String resResultCode) {
        this.resResultDesc = resResultDesc;
        this.resResultCode = resResultCode;
    }

    public String getResResultDesc() {
        return resResultDesc;
    }

    public String getResResultCode() {
        return resResultCode;
    }
}
