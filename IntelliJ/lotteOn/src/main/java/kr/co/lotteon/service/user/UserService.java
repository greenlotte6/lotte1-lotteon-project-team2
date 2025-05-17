package kr.co.lotteon.service.user;


import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.service.point.PointService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final PointRepository pointRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final CouponIssueRepository couponIssueRepository;

    public boolean checkUid(String uid) {
        return userRepository.existsByUid(uid);
    }

    public void register(UserDTO userdto) {
        User user = User.builder()
                .uid(userdto.getUid())
                .pass(passwordEncoder.encode(userdto.getPass()))
                .name(userdto.getName())
                .email(userdto.getEmail())
                .hp(userdto.getHp())
                .zip(userdto.getZip())
                .addr1(userdto.getAddr1())
                .addr2(userdto.getAddr2())
                .role("USER")
                .state("정상")
                .build();

        userRepository.save(user);

        // 유저 상세정보
        UserDetails userDetails = UserDetails.builder()
                .gender(userdto.getGender())
                .userPoint(5000)
                .user(user)
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

    }

    public UserDTO findById(String uid){
        Optional<User> optUser = userRepository.findById(uid);

        if(optUser.isPresent()){
            User user = optUser.get();
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return userDTO;
        }

        return null;
    }

    public Optional<User> findByNameAndHp(String name, String hp) {
        return userRepository.findByNameAndHp(name, hp);
    }

// UserService.java

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    public Optional<User> findByUidAndPass(String uid, String rawPass) {
        Optional<User> optUser = userRepository.findById(uid);

        if (optUser.isPresent()) {
            User user = optUser.get();
            if (passwordEncoder.matches(rawPass, user.getPass())) {
                return optUser;
            }
        }
        return Optional.empty();
    }




}
