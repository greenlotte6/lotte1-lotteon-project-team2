package kr.co.lotteon.service.point;

import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {

    private final UserDetailsRepository userDetailsRepository;
    private final PointRepository pointRepository;
    private final ModelMapper modelMapper;

    public UserDetailsDTO changePoint(Integer usedPoint, org.springframework.security.core.userdetails.UserDetails userDetails, int orderNo) {

        if (usedPoint == null || usedPoint == 0) {
            return null;
        }

        String uid = userDetails.getUsername();

        User user = User.builder()
                    .uid(uid)
                    .build();

        Optional<UserDetails> optUserDetails = userDetailsRepository.findByUser(user);

        Point point = Point.builder()
                .point(usedPoint * (-1))
                .user(user)
                .pointDesc("상품 주문(주문 번호: " + orderNo + ") 차감")
                .expiryDate(null)
                .build();

        pointRepository.save(point);

        if (optUserDetails.isPresent()) {
            UserDetails userDetail = optUserDetails.get();
            userDetail.setUserPoint(userDetail.getUserPoint() - usedPoint);
            userDetailsRepository.save(userDetail);
            UserDetailsDTO userDetailsDTO = modelMapper.map(userDetail, UserDetailsDTO.class);
            return userDetailsDTO;
        }
        return null;
    }


}
