package kr.co.lotteon.oauth2;


import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final PointRepository pointRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth 인층 업체(구글, 네이터, 카카오) 유저 정보 객체 반환
        log.info("userRequest={}", userRequest);

        String accessToken = userRequest.getClientRegistration().getClientId() + ":" + userRequest.getClientRegistration().getClientSecret();
        log.info("accessToken={}", accessToken);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        log.info("provider={}", provider);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User={}", oAuth2User);

        Map<String, Object> attrs = oAuth2User.getAttributes();

        String email;
        String name;

        if(provider.equals("kakao")){
            Map<String, Object> kakaoAccount = (Map<String, Object>) attrs.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
        }else if(provider.equals("naver")){
            Map<String, Object> response = (Map<String, Object>) attrs.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
        }else{
            // 구글
            email = (String) attrs.get("email");
            name = (String) attrs.get("name");
            log.info("email={}", email);
        }

        log.info("▶ 추출된 사용자 정보: email={}, name={}", email, name);

        String uid = email.substring(0, email.lastIndexOf("@"));

        if(provider.equals("kakao")){
            uid = "K_" + uid;
        }else if(provider.equals("naver")){
            uid = "N_" + uid;
        }else{
            uid = "G_" + uid;
        }
        log.info("▶ 생성된 uid={}", uid);
        // 회원 테이블에서 사용자 확인
        Optional<User> optUser = userRepository.findById(uid);
        log.info("▶ DB 조회 결과: 존재함? {}", optUser.isPresent());
        User user = null;
        if(optUser.isPresent()){
            // 회원이 존재하지 않으면 정보를 저장
            user = optUser.get();
            log.info("▶ 기존 사용자 로그인: {}", user.getUid());
        }else{
            user = User.builder()
                    .uid(uid)
                    .name(name)
                    .email(email)
                    .role("USER")
                    .state("정상")
                    .provider(provider)
                    .build();
            log.info("▶ 신규 사용자 저장 시도: {}", user);
            userRepository.save(user);

            UserDetails userDetails = UserDetails.builder()
                    .user(user)
                    .userPoint(5000)
                    .gender("male")
                    .rank("FAMILY")
                    .build();

            userDetailsRepository.save(userDetails);

            LocalDateTime now = LocalDateTime.now();

            // 포인트 기록
            Point point = Point.builder()
                    .expiryDate(now.plusMonths(6))
                    .pointDesc("회원가입 축하포인트!")
                    .point(5000)
                    .user(user)
                    .build();

            pointRepository.save(point);

            // 쿠폰 발급
            Coupon coupon = Coupon.builder()
                    .cno(1012211368)
                    .build();

            String expire = String.valueOf(now.plusMonths(1));

            // 쿠폰 이슈
            CouponIssue couponIssue = CouponIssue.builder()
                    .user(user)
                    .coupon(coupon)
                    .issuedBy("관리자")
                    .validTo(expire)
                    .build();

            couponIssueRepository.save(couponIssue);



            log.info("▶ 사용자 저장 완료");
        }


        return MyUserDetails.builder()
                .user(user)
                .attributes(attrs)
                .accessToken(accessToken)
                .build();

    }

}
