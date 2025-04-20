package kr.co.lotteon.repository.user;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserDetailsRepositoryTest {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    /*
    @Test
    void test() {
        User user = userRepository.findById("K_clsrntkdgh").get();

        System.out.println(user);



        UserDetails userDetails = UserDetails.builder()
                .userPoint(1000)
                .gender("M")
                .rank("등급")
                .user(user)
                .build();

        userDetailsRepository.save(userDetails);


    }
     */


    /*
    @Test
    void find(){
        UserDetails userDetails = userDetailsRepository.findById(2).get();

        UserDetailsDTO userDetailsDTO = modelMapper.map(userDetails, UserDetailsDTO.class);
        System.out.println(userDetailsDTO);
    }

*/

}