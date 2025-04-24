package kr.co.lotteon.service.product;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dao.ProductMapper;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.page.PageViewDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.service.article.InquiryService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.feedback.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final CouponService couponService;
    private final ModelMapper modelMapper;

    // 상품 view 한번에 묶기
    public PageViewDTO getPageViewDTO(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();
        // 상품 + 상품이미지
        ProductDTO productDTO = productMapper.selectProductByProdNo(prodNo);
        // 쿠폰
        List<CouponDTO> couponDTOList = couponService.findAllByCompany(productDTO.getCompany());
        // 상품 상세
        ProductDetailDTO productDetailDTO = productDetailService.findByProdNo(pageRequestDTO);
        // 리뷰
        PageResponseDTO reviewPageResponseDTO = reviewService.selectAllForList(pageRequestDTO);
        //PageResponseDTO inquiryPageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);

        log.info("reviewPageResponseDTO: {}", reviewPageResponseDTO);

        return new PageViewDTO(
                productDTO,
                couponDTOList,
                productDetailDTO,
                reviewPageResponseDTO
                //inquiryPageResponseDTO
        );
    }


    // 글 조회수
    @Transactional
    public void increaseHit(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();
        Product product = productRepository.findByProdNo(prodNo)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        product.setHit(product.getHit() + 1);
    }

}

