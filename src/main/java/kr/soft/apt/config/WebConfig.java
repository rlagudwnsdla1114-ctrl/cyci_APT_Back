package kr.soft.apt.config;

import kr.soft.apt.config.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {  // 클래스 이름 첫 글자 대문자 주의


    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:5175", "http://192.168.40.81:5173") // ★ 반드시 명시된 도메인
                .allowedMethods("*")                     // GET, POST 등 모든 메서드 허용
                .allowedHeaders("*")                     // 모든 헤더 허용
                .allowCredentials(true)                  // 쿠키/세션 허용
                .maxAge(3600);                           // preflight 캐시 시간
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")   // ✅ API는 기본적으로 인증 필요
                .excludePathPatterns(
                        // ✅ 로그인/회원가입 (토큰 없을 때 접근해야 함)
                        "/api/jobseeker/login",
                        "/api/jobseeker/signup",
                        "/api/company/login",
                        "/api/company/signup",

                        // ✅ 토큰 재발급/로그아웃 같은 인증 예외가 필요하면 추가
                        "/api/auth/refresh",

                        // ✅ 정적 리소스는 애초에 /api가 아니지만 혹시 몰라 예외로 둬도 됨
                        "/uploads/**",

                        // ✅ CORS preflight(가끔 필요)
                        "/error"
                );
    }
}
