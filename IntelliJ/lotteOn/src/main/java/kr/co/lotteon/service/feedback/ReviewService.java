package kr.co.lotteon.service.feedback;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.entity.feedback.Review;
import kr.co.lotteon.repository.feedback.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    // 리뷰 + 유저 + 상품
    public PageResponseDTO selectAllForList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("prodNo");

        Page<Tuple> pageReview = reviewRepository.selectAllForList(pageRequestDTO, pageable);

        List<ReviewDTO> reviewDTOList = pageReview.getContent().stream().map(tuple -> {
            Review review = tuple.get(0, Review.class);
            String company = tuple.get(1, String.class);
            String uid = tuple.get(2,  String.class);

            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            reviewDTO.setCompany(company);
            reviewDTO.setUid(uid);

            return reviewDTO;
        }).toList();

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewDTOList)
                .total(total)
                .build();
    }






}
