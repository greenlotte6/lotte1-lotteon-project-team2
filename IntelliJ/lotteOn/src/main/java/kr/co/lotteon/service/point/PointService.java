package kr.co.lotteon.service.point;

import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
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
    private final ModelMapper modelMapper;

    public UserDetailsDTO changePoint(int usedPoint, org.springframework.security.core.userdetails.UserDetails userDetails) {
        String uid = userDetails.getUsername();

        User user = User.builder()
                    .uid(uid)
                    .build();

        Optional<UserDetails> optUserDetails = userDetailsRepository.findByUser(user);

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
