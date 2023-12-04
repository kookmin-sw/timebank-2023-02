package kookmin.software.capstone2023.timebank.domain.model.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kookmin.software.capstone2023.timebank.domain.model.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authentication_password")
@Getter
@Setter
@NoArgsConstructor
public class PasswordAuthentication extends BaseTimeEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = true)
    private String username;

    @Column(nullable = false, updatable = true)
    private String password;

    public PasswordAuthentication(Long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
