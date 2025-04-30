package kr.co.lotteon.oauth2;

import kr.co.lotteon.config.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReauthController {

    private final OAuth2Properties oAuth2Properties;


    @GetMapping("/reauth/google")
    public RedirectView reauthGoogle() {
        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + oAuth2Properties.getClientId()
                + "&redirect_uri=" + oAuth2Properties.getRedirectUri()
                + "&response_type=code"
                + "&scope=openid%20email%20profile"
                + "&prompt=login";

        log.info("redirect url : {}", oauthUrl);

        return new RedirectView(oauthUrl);
    }
}