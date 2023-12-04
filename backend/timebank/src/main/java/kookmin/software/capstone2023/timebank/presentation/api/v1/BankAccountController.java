package kookmin.software.capstone2023.timebank.presentation.api.v1;

import jakarta.servlet.http.HttpServletRequest;
import kookmin.software.capstone2023.timebank.application.service.bank.account.BankAccountCreateService;
import kookmin.software.capstone2023.timebank.application.service.bank.account.BankAccountReadService;
import kookmin.software.capstone2023.timebank.application.service.bank.account.BankAccountUpdateService;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@UserAuthentication
@RestController
@RequestMapping("api/v1/bank/account")
public class BankAccountController {

    private final BankAccountCreateService bankAccountCreateService;
    private final BankAccountReadService bankAccountReadService;
    private final BankAccountUpdateService bankAccountUpdateService;

    public BankAccountController(BankAccountCreateService bankAccountCreateService,
                                 BankAccountReadService bankAccountReadService,
                                 BankAccountUpdateService bankAccountUpdateService) {
        this.bankAccountCreateService = bankAccountCreateService;
        this.bankAccountReadService = bankAccountReadService;
        this.bankAccountUpdateService = bankAccountUpdateService;
    }

    @PostMapping
    public BankAccountCreateResponseData createBankAccount(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @Validated @RequestBody BankAccountCreateRequestData data) {
        BankAccountCreateService.CreatedBankAccount createdBankAccount =
                bankAccountCreateService.createBankAccount(
                        userContext.getAccountId(),
                        data.getPassword(),
                        Long.valueOf(1));

        return new BankAccountCreateResponseData(
                createdBankAccount.getBalance(),
                createdBankAccount.getBankAccountNumber(),
                createdBankAccount.getBankAccountId()
        );
    }

    @GetMapping
    public List<BankAccountReadResponseData> readBankAccounts(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext) {
        List<BankAccountReadService.ReadedBankAccount> bankAccountReadResponseDataList =
                bankAccountReadService.readBankAccountsByAccountId(userContext.getAccountId());

        return bankAccountReadResponseDataList.stream()
                .map(it -> new BankAccountReadResponseData(
                        it.getBankAccountId(),
                        it.getBranchId(),
                        it.getBalance(),
                        it.getCreatedAt(),
                        it.getBankAccountNumber(),
                        it.getOwnerName(),
                        it.getOwnerType()))
                .collect(Collectors.toList());
    }

    @GetMapping("{bankAccountNumber}")
    public BankAccountReadResponseData readBankAccount(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable String bankAccountNumber) {
        BankAccountReadService.ReadedBankAccount bankAccountReadResponseData =
                bankAccountReadService.readBankAccountByAccountNumber(
                        userContext.getAccountId(),
                        bankAccountNumber);

        return new BankAccountReadResponseData(
                bankAccountReadResponseData.getBankAccountId(),
                bankAccountReadResponseData.getBranchId(),
                bankAccountReadResponseData.getBalance(),
                bankAccountReadResponseData.getCreatedAt(),
                bankAccountReadResponseData.getBankAccountNumber(),
                bankAccountReadResponseData.getOwnerName(),
                bankAccountReadResponseData.getOwnerType());
    }

    @PostMapping("/account-password-verification")
    public PasswordVerificationResponseData verifyAccountPassword(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @Validated @RequestBody PasswordVerificationRequestData data,
            HttpServletRequest httpRequest) {
        String ipAddress = getClientIpAddress(httpRequest);
        BankAccountReadService.VerificationResult res = bankAccountReadService.verifyPassword(data, ipAddress);
        return new PasswordVerificationResponseData(
                String.valueOf(res.getFailedAttempts()),
                String.valueOf(res.getIsPasswordCorrect()));
    }

    @PutMapping("/password")
    public PasswordUpdateResponseData updatePassword(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @Validated @RequestBody PasswordUpdateRequestData data) {
        BankAccountUpdateService.UpdatedBankAccount updatedBankAccount =
                bankAccountUpdateService.updateBankAccountPassword(
                        userContext.getAccountId(),
                        data.getBankAccountNumber(),
                        data.getBeforePassword(),
                        data.getAfterPassword());

        return new PasswordUpdateResponseData(
                updatedBankAccount.getBankAccountNumber(),
                updatedBankAccount.getUpdatedAt(),
                "1");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        return (ipAddress == null || ipAddress.isEmpty()) ? request.getRemoteAddr() : ipAddress;
    }
}
