package kr.co.lotteon.oauth2;

import kr.co.lotteon.config.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OAuth2CallbackController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OAuth2Properties oAuth2Properties;

    @GetMapping("/oauth2/callback/google")
    public String handleGoogleCallback(@RequestParam("code") String code) {
        // 1. access_token 요청
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", oAuth2Properties.getClientId());
        params.add("client_secret", oAuth2Properties.getClientSecret());
        params.add("redirect_uri", oAuth2Properties.getRedirectUri());
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        String accessToken = (String) response.getBody().get("access_token");

        // 2. 사용자 정보 요청
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userInfoRequest,
                Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();
        log.info("Google user info: {}", userInfo);

        // 3. 여기서 사용자 정보 확인 후 인증 처리 또는 인증 완료 플래그 설정 가능
        // 세션에 인증 완료 상태 저장 등

        return "redirect:/my/info";
    }
}
