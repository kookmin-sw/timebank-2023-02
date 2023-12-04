package kookmin.software.capstone2023.timebank.application.service.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kookmin.software.capstone2023.timebank.application.configuration.AccessTokenProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AccessTokenService {
    private final AccessTokenProperties accessTokenProperties;

    public AccessTokenService(AccessTokenProperties accessTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
    }

    public String issue(long userId, long accountId, Instant expiresAt) {
        Algorithm algorithm = Algorithm.HMAC256(accessTokenProperties.getSecretKey());

        String token = JWT.create()
                .withClaim("userId", userId)
                .withClaim("accountId", accountId)
                .withExpiresAt(java.util.Date.from(expiresAt))
                .sign(algorithm);

        return token;
    }

    public AccessTokenClaims verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(accessTokenProperties.getSecretKey());

        try {
            DecodedJWT decodedToken = JWT.require(algorithm).build().verify(token);
            long userId = decodedToken.getClaim("userId").asLong();
            long accountId = decodedToken.getClaim("accountId").asLong();
            return new AccessTokenClaims(userId, accountId);
        } catch (Exception e) {
            if (e instanceof AccessTokenException.TokenExpiredException) {
                throw new AccessTokenException.TokenExpiredException();
            } else {
                throw new AccessTokenException.InvalidTokenException();
            }
        }
    }
}
