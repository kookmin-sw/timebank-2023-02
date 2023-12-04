package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.auth.SocialAuthentication;
import kookmin.software.capstone2023.timebank.domain.model.auth.SocialPlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialAuthenticationJpaRepository extends JpaRepository<SocialAuthentication, Long> {

    SocialAuthentication findByPlatformTypeAndPlatformUserId(SocialPlatformType platformType, String platformUserId);
}
