package kr.soft.apt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {  // 클래스 이름 첫 글자 대문자 주의


//    @Autowired
//    private AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // ★ 반드시 명시된 도메인
                .allowedMethods("*")                     // GET, POST 등 모든 메서드 허용
                .allowedHeaders("*")                     // 모든 헤더 허용
                .allowCredentials(true)                  // 쿠키/세션 허용
                .maxAge(3600);                           // preflight 캐시 시간
    }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/api/**")
//                .excludePathPatterns(
//                        "/api/member/**",
//                        "/api/board/**"
//                );
//    }
}
