package kookmin.software.capstone2023.timebank.presentation.api.configuration;

import kookmin.software.capstone2023.timebank.presentation.api.auth.interceptor.ManagerAuthenticationInterceptor;
import kookmin.software.capstone2023.timebank.presentation.api.auth.interceptor.UserAuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final UserAuthenticationInterceptor userAuthenticationInterceptor;
    private final ManagerAuthenticationInterceptor managerAuthenticationInterceptor;

    public WebMvcConfiguration(
            UserAuthenticationInterceptor userAuthenticationInterceptor,
            ManagerAuthenticationInterceptor managerAuthenticationInterceptor) {
        this.userAuthenticationInterceptor = userAuthenticationInterceptor;
        this.managerAuthenticationInterceptor = managerAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthenticationInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(managerAuthenticationInterceptor)
                .addPathPatterns("/api/**");
    }

    /**
     * CORS 설정
     * <p>
     * 개발 환경이라 전체 허용하지만 실제 서비스에서는 필요한 도메인만 허용하는 것이 좋아요.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
