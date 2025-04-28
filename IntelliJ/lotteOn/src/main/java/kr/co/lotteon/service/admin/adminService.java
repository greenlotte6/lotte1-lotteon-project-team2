package kr.co.lotteon.service.admin;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.article.FaqDTO;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.article.RecruitDTO;
import kr.co.lotteon.dto.category.MainCategoryDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.article.Faq;
import kr.co.lotteon.entity.article.Notice;
import kr.co.lotteon.entity.article.Recruit;
import kr.co.lotteon.entity.category.MainCategory;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductDetail;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.FaqRepository;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.article.NoticeRepository;
import kr.co.lotteon.repository.article.RecruitRepository;
import kr.co.lotteon.repository.category.MainCategoryRepository;
import kr.co.lotteon.repository.category.SubCategoryRepository;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class adminService {

    private final ModelMapper modelMapper;

    // 판매자, 유저
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    // 상품
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;

    // 카테고리
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    //쿠폰
    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;

    //장바구니
    private final CartRepository cartRepository;
    
    // 게시판
    private final NoticeRepository noticeRepository;
    private final RecruitRepository recruitRepository;
    private final FaqRepository faqRepository;
    private final InquiryRepository inquiryRepository;
    
    /*
     * 관리자 페이지 (상품 등록 메서드)
     * */
    public Product saveProduct(ProductDTO productDTO) {

        // 판매자, 서브카테고리 호출
        Seller seller = sellerRepository.findByCompany(productDTO.getCompany());
        SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCateNo()).get();
        productDTO.setCompany(seller.getCompany());

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

    // 상품 수정하기
    public Product ModifyProduct(ProductDTO productDTO) {

        String no = productDTO.getProdNo();
        Product product = productRepository.findById(no).get();
        SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCateNo()).get();

        product.setSubCategory(subCategory);
        product.setProdName(productDTO.getProdName());
        product.setProdContent(productDTO.getProdContent());
        product.setCompany(productDTO.getCompany());
        product.setProdBrand(productDTO.getProdBrand());
        product.setProdPrice(productDTO.getProdPrice());
        product.setProdDiscount(productDTO.getProdDiscount());

        // 재고 수량 체크
        product.setProdStock(productDTO.getProdStock());
        product.setProdDeliveryFee(productDTO.getProdDeliveryFee());

        return productRepository.save(product);
    }

    // 상품 상세 정보 수정
    public void modifyProductDetail(Product savedProduct, ProductDetailDTO productDetailDTO) {
        Optional<ProductDetail> productDetailOpt = productDetailRepository.findByProduct_ProdNo(savedProduct.getProdNo());
        if(productDetailOpt.isPresent()){
            ProductDetail productDetail = productDetailOpt.get();

            // 상품 선택정보 수정
            productDetail.setOpt1(productDetailDTO.getOpt1());
            productDetail.setOpt2(productDetailDTO.getOpt2());
            productDetail.setOpt3(productDetailDTO.getOpt3());
            productDetail.setOpt4(productDetailDTO.getOpt4());
            productDetail.setOpt5(productDetailDTO.getOpt5());
            productDetail.setOpt6(productDetailDTO.getOpt6());
            productDetail.setOpt1Cont(productDetailDTO.getOpt1Cont());
            productDetail.setOpt2Cont(productDetailDTO.getOpt2Cont());
            productDetail.setOpt3Cont(productDetailDTO.getOpt3Cont());
            productDetail.setOpt4Cont(productDetailDTO.getOpt4Cont());
            productDetail.setOpt5Cont(productDetailDTO.getOpt5Cont());
            productDetail.setOpt6Cont(productDetailDTO.getOpt6Cont());

            //상품정보 제공고시 수정
            productDetail.setProdState(productDetailDTO.getProdState());
            productDetail.setTaxFree(productDetailDTO.getTaxFree());
            productDetail.setReceiptType(productDetailDTO.getReceiptType());
            productDetail.setBizType(productDetailDTO.getBizType());
            productDetail.setOrigin(productDetailDTO.getOrigin());
            productDetailRepository.save(productDetail);
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

        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageProduct = productRepository.selectAllForListByRole(pageRequestDTO, pageable);

        List<ProductDTO> productDTOList = pageProduct.getContent().stream().map(tuple -> {
            Product product = tuple.get(0, Product.class);
            String company = tuple.get(1,  String.class);
            ProductImage productImage = tuple.get(2,  ProductImage.class);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setCompany(company);

            if(productImage!=null){
                ProductImageDTO productImageDTO = modelMapper.map(productImage, ProductImageDTO.class);
                productDTO.setProductImage(productImageDTO);
            }

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

    // 발급된 쿠폰 리스트 출력
    public PageResponseDTO selectAllForIssuedCoupon(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = couponIssueRepository.selectAllCouponIssue(pageRequestDTO, pageable);

        List<CouponIssueDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            CouponIssue couponIssue = tuple.get(0, CouponIssue.class);
            Coupon coupon = tuple.get(1, Coupon.class);
            String uid = tuple.get(2, String.class);

            String couponName = coupon.getCouponName().split("/")[0];
            coupon.setCouponName(couponName);
            CouponIssueDTO couponIssueDTO = modelMapper.map(couponIssue, CouponIssueDTO.class);
            CouponDTO couponDTO = modelMapper.map(coupon, CouponDTO.class);
            couponIssueDTO.setCoupon(couponDTO);
            couponIssueDTO.setUid(uid);
            return couponIssueDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("DTOList: {}", pageObject);

        return PageResponseDTO.<CouponIssueDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
    }

    // 발급된 쿠폰 삭제하기
    public void ExpiryCouponIssue(Long issueNo) {
        Optional<CouponIssue>  couponIssueOpt = couponIssueRepository.findById(issueNo);
        if(couponIssueOpt.isPresent()){
            CouponIssue couponIssue = couponIssueOpt.get();
            couponIssue.setState("중단");
            couponIssue.setUsedDate(LocalDate.now());
            couponIssueRepository.save(couponIssue);
        }
    }

    // 상품 수정을 위한 상품번호에 따른 값 찾기
    public ProductDTO findProductByNo(String no) {
        Optional<Product> productOpt = productRepository.findById(no);

        if(productOpt.isPresent()){
            Product product = productOpt.get();
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            return productDTO;
        }

        return null;
    }

    public List<MainCategoryDTO> findAllMainCategory() {
        List<MainCategory> list = mainCategoryRepository.findAll();
        List<MainCategoryDTO> mainCategoryDTOS = new ArrayList<>();
        for (MainCategory mainCategory : list) {
            MainCategoryDTO mainCategoryDTO = modelMapper.map(mainCategory, MainCategoryDTO.class);
            mainCategoryDTOS.add(mainCategoryDTO);
        }
        return mainCategoryDTOS;
    }


    // 채용하기
    public void saveRecruit(RecruitDTO recruitDTO, UserDetails userDetails) {
        Recruit recruit = modelMapper.map(recruitDTO, Recruit.class);
        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        recruit.setUser(user);
        recruitRepository.save(recruit);
    }


    public PageResponseDTO findAllRecruit(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = recruitRepository.selectAllRecruit(pageRequestDTO, pageable);

        List<RecruitDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Recruit recruit = tuple.get(0, Recruit.class);
            return modelMapper.map(recruit, RecruitDTO.class);
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("recruitDTOList: {}", pageObject);

        return PageResponseDTO.<RecruitDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();

    }

    public void deleteRecruitByList(List<Integer> deleteNos) {
        for(int i : deleteNos){
            recruitRepository.deleteById(i);
        }
    }

    // 자주묻는질문 저장
    public void saveFaq(FaqDTO faqDTO) {
        Faq faq = modelMapper.map(faqDTO, Faq.class);
        faqRepository.save(faq);
    }

    // 작성을 위한 10개 제한(자주묻는 질문)
    public Boolean limitFaq(FaqDTO faqDTO) {
        int count = faqRepository.countByCateV2(faqDTO.getCateV2());

        if(count >= 10){
            return false;
        }

        return true;
    }

    // 자주묻는질문 리스트 출력
    public PageResponseDTO findAllFaq(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = faqRepository.selectAllFaq(pageRequestDTO, pageable);

        List<FaqDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Faq faq = tuple.get(0, Faq.class);
            return modelMapper.map(faq, FaqDTO.class);
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("faqDTOList: {}", pageObject);

        return PageResponseDTO.<FaqDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
        
    }

    // 자주묻는 질문 삭제
    public void deleteFaq(List<Integer> deleteNos) {
        for(int i : deleteNos){
            faqRepository.deleteById(i);
        }
    }

    // 자주묻는 질문 찾기
    public FaqDTO findFaqByNo(int no) {
        Optional<Faq> faqOpt = faqRepository.findById(no);
        if(faqOpt.isPresent()){
            Faq faq = faqOpt.get();
            return modelMapper.map(faq, FaqDTO.class);
        }
        return null;
    }

    public void modifyFaq(FaqDTO faqDTO) {
        Optional<Faq> faqOpt = faqRepository.findById(faqDTO.getNo());
        if(faqOpt.isPresent()){
            Faq faq = faqOpt.get();
            faq.setTitle(faqDTO.getTitle());
            faq.setContent(faqDTO.getContent());
            faq.setCateV1(faqDTO.getCateV1());
            faq.setCateV2(faqDTO.getCateV2());
            faqRepository.save(faq);
        }
    }

    public PageResponseDTO findAllFaqByType(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = faqRepository.selectAllFaqByType(pageRequestDTO, pageable);

        List<FaqDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Faq faq = tuple.get(0, Faq.class);
            return modelMapper.map(faq, FaqDTO.class);
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("faqDTOList: {}", pageObject);

        return PageResponseDTO.<FaqDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
    }

    // 문의하기
    public void deleteQnaByList(List<Integer> deleteNos) {
        for(int i : deleteNos){
            inquiryRepository.deleteById(i);
        }
    }
}
