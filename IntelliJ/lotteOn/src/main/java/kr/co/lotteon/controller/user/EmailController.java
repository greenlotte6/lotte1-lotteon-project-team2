package kr.co.lotteon.controller.user;


import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/email/sendCode")
    @ResponseBody
    public String sendEmailCode(@RequestParam("email")  String email, HttpSession session) {

        // 6자리 난수 생성
        String code = String.valueOf(new Random().nextInt(899999) + 100000);

        // 세션에 저장 (key: emailCode)
        session.setAttribute("emailCode", code);


        // 실제 이메일 전송
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("롯데ON 이메일 인증 코드");
            helper.setText("인증 코드: " + code, true); // HTML 가능

            mailSender.send(message);
            return "인증코드가 이메일로 전송되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "이메일 전송에 실패했습니다.";
        }


    }

    @PostMapping("/email/verifyCode")
    @ResponseBody
    public String verifyCode(@RequestParam("inputCode") String inputCode, HttpSession session) {
        String savedCode = (String) session.getAttribute("emailCode");

        if (savedCode != null && savedCode.equals(inputCode)) {
            return "success"; // 인증 성공
        } else {
            return "fail"; // 인증 실패
        }
    }


}
