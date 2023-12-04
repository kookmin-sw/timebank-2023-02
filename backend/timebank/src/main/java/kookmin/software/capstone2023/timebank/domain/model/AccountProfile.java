package kookmin.software.capstone2023.timebank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class AccountProfile {

    /**
     * 닉네임
     */
    @Column(nullable = true, updatable = true, length = 20)
    private String nickname;

    /**
     * 프로필 이미지 URL
     */
    @Column(nullable = true, updatable = true, length = 100)
    private String imageUrl;
}
