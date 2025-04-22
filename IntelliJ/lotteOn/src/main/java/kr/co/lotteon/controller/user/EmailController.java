package kr.co.lotteon.controller.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final JavaMailSender mailSender;

    @PostMapping("/email/sendCode")
    public ResponseEntity<String> sendEmailCode(@RequestParam String email, HttpSession session) {
        // 6자리 인증코드 생성
        String code = String.valueOf(new Random().nextInt(899999) + 100000);

        // 메일 내용 작성
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[LotteOn] 이메일 인증코드입니다.");
        message.setText("안녕하세요.\n요청하신 인증코드는 다음과 같습니다.\n\n✔ 인증코드: " + code + "\n\n감사합니다.");

        try {
            mailSender.send(message);
            session.setAttribute("emailCode", code);
            return ResponseEntity.ok("인증코드가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패!");
        }
    }

    @PostMapping("/email/verifyCode")
    public ResponseEntity<String> verifyEmailCode(@RequestParam String code, HttpSession session) {
        String savedCode = (String) session.getAttribute("emailCode");

        if (savedCode != null && savedCode.equals(code)) {
            return ResponseEntity.ok("인증 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패! 코드가 일치하지 않습니다.");
        }
    }




}