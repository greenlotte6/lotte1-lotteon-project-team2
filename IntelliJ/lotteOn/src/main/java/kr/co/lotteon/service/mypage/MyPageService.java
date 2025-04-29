package kr.co.lotteon.service.mypage;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.feedback.Review;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.feedback.ReviewRepository;
import kr.co.lotteon.repository.point.PointRepository;
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
    private final ReviewRepository reviewRepository;
    private final PointRepository pointRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public PageResponseDTO<InquiryDTO> inquiryFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO){

        // 1. UserDTO → User Entity 변환
        User user = modelMapper.map(userDTO, User.class);

        // 2. Pageable 객체 생성 (정렬 기준: "no")
        Pageable pageable = pageRequestDTO.getPageable("no");

        // 3. Inquiry 엔티티를 페이징하여 조회
        Page<Inquiry> pageInquiry = inquiryRepository.findAllByUser(user, pageable);

        // 4. Inquiry 엔티티 리스트를 InquiryDTO 리스트로 변환
        List<InquiryDTO> inquiryDTOList = pageInquiry.getContent().stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class))
                .collect(Collectors.toList());

        // 5. 전체 데이터 개수 구하기
        int total = (int) pageInquiry.getTotalElements();

        // 6. PageResponseDTO 객체 생성 및 반환
        return PageResponseDTO.<InquiryDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(inquiryDTOList)
                .total(total)
                .build();
    }


    public PageResponseDTO<ReviewDTO> reviewFindAll(String writer, PageRequestDTO pageRequestDTO){

        Optional<User> optUser = userRepository.findByUid(writer);
        User user = optUser.get();

        Pageable pageable = pageRequestDTO.getPageable("rno");

        Page<Review> pageReview = reviewRepository.findAllByWriter(user, pageable);

        List<ReviewDTO> reviewList = pageReview.getContent().stream()
                .map(review ->modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();

    }

    public PageResponseDTO<PointDTO> pointFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO){

        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("pointNo");

        Page<Point> pagePoint = pointRepository.findAllByUser(user, pageable);

        List<PointDTO> pointDTOList = pagePoint.getContent().stream()
                .map(point -> modelMapper.map(point, PointDTO.class))
                .collect(Collectors.toList());

        int total = (int) pagePoint.getTotalElements();

        return PageResponseDTO.<PointDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(pointDTOList)
                .total(total)
                .build();
        /*
        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("pointNo");

        Page<Point> pagePoint = pointRepository.findAllByUid(user, pageable);

        List<PointDTO> pointList = pagePoint.getContent().stream()
                .map(point ->modelMapper.map(point, PointDTO.class))
                .collect(Collectors.toList());

        int total = (int) pagePoint.getTotalElements();

        return PageResponseDTO.<PointDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(pointList)
                .total(total)
                .build();
*/
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
