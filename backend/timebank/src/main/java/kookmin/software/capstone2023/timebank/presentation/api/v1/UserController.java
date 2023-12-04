package kookmin.software.capstone2023.timebank.presentation.api.v1;

import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.application.service.account.AccountFinder;
import kookmin.software.capstone2023.timebank.application.service.auth.AccountLoginService;
import kookmin.software.capstone2023.timebank.application.service.auth.AccountRegisterService;
import kookmin.software.capstone2023.timebank.application.service.user.UserFinder;
import kookmin.software.capstone2023.timebank.application.service.user.UserUpdateService;
import kookmin.software.capstone2023.timebank.application.service.user.UserWithdrawalService;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final AccountFinder accountFinder;
    private final AccountLoginService accountLoginService;
    private final AccountRegisterService accountRegisterService;
    private final UserFinder userFinder;
    private final UserUpdateService userUpdateService;
    private final UserWithdrawalService userWithdrawalService;

    public UserController(
            AccountFinder accountFinder,
            AccountLoginService accountLoginService,
            AccountRegisterService accountRegisterService,
            UserFinder userFinder,
            UserUpdateService userUpdateService,
            UserWithdrawalService userWithdrawalService) {
        this.accountFinder = accountFinder;
        this.accountLoginService = accountLoginService;
        this.accountRegisterService = accountRegisterService;
        this.userFinder = userFinder;
        this.userUpdateService = userUpdateService;
        this.userWithdrawalService = userWithdrawalService;
    }

    @PostMapping("login")
    public UserLoginResponseData loginUser(
            @Validated @RequestBody UserLoginRequestData data) {
        var loginData = accountLoginService.login(data.toAuthenticationRequest());

        return new UserLoginResponseData(loginData.getAccessToken());
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(
            @Validated @RequestBody UserRegisterRequestData data) {
        accountRegisterService.register(
                data.toAuthenticationRequest(),
                data.getName(),
                data.getPhoneNumber(),
                data.getGender(),
                data.getBirthday(),
                AccountType.INDIVIDUAL
        );
    }

    @UserAuthentication
    @GetMapping("me")
    public CurrentUserResponseData getCurrentUser(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext) {
        var user = userFinder.findById(userContext.getUserId());
        if (user == null) {
            throw new NotFoundException("유저 정보를 찾을 수 없습니다.");
        }

        return new CurrentUserResponseData(
                user.getId(),
                user.getName(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getBirthday(),
                AccountResponseData.fromDomain(user.getAccount())
        );
    }

    @UserAuthentication
    @PutMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserInfo(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestBody UserUpdateRequestData data) {
        userUpdateService.updateUserInfo(
                userContext.getUserId(),
                data.getName(),
                data.getPhoneNumber(),
                data.getGender(),
                data.getBirthday()
        );
    }

    @UserAuthentication
    @PutMapping("me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserPassword(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestBody UserUpdatePasswordRequestData data) {
        userUpdateService.updatePassword(
                userContext.getUserId(),
                data.getPassword()
        );
    }

    @UserAuthentication
    @DeleteMapping("me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawalUser(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext) {
        userWithdrawalService.withdrawal(userContext.getUserId());
    }
}
