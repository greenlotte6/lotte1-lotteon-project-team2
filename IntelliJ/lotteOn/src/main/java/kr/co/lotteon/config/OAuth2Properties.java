package kr.co.lotteon.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class OAuth2Properties {

    private String clientId;
    private String redirectUri;
    private String clientSecret;

}