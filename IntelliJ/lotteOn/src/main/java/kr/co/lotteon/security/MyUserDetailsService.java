package kr.co.lotteon.security;


import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 사용자 조회
        Optional<User> optUser = userRepository.findById(username);

        if(optUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        User user = optUser.get();

        // 탈퇴한 회원 체크
        if(user.getLeaveDate() != null) {
            throw new DisabledException("탈퇴한 회원입니다.");
        }

        // Security 사용자 인증객체 생성
        MyUserDetails myUserDetails = MyUserDetails.builder()
                .user(user)
                .build();

        // 리턴되는 myUserDetails는 Security ContextHolder에 저장
        return myUserDetails;
    }
}