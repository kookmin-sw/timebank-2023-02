package kookmin.software.capstone2023.timebank.domain.model.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kookmin.software.capstone2023.timebank.domain.model.BaseTimeEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "authentication_social")
@Getter
@Setter
public class SocialAuthentication extends BaseTimeEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SocialPlatformType platformType;

    @Column(nullable = false, updatable = true, length = 100)
    private String platformUserId;

    public SocialAuthentication(Long userId, SocialPlatformType platformType, String platformUserId) {
        this.userId = userId;
        this.platformType = platformType;
        this.platformUserId = platformUserId;
    }
}
