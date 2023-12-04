package kookmin.software.capstone2023.timebank.presentation.api.v1;

import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferService;
import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferServiceImpl;
import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer.BankAccountTransferRequestData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer.BankFundTransferResponseData;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@UserAuthentication
@RestController
@RequestMapping("api/v1/bank/account/transfer")
public class BankTransferController {

    private final TransferServiceImpl transferService;

    public BankTransferController(TransferServiceImpl transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public BankFundTransferResponseData transfer(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @Validated @RequestBody BankAccountTransferRequestData data) {
        BankAccountTransaction response = transferService.transfer(
                new TransferService.TransferRequest(
                        userContext.getAccountId(),
                        data.getSenderBankAccountNumber(),
                        data.getReceiverBankAccountNumber(),
                        data.getAmount(),
                        data.getPassword()
                )
        );

        return new BankFundTransferResponseData(
                response.getTransactionAt(),
                response.getAmount(),
                response.getBalanceSnapshot(),
                response.getStatus(),
                response.getSenderBankAccount().getAccountNumber(),
                response.getReceiverBankAccount().getAccountNumber()
        );
    }
}
