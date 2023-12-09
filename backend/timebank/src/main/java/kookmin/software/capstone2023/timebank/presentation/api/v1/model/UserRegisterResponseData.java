package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserRegisterResponseData {

    private final Long accountId;
    private final Long userId;
}
