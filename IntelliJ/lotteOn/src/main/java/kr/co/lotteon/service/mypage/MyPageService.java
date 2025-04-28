package kr.co.lotteon.service.mypage;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public PageResponseDTO<InquiryDTO> inquiryFindAll(User writer, PageRequestDTO pageRequestDTO){

        // 1. Pageable 객체 생성 (정렬 기준: "no")
        Pageable pageable = pageRequestDTO.getPageable("no");

        // 2. Inquiry 엔티티를 페이징하여 조회
        Page<Inquiry> pageInquiry = inquiryRepository.findAllByUser(writer, pageable);

        // 3. Inquiry 엔티티 리스트를 InquiryDTO 리스트로 변환
        List<InquiryDTO> inquiryDTOList = pageInquiry.getContent().stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class))
                .collect(Collectors.toList());

        // 4. 전체 데이터 개수 구하기
        int total = (int) pageInquiry.getTotalElements();

        // 5. PageResponseDTO 객체 생성 및 반환
        return PageResponseDTO.<InquiryDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(inquiryDTOList)
                .total(total)
                .build();
    }


    public UserDTO findByUid(String uid){

        Optional<User> optUser = userRepository.findByUid(uid);

        if(optUser.isPresent()){
            User user = optUser.get();
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return userDTO;
        }

        return null;



    }


}
