package kr.co.lotteon.config;


import kr.co.lotteon.interceptor.AppInfoInterceptor;
import kr.co.lotteon.interceptor.VisitorTrackingInterceptor;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppInfo appInfo;
    private final ConfigService configService; // 추가
    private final VisitorService visitorService;




    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본 static 경로 설정 (예: /static/ 경로로 접근할 수 있도록 설정)
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:uploads/");

    }


    @Bean
    public VisitorTrackingInterceptor visitorTrackingInterceptor() {
        return new VisitorTrackingInterceptor(visitorService);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo, configService));


        // 방문자 추적 인터셉터 등록
        registry.addInterceptor(visitorTrackingInterceptor())
                .addPathPatterns("/**") // 모든 경로에 적용
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/upload/**") // 정적 리소스 제외
                .excludePathPatterns("/api/admin/**"); // 관리자 API 제외 (선택사항)

    }

}