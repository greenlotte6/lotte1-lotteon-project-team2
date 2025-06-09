package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.config.Banner;
import kr.co.lotteon.entity.feedback.Review;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.config.BannerRepository;
import kr.co.lotteon.repository.feedback.ReviewRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductViewService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final InquiryRepository inquiryRepository;
    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;


    // 상품, 판매자(사용자), 메인 카테고리, 서브카테고리, 상품이미지, 상품상세 정보
    public ProductDTO selectProductByProdNo(String prodNo) {
        Optional<Product> optProduct = productRepository.findByProdNo(prodNo);

        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            ProductDTO productDTO =  modelMapper.map(product, ProductDTO.class);
            productDTO = OptionSplit(productDTO);
            return productDTO;
        }
        return null;
    }


    // 상품 옵션 분리
    public ProductDTO OptionSplit(ProductDTO productDTO) {
        String prodNo = productDTO.getProdNo();
        Product product = productRepository.findById(prodNo).orElse(null);

        ProductDTO productDTO1 = modelMapper.map(product, ProductDTO.class);

        String[] option = new String[6];
        String[][] str = new String[6][10];

        if (productDTO1.getProductDetail() != null) {

            // 첫 번째 옵션
            if (productDTO1.getProductDetail().getOpt1() != null) {
                option[0] = productDTO1.getProductDetail().getOpt1();
                String[] optList = productDTO1.getProductDetail().getOpt1Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[0][i] = optList[i];
                }
            }

            // 두 번째 옵션
            if (productDTO1.getProductDetail().getOpt2() != null) {
                option[1] = productDTO1.getProductDetail().getOpt2();
                String[] optList = productDTO1.getProductDetail().getOpt2Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[1][i] = optList[i];
                }
            }

            // 세 번째 옵션
            if (productDTO1.getProductDetail().getOpt3() != null) {
                option[2] = productDTO1.getProductDetail().getOpt3();
                String[] optList = productDTO1.getProductDetail().getOpt3Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[2][i] = optList[i];
                }
            }

            // 네 번째 옵션
            if (productDTO1.getProductDetail().getOpt4() != null) {
                option[3] = productDTO1.getProductDetail().getOpt4();
                String[] optList = productDTO1.getProductDetail().getOpt4Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[3][i] = optList[i];
                }
            }

            // 다섯 번째 옵션
            if (productDTO1.getProductDetail().getOpt5() != null) {
                option[4] = productDTO1.getProductDetail().getOpt5();
                String[] optList = productDTO1.getProductDetail().getOpt5Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[4][i] = optList[i];
                }
            }

            // 여섯 번째 옵션
            if (productDTO1.getProductDetail().getOpt6() != null) {
                option[5] = productDTO1.getProductDetail().getOpt6();
                String[] optList = productDTO1.getProductDetail().getOpt6Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[5][i] = optList[i];
                }
            }

            productDTO.setOption(option);
            productDTO.setOptions(str);
            return productDTO;
        }
        return productDTO;
    }


    // 광고 정보
    @Cacheable(value = "slide-banners", key = "#cate")
    public List<BannerDTO> findBanner(String cate) {
        List<Banner> bannerList = bannerRepository.findBannerByCate(cate);

        List<BannerDTO> bannerDTOList = new ArrayList<>();
        for(Banner banner : bannerList){
            bannerDTOList.add(modelMapper.map(banner, BannerDTO.class));
        }

        return bannerDTOList;
    }


    // 활성화된 광고들 중 무작위 선택
    public BannerDTO randomBanner(List<BannerDTO> banners) {
        List<BannerDTO> activeBanners = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for(BannerDTO banner : banners){
            LocalDateTime startDateTime = LocalDateTime.of(banner.getStartDay(), banner.getStartTime());
            LocalDateTime endDateTime = LocalDateTime.of(banner.getEndDay(), banner.getEndTime());

            if (startDateTime.isBefore(now) && endDateTime.isAfter(now)) {
                activeBanners.add(banner);
            }
        }

        if(activeBanners.isEmpty()){
            return null;
        }else{
            int size = banners.size();
            int random = (int) (Math.random() * size);
            return activeBanners.get(random);
        }
    }


    // 리뷰 정보
    public PageResponseDTO selectAllForReview(PageRequestDTO pageRequestDTO) {
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


    // 문의 정보
    public PageResponseDTO selectAllForQna(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("prodNo");
        Page<Tuple> pageInquiry = inquiryRepository.selectAllForList(pageRequestDTO, pageable);

        List<InquiryDTO> inquiryDTOList = pageInquiry.getContent().stream().map(tuple -> {
            Inquiry inquiry = tuple.get(0, Inquiry.class);
            String uid = tuple.get(1, String.class);
            InquiryDTO inquiryDTO = modelMapper.map(inquiry, InquiryDTO.class);
            inquiryDTO.setUid(uid);
            return inquiryDTO;
        }).toList();

        int total = (int) pageInquiry.getTotalElements();

        return PageResponseDTO.<InquiryDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(inquiryDTOList)
                .total (total)
                .build();
    }


    // 조회수
    public void hitCountUp(String prodNo) {
        Optional<Product> productOptional = productRepository.findByProdNo(prodNo);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setHit(product.getHit() + 1);
            productRepository.save(product);
        }
    }


}
