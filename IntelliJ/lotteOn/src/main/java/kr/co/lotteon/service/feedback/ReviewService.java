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
            String uid = tuple.get(1, String.class);

            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            reviewDTO.setUid(uid);

            return reviewDTO;
        }).toList();

        int total = (int) pageReview.getTotalElements();

        double setAvgRate = calculateAvgRate(new PageResponseDTO<>(pageRequestDTO, reviewDTOList, total));

        PageResponseDTO<ReviewDTO> responseDTO = PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewDTOList)
                .total(total)
                .build();

        // 평균 평점도 반환하거나 다른 곳에 사용
        responseDTO.setAvgRate(setAvgRate);

        return responseDTO;
    }

    // 리뷰 평점 계산
    public double calculateAvgRate(PageResponseDTO reviewPageResponseDTO) {
        if (reviewPageResponseDTO != null && reviewPageResponseDTO.getDtoList() != null && !reviewPageResponseDTO.getDtoList().isEmpty()) {
            double avgRating = 0;
            List<ReviewDTO> dtoList = reviewPageResponseDTO.getDtoList();
            for (ReviewDTO review : dtoList) {
                avgRating += review.getRating().doubleValue();
            }
            double average = avgRating / dtoList.size();
            return Math.round(average * 10) / 10.0; // 소수점 첫째 자리까지 반올림
        }
        return 0.0;
    }


}
