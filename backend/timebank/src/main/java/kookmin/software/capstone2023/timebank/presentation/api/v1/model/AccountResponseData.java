package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

import kookmin.software.capstone2023.timebank.domain.model.Account;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;

public class AccountResponseData {

    private final Long id;
    private final AccountType type;
    private final String name;
    private final ProfileResponseData profile;

    public AccountResponseData(Long id, AccountType type, String name, ProfileResponseData profile) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public AccountType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ProfileResponseData getProfile() {
        return profile;
    }

    public static class ProfileResponseData {
        private final String nickname;
        private final String imageUrl;

        public ProfileResponseData(String nickname, String imageUrl) {
            this.nickname = nickname;
            this.imageUrl = imageUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static AccountResponseData fromDomain(Account account) {
        return new AccountResponseData(
                account.getId(),
                account.getType(),
                account.getName(),
                account.getProfile() != null ? new ProfileResponseData(
                        account.getProfile().getNickname(),
                        account.getProfile().getImageUrl()
                ) : null
        );
    }
}
