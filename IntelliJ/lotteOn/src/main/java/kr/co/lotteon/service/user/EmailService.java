package kr.co.lotteon.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private String createAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public String sendAuthCode(String toEmail) {
        String authCode = createAuthCode();

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("[LotteOn] 이메일 인증 코드입니다.");
            helper.setText("<h1>인증 코드: " + authCode + "</h1>", true);

            mailSender.send(message);
            log.info("이메일 전송 완료: {}", toEmail);
            return authCode;
        } catch (MessagingException e) {
            log.error("이메일 전송 실패", e);
            return null;
        }
    }
}