package kr.co.lotteon.service.admin;

import ch.qos.logback.core.model.Model;
import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.article.Notice;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductDetail;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.NoticeRepository;
import kr.co.lotteon.repository.category.SubCategoryRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductDetailRepository;
import kr.co.lotteon.repository.product.ProductImageRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.seller.SellerRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class adminService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CartRepository cartRepository;
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    private final ModelMapper modelMapper;


    /*
     * 관리자 페이지 (상품 등록 메서드)
     * */
    public Product saveProduct(ProductDTO productDTO) {

        // 판매자, 서브카테고리 호출
        Seller seller = sellerRepository.findByCompany(productDTO.getCompany());
        SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCateNo()).get();

        Product product = modelMapper.map(productDTO, Product.class);

        product.setSeller(seller);
        product.setSubCategory(subCategory);

        // 제품 번호를 위한 준비
        // 년도 + 메인카테고리 번호 + 서브카테고리 번호
        String subNo = String.valueOf(subCategory.getSubCateNo());
        if(subNo.length()==1){
            subNo = "0" + subNo;
        }

        String mainNo = String.valueOf(subCategory.getMainCategory().getMainCateNo());
        if(mainNo.length()==1){
            mainNo = "0" + mainNo;
        }
        String year = String.valueOf(LocalDate.now().getYear());

        long prodNo = Long.parseLong(year + mainNo + subNo + "001");

        //findByProdNoStartingWith
        while(true){

            String select = String.valueOf(prodNo);
            List<Product> optionalProduct = productRepository.findByProdNoStartingWith(select);
            if(optionalProduct.size()>0){
                prodNo += 1;
            }

            if(optionalProduct.size()==0){
                product.setProdNo(String.valueOf(prodNo));
                return productRepository.save(product);
            }
        }


    }

    /*
     * 제품 상세 정보 저장 메서드
     * */
    public void saveProductDetail(ProductDetailDTO productDetailDTO, Product savedProduct) {

        ProductDetail productDetail = modelMapper.map(productDetailDTO, ProductDetail.class);
        productDetail.setProduct(savedProduct);
        productDetailRepository.save(productDetail);
    }

    public PageResponseDTO selectAllForList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageProduct = productRepository.selectAllForListByRole(pageRequestDTO, pageable);

        List<ProductDTO> productDTOList = pageProduct.getContent().stream().map(tuple -> {
            Product product = tuple.get(0, Product.class);
            String company = tuple.get(1,  String.class);
            ProductImage productImage = tuple.get(2,  ProductImage.class);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setCompany(company);

            ProductImageDTO productImageDTO = modelMapper.map(productImage, ProductImageDTO.class);
            productDTO.setProductImageDTO(productImageDTO);

            return productDTO;
        }).toList();

        int total = (int) pageProduct.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", productDTOList);

        return PageResponseDTO.<ProductDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(productDTOList)
                .total(total)
                .build();

    }

    /*
     * 제품 삭제
     * */

    @Transactional
    public void deleteProduct(String no) {

        // 제품 엔티티 출력
        Optional<Product> optProduct = productRepository.findById(no);

        /*
         * 상품을 삭제할 때
         * */
        if(optProduct.isPresent()){
            Product product = optProduct.get();

            productDetailRepository.deleteByProduct(product);
            productImageRepository.deleteByProduct(product);
            cartRepository.deleteByProduct(product);
            productRepository.deleteById(no);
        }


    }

    
    /*
    * 공지사항 부분
    * */
    
    // 공지사항 저장
    public void saveNotice(NoticeDTO noticeDTO, UserDetails userDetails) {

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        Notice notice = modelMapper.map(noticeDTO, Notice.class);
        notice.setUser(user);

        noticeRepository.save(notice);

    }

    // 공지사항 목록 페이징
    public PageResponseDTO findAllNotice(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        if(pageRequestDTO.getSearchType()==null){
            pageRequestDTO.setSearchType("전체");
        }

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = noticeRepository.selectAllNotice(pageRequestDTO, pageable);

        List<NoticeDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Notice notice = tuple.get(0, Notice.class);
            return modelMapper.map(notice, NoticeDTO.class);
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", pageObject);

        return PageResponseDTO.<NoticeDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();


    }

    // 공지사항 삭제
    public void deleteNoticeByNo(String no) {
        noticeRepository.deleteById(Integer.parseInt(no));
    }

    // 공지사항 데이터 찾기
    public NoticeDTO findNoticeByNo(String no) {
        int pk = Integer.parseInt(no);
        Optional<Notice> noticeOpt = noticeRepository.findById(pk);

        if(noticeOpt.isPresent()){
            Notice notice = noticeOpt.get();
            NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);
            return noticeDTO;
        }

        return null;

    }

    // 공지사항 수정하기
    public void modify(NoticeDTO noticeDTO) {
        Optional<Notice> noticeOpt = noticeRepository.findById(noticeDTO.getNo());
        if(noticeOpt.isPresent()){
            Notice notice = noticeOpt.get();
            notice.setCate(noticeDTO.getCate());
            notice.setTitle(noticeDTO.getTitle());
            notice.setContent(noticeDTO.getContent());
            noticeRepository.save(notice);
        }

    }

    public void deleteNoticeByList(List<Integer> deleteNos) {
        for( int i : deleteNos){
            noticeRepository.deleteById(i);
        }
    }


    /*
    * 쿠폰
    * */
    // 쿠폰 등록
    public void saveCoupon(CouponDTO couponDTO, UserDetails userDetails) {
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);

        Optional<User> userOtp = userRepository.findById(userDetails.getUsername());

        if(userOtp.isPresent()){
            User user = userOtp.get();
            coupon.setUser(user);
            coupon.setIssuedBy(user.getName());

            // 쿠폰 이름 : 내용 / (유저이름)
            // 예) 삼성 7% 할인 / (관리자)
            String couponName = coupon.getCouponName() + " / " + couponDTO.getIssuedBy();
            coupon.setCouponName(couponName);

            couponRepository.save(coupon);


        }


    }

    /*
    * 쿠폰 리스트
    * */
    public PageResponseDTO selectAllForCoupon(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        if(pageRequestDTO.getSearchType()==null){
            pageRequestDTO.setSearchType("전체");
        }

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = couponRepository.selectAllCoupon(pageRequestDTO, pageable);

        List<CouponDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Coupon coupon = tuple.get(0, Coupon.class);
            if(coupon.getCouponName().contains("/")){
                String name = coupon.getCouponName().split("/")[0];
                coupon.setCouponName(name);
            }
            return modelMapper.map(coupon, CouponDTO.class);
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", pageObject);

        return PageResponseDTO.<CouponDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
        
    }

    // 쿠폰 만료
    public void ExpiryCoupon(Long cno) {
        Optional<Coupon> couponOpt = couponRepository.findById(cno);
        if(couponOpt.isPresent()){
            Coupon coupon = couponOpt.get();
            coupon.setState("종료");
            couponRepository.save(coupon);
        }
    }
}
