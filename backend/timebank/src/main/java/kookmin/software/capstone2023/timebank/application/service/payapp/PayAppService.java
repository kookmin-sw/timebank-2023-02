package kookmin.software.capstone2023.timebank.application.service.payapp;

import kookmin.software.capstone2023.timebank.domain.model.PayApp;
import kookmin.software.capstone2023.timebank.domain.repository.PayAppRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class PayAppService {
    private final PayAppRepository payAppRepository;

    public void savePayApp(PayApp payApp) {
        payAppRepository.save(payApp);
    }

    public Optional<PayApp> findPayAppByName(String name) {
        return payAppRepository.findByName(name);
    }
}
