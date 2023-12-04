package kookmin.software.capstone2023.timebank.domain.model;


import jakarta.persistence.*;
import kookmin.software.capstone2023.timebank.application.service.payapp.CreateKeys;
import kookmin.software.capstone2023.timebank.application.service.payapp.PayAppService;
import lombok.*;


@Entity
@Table(name = "payapp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = true, length = 20)
    private String name;

    @Column(nullable = false, updatable = true)
    private String appkey;

    public void updateAppInfo(String name) {
        this.name = name;
    }

    public PayApp(String name) {
        this.name = name;
        this.appkey = createPayAppKey();
    }

    private String createPayAppKey() {
        CreateKeys createKeys = new CreateKeys();
        return createKeys.createKey();
    }

}
