package kookmin.software.capstone2023.timebank.presentation.api.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.application.service.auth.UserAuthenticator;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.ManagerAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ManagerAuthenticationInterceptor implements HandlerInterceptor {
    private final UserAuthenticator userAuthenticator;

    public ManagerAuthenticationInterceptor(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ManagerAuthentication managerAuthentication = handlerMethod.getMethodAnnotation(ManagerAuthentication.class);
        if (managerAuthentication == null) {
            managerAuthentication = handlerMethod.getBeanType().getAnnotation(ManagerAuthentication.class);
            if (managerAuthentication == null) {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        }

        if (request.getAttribute(RequestAttributes.USER_CONTEXT) != null) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String authorizationToken = getAuthorizationToken(request);
        if (authorizationToken == null) {
            throw new UnauthorizedException();
        }

        UserAuthenticator.AuthenticationData authenticationData = userAuthenticator.authenticate(authorizationToken);
        if (authenticationData.getAccountType() != AccountType.BRANCH) {
            throw new UnauthorizedException();
        }

        request.setAttribute(
                RequestAttributes.USER_CONTEXT,
                new UserContext(authenticationData.getUserId(), authenticationData.getAccountId(), authenticationData.getAccountType())
        );

        return true;
    }

    private String getAuthorizationToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        } else {
            return null;
        }
    }

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
}
