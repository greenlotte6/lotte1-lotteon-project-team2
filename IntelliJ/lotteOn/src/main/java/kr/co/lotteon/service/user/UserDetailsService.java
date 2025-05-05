package kr.co.lotteon.service.user;

import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ModelMapper modelMapper;

    public UserDetailsDTO findByUser (String uid) {
        Optional<User> optUser = userRepository.findByUid(uid);
        if (optUser.isEmpty()) {
            return null;
        }
        User user = optUser.get();

        Optional<UserDetails> optUserDetails = userDetailsRepository.findByUser(user);

        if (optUserDetails.isPresent()) {
            UserDetails userDetails = optUserDetails.get();
            UserDetailsDTO userDetailsDTO = modelMapper.map(userDetails, UserDetailsDTO.class);
            return userDetailsDTO;
        }
        return null;
    }
}
