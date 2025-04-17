package kr.co.lotteon.config;


import kr.co.lotteon.interceptor.AppInfoInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {


/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본 static 경로 설정 (예: /static/ 경로로 접근할 수 있도록 설정)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
*/

    private final AppInfo appInfo;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoInterceptor(appInfo));
    }

}