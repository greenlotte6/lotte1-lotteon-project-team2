package kr.co.lotteon.service.user;


import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

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


}
