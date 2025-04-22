package kr.co.lotteon.service.user;


import kr.co.lotteon.entity.user.SignUp;
import kr.co.lotteon.repository.user.SignupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final SignupRepository signupRepository;

    public SignUp getSignUp () {
        return signupRepository.findById(1).orElse(null);
    }
}
