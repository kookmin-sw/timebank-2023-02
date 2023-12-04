package kookmin.software.capstone2023.timebank.domain.model;

import kookmin.software.capstone2023.timebank.application.service.payapp.PayAppService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class PayAppList {
    private final List<PayApp> payAppList = new ArrayList<>();
    private static PayAppList instance = null;

    private PayAppList(){

    }
    public static synchronized PayAppList getInstance() {
        if (instance == null) {
            instance = new PayAppList();
        }
        return instance;
    }

    public void addPayApp(PayApp payApp){
        this.payAppList.add(payApp);
    }
}
