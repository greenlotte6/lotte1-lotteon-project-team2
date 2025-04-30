package kr.co.lotteon.config;


import kr.co.lotteon.interceptor.AppInfoInterceptor;
import kr.co.lotteon.interceptor.AutoLoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본 static 경로 설정 (예: /static/ 경로로 접근할 수 있도록 설정)
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:uploads/");
    }


    private final AppInfo appInfo;
    private final AutoLoginInterceptor autoLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo));

        // ✅ 자동 로그인 인터셉터 등록
        registry.addInterceptor(autoLoginInterceptor)
                .addPathPatterns("/**") // 전역 적용
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/upload/**");
    }






}


