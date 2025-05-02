package kr.co.lotteon.config;


import kr.co.lotteon.interceptor.AppInfoInterceptor;
import kr.co.lotteon.service.config.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppInfo appInfo;
    private final ConfigService configService; // 추가

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본 static 경로 설정 (예: /static/ 경로로 접근할 수 있도록 설정)
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:uploads/");
    }




    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo, configService));

    }

}