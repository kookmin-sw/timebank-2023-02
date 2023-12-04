package kookmin.software.capstone2023.timebank.presentation.api.v1;

import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.application.service.payapp.PayAppService;
import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import kookmin.software.capstone2023.timebank.domain.model.PayApp;
import kookmin.software.capstone2023.timebank.domain.model.PayAppList;
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountTransactionSpecs;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountTransactionResponseData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.AccountResponseData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.CurrentUserResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/payapp")
public class PayAppController {
    private final PayAppService payAppService;
    public PayAppController(PayAppService payAppService){
        this.payAppService = payAppService;
    }

    @GetMapping("register/{appName}")
    @ResponseBody
    public PayApp registerPayApp(@PathVariable String appName) {
        PayApp payApp = new PayApp(appName);
        PayAppList payAppList = PayAppList.getInstance();

        payAppList.addPayApp(payApp);
        payAppService.savePayApp(payApp);
        return payApp;
    }

}
