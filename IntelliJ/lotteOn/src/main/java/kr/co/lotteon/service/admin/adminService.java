package kr.co.lotteon.service.admin;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.article.FaqDTO;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.article.RecruitDTO;
import kr.co.lotteon.dto.category.MainCategoryDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.operation.OperationDTO;
import kr.co.lotteon.dto.operation.OrderSummaryDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.dto.seller.SalesDTO;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.article.Faq;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.article.Notice;
import kr.co.lotteon.entity.article.Recruit;
import kr.co.lotteon.entity.category.MainCategory;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.point.Point;
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
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductDetailRepository;
import kr.co.lotteon.repository.product.ProductImageRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.seller.SellerRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class adminService {

    private final ModelMapper modelMapper;

    // 판매자, 유저
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final UserDetailsRepository userDetailsRepository;

    // 상품
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;

    // 주문
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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

    // 포인트
    private final PointRepository pointRepository;

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

            List<CouponIssue> couponIssues = couponIssueRepository.findByCoupon(coupon);
            if(!couponIssues.isEmpty()){
                for(CouponIssue couponIssue : couponIssues){
                    couponIssue.setState("중단");
                    couponIssueRepository.save(couponIssue);
                }
            }
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

    // 문의하기(삭제)
    public void deleteQnaByList(List<Integer> deleteNos) {
        for(int i : deleteNos){
            inquiryRepository.deleteById(i);
        }
    }

    // 문의하기 뷰
    public InquiryDTO findInquiryByNo(int no) {
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(no);
        if(inquiryOpt.isPresent()){
            Inquiry inquiry = inquiryOpt.get();
            return modelMapper.map(inquiry, InquiryDTO.class);
        }
        return null;
    }

    // 문의하기 응답
    public void replyQna(int no, String answer) {
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(no);
        if(inquiryOpt.isPresent()){
            Inquiry inquiry = inquiryOpt.get();
            inquiry.setAnswer(answer);
            inquiry.setState("답변완료");
            inquiryRepository.save(inquiry);
        }
    }

    public PageResponseDTO findAllQnaByType(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = inquiryRepository.selectAllQnaByType(pageRequestDTO, pageable);

        List<InquiryDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Inquiry inquiry = tuple.get(0, Inquiry.class);
            User user = tuple.get(1, User.class);
            InquiryDTO inquiryDTO = modelMapper.map(inquiry, InquiryDTO.class);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            inquiryDTO.setUser(userDTO);
            return inquiryDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("qnaDTOList: {}", pageObject);

        return PageResponseDTO.<InquiryDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
    }

    /*
     * 관리자 상점목록
     * */

    public PageResponseDTO selectAllForSeller(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = sellerRepository.selectAllSellerByType(pageRequestDTO, pageable);

        List<SellerDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Seller seller = tuple.get(0, Seller.class);
            User user = tuple.get(1, User.class);

            SellerDTO sellerDTO = modelMapper.map(seller, SellerDTO.class);

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            String state = userDTO.getState();
            String manage;
            if(state.equals("정상") || state.equals("운영")){
                manage = "중단";
            }else if(state.equals("중단")){
                manage = "재개";
            }else{
                manage = "승인";
            }
            userDTO.setManage(manage);
            sellerDTO.setUser(userDTO);
            return sellerDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("sellerDTOList: {}", pageObject);

        return PageResponseDTO.<SellerDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
    }


    public void modifySellerState(String uid, String state) {
        Optional<User> userOpt = userRepository.findById(uid);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            String manage = user.getState();
            LocalDateTime leave = null;
            if(manage.equals("정상")){
                user.setState("중단");
                leave = LocalDateTime.now();
            }else if(manage.equals("중단")){
                user.setState("재개");
            }else {
                user.setState("정상");
            }

            user.setLeaveDate(leave);

            userRepository.save(user);
        }
    }

    public PageResponseDTO searchAllForSeller(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(10);
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = sellerRepository.selectAllSellerByTypeAndKeyword(pageRequestDTO, pageable);

        List<SellerDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            Seller seller = tuple.get(0, Seller.class);
            User user = tuple.get(1, User.class);

            SellerDTO sellerDTO = modelMapper.map(seller, SellerDTO.class);

            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            String state = userDTO.getState();
            String manage;
            if(state.equals("정상") || state.equals("운영")){
                manage = "중단";
            }else if(state.equals("중단")){
                manage = "재개";
            }else{
                manage = "승인";
            }
            userDTO.setManage(manage);
            sellerDTO.setUser(userDTO);
            return sellerDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("sellerDTOList: {}", pageObject);

        return PageResponseDTO.<SellerDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
    }

    // 판매자 상태를 중단으로 바꾸고
    // 판매자 leaveDate에 값을 넣음으로서 로그인 불가능
    public void deleteShop(List<Integer> deleteNos) {
        if(!deleteNos.isEmpty()){
            for(int sno : deleteNos){
                Optional<Seller> sellerOpt = sellerRepository.findById(sno);
                if(sellerOpt.isPresent()){
                    Seller seller = sellerOpt.get();
                    User user = seller.getUser();
                    user.setState("중단");
                    user.setLastLoginAt(LocalDateTime.now());
                    userRepository.save(user);
                }
            }
        }
    }

    /*
     * 회원목록
     * */
    public PageResponseDTO selectMemberForList(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = userRepository.selectAllUser(pageRequestDTO, pageable);

        List<UserDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            User user = tuple.get(0, User.class);
            UserDTO userDTO  = modelMapper.map(user, UserDTO.class);
            kr.co.lotteon.entity.user.UserDetails userDetails = tuple.get(1, kr.co.lotteon.entity.user.UserDetails.class);
            UserDetailsDTO userDetailsDTO = modelMapper.map(userDetails, UserDetailsDTO.class);
            String gender = "male".equals(userDetailsDTO.getGender()) ? "M" : "F";
            userDetailsDTO.setGender(gender);
            userDTO.setUserDetails(userDetailsDTO);

            return userDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", pageObject);

        return PageResponseDTO.<UserDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();

    }

    // 포인트 목록
    public PageResponseDTO selectPointForList(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = userRepository.selectAllPoint(pageRequestDTO, pageable);

        List<PointDTO> DTOList = pageObject.getContent().stream().map(tuple -> {
            User user = tuple.get(0, User.class);
            UserDTO userDTO  = modelMapper.map(user, UserDTO.class);
            Point point = tuple.get(1, Point.class);
            PointDTO pointDTO = modelMapper.map(point, PointDTO.class);

            Integer sum =  pointRepository.findSumOfFuturePoints(user.getUid(), pointDTO.getPointNo());
            Integer userSum = pointRepository.findTotalPointByUid(user.getUid());

            if(sum == null){
                sum = 0;
            }

            pointDTO.setPointTotal(userSum - sum);

            pointDTO.setUser(userDTO);
            return pointDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", pageObject);

        return PageResponseDTO.<PointDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();
    }

    // 포인트 삭제
    @Transactional
    public void deletePoint(List<Integer> deleteNos) {
        for(int no : deleteNos){
            Point point = pointRepository.findById(no).get();

            int pointNum = point.getPoint();

            // 유저 포인트 총량 계산
            kr.co.lotteon.entity.user.UserDetails userDetails = userDetailsRepository.findByUser(point.getUser()).get();
            int userPoint = userDetails.getUserPoint() - pointNum;
            userDetails.setUserPoint(userPoint);
            userDetailsRepository.save(userDetails);

            pointRepository.deleteById(no);
        }
    }

    // 회원 상태 변경
    public void modifyMemberState(String uid, String state) {
        Optional<User> userOpt = userRepository.findById(uid);
        if(userOpt.isPresent()){
            User user = userOpt.get();

            switch (state){
                case "중지" :{
                    user.setState(state);
                    user.setLeaveDate(LocalDateTime.now());
                    userRepository.save(user);
                    break;}
                case "재개" : {
                    user.setState("정상");
                    user.setLeaveDate(null);
                    userRepository.save(user);
                    break;}
                case "비활성" :{

                    User deactivatedUser = User.builder()
                            .uid(user.getUid())
                            .state("비활성")
                            .leaveDate(LocalDateTime.now())
                            .build();

                    kr.co.lotteon.entity.user.UserDetails userDetails = userDetailsRepository.findByUser(deactivatedUser).get();
                    userDetails.setUserPoint(0);
                    userDetails.setRank(null);
                    userDetails.setGender(null);

                    userRepository.save(deactivatedUser);
                    userDetailsRepository.save(userDetails);
                }


            }
        }

    }

    public boolean modifyUserRank(String uid, String rank) {

        User user = User.builder()
                .uid(uid)
                .build();

        Optional<kr.co.lotteon.entity.user.UserDetails> userOpt = userDetailsRepository.findByUser(user);
        if(userOpt.isPresent()){
            kr.co.lotteon.entity.user.UserDetails userDetails = userOpt.get();
            userDetails.setRank(rank);
            userDetailsRepository.save(userDetails);
            return true;
        }

        return false;

    }

    // 유저 변경하기
    public void modifyMember(UserDTO userDTO, UserDetailsDTO userDetailsDTO) {
        String uid = userDTO.getUid();
        Optional<User> userOpt = userRepository.findById(uid);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setHp(userDTO.getHp());
            user.setAddr1(userDTO.getAddr1());
            user.setAddr2(userDTO.getAddr2());
            user.setZip(userDTO.getZip());
            userRepository.save(user);

            Optional<kr.co.lotteon.entity.user.UserDetails>  userDetailsOpt = userDetailsRepository.findByUser(user);
            if(userDetailsOpt.isPresent()){
                kr.co.lotteon.entity.user.UserDetails userDetails = userDetailsOpt.get();
                userDetails.setContent(userDetailsDTO.getContent());
                userDetails.setGender(userDetailsDTO.getGender());
                userDetailsRepository.save(userDetails);
            }
        }
    }

    // 매출
    public PageResponseDTO selectAllSales(PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = orderRepository.selectAllSales(pageRequestDTO, pageable);

        String sort = pageRequestDTO.getSearchType();

        List<SalesDTO> DTOList = pageObject.getContent().stream().map(tuple -> {

            Seller seller = tuple.get(0, Seller.class);
            Long orderTotal = tuple.get(1, Long.class);
            if(orderTotal == null){
                orderTotal = 0L;
            }

            int sno = seller.getSno();


            if(sort == null || sort.equals("일별")){
                List<Object[]> result  = orderRepository.findOrderStatusCountsBySeller(sno);

                Map<String, Integer> countMap = new HashMap<>();

                for (Object[] row : result) {
                    String status = (String) row[0];
                    Long count = (Long) row[1];
                    countMap.put(status, count.intValue());
                }

                Long confirmTotal = orderRepository.findConfirmedSalesTotalBySeller(sno);

                if(confirmTotal == null){
                    confirmTotal = 0L;
                }

                SalesDTO salesDTO = SalesDTO.builder()
                        .company(seller.getCompany())
                        .bizRegNo(seller.getBizRegNo())
                        .orderTotal(orderTotal)
                        .orderCount(countMap.getOrDefault("입금대기", 0))
                        .creditCount(countMap.getOrDefault("결제완료", 0))
                        .shippingCount(countMap.getOrDefault("배송중", 0))
                        .deliveryCount(countMap.getOrDefault("배송완료", 0))
                        .confirmCount(countMap.getOrDefault("구매확정", 0))
                        .total(confirmTotal)
                        .build();

                return salesDTO;
            }else{

                LocalDateTime term = LocalDateTime.now();
                if(sort.equals("주간")){
                    term = term.minusDays(7);
                }else{
                    term = term.minusMonths(1);
                }

                List<Object[]> result  = orderRepository.findOrderStatusCountsBySellerAndDate(sno, term);

                Map<String, Integer> countMap = new HashMap<>();

                for (Object[] row : result) {
                    String status = (String) row[0];
                    Long count = (Long) row[1];
                    countMap.put(status, count.intValue());
                }

                Long confirmTotal = orderRepository.findConfirmedSalesTotalBySellerAndDate(sno, term);

                if(confirmTotal == null){
                    confirmTotal = 0L;
                }

                SalesDTO salesDTO = SalesDTO.builder()
                        .company(seller.getCompany())
                        .bizRegNo(seller.getBizRegNo())
                        .orderTotal(orderTotal)
                        .orderCount(countMap.getOrDefault("입금대기", 0))
                        .creditCount(countMap.getOrDefault("결제완료", 0))
                        .shippingCount(countMap.getOrDefault("배송중", 0))
                        .deliveryCount(countMap.getOrDefault("배송완료", 0))
                        .confirmCount(countMap.getOrDefault("구매확정", 0))
                        .total(confirmTotal)
                        .build();

                return salesDTO;
            }

        }).toList();

        int total = (int) pageObject.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", pageObject);

        return PageResponseDTO.<SalesDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();


    }

    // 관리자 메인 (공지사항 5개 출력)
    public List<NoticeDTO> NoticeLimit5() {
        List<NoticeDTO> noticeDTOS = noticeRepository.findTop5ByOrderByNoDesc()
                .stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());
        return noticeDTOS;
    }

    // 관리자 메인 (문의사항 5개 출력)
    public List<InquiryDTO> InquiryLimit5() {
        List<InquiryDTO> inquireDTOS = inquiryRepository.findTop5ByOrderByNoDesc()
                .stream()
                .map(inquire -> modelMapper.map(inquire, InquiryDTO.class))
                .collect(Collectors.toList());
        return inquireDTOS;
    }

    // 관리자 메인 문의사항 갯수 출력
    public OperationDTO countInquiry(OperationDTO operationDTO) {

        operationDTO.setInquiryCountTotal(inquiryRepository.count());

        LocalDate now = LocalDate.now();
        operationDTO.setInquiryCountToday(inquiryRepository.countByWdate(now));

        LocalDate yesterday = now.minusDays(1);
        operationDTO.setInquiryCountYesterday(inquiryRepository.countByWdate(yesterday));
        return operationDTO;
    }

    public OperationDTO countMemberRegister(OperationDTO operationDTO) {

        operationDTO.setMemberCountTotal(userRepository.count());

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfYesterDay = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime startOfTomorrow = LocalDate.now().plusDays(1).atStartOfDay();

        long todayJoinCount = userRepository.countByRegDateBetween(startOfToday, startOfTomorrow);
        operationDTO.setMemberCountToday(todayJoinCount);

        long yesterdayJoinCount = userRepository.countByRegDateBetween(startOfYesterDay, startOfToday);
        operationDTO.setMemberCountYesterday(yesterdayJoinCount);

        return operationDTO;
    }

    public OperationDTO countOrder(OperationDTO operationDTO) {

        operationDTO.setOrderCountTotal(orderRepository.count());

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfYesterDay = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime startOfTomorrow = LocalDate.now().plusDays(1).atStartOfDay();

        operationDTO.setOrderCountToday(orderRepository.countByOrderDateBetween(startOfToday, startOfTomorrow));
        operationDTO.setOrderCountYesterday(orderRepository.countByOrderDateBetween(startOfYesterDay, startOfToday));

        operationDTO.setOrderPriceTotal(orderRepository.findTotalOrderPrice());

        Long orderPriceToday = orderRepository.findTotalOrderPriceBetween(startOfToday, startOfTomorrow);
        if(orderPriceToday == null){
            orderPriceToday = 0L;
        }
        operationDTO.setOrderPriceToday(orderPriceToday);

        Long orderPriceYesterday = orderRepository.findTotalOrderPriceBetween(startOfYesterDay, startOfToday);
        if(orderPriceYesterday == null){
            orderPriceYesterday = 0L;
        }
        operationDTO.setOrderPriceYesterday(orderPriceYesterday);

        return operationDTO;
    }

    public OperationDTO countOrderDetail(OperationDTO operationDTO) {

        String state = "입금대기";
        long ready = orderItemRepository.countByOrderStatus(state);

        state = "배송완료";
        long delivery = orderItemRepository.countByOrderStatus(state);

        state = "취소요청";
        long cancel = orderItemRepository.countByOrderStatus(state);

        state = "교환요청";
        long exchange = orderItemRepository.countByOrderStatus(state);

        state = "반품요청";
        long returnCount = orderItemRepository.countByOrderStatus(state);

        operationDTO.setReadyTotal(ready);
        operationDTO.setDeliveryTotal(delivery);
        operationDTO.setCancelTotal(cancel);
        operationDTO.setExchangeTotal(exchange);
        operationDTO.setReturnTotal(returnCount);

        return operationDTO;
    }

    public OperationDTO countProductCategory(OperationDTO operationDTO) {

        List<Object[]> results = orderItemRepository.findTotalPriceGroupByCategory();

        int num = 1;
        operationDTO.setSale4("기타");

        for (Object[] row : results) {
            String category = (String) row[0];
            Double totalDiscountedPrice = (Double) row[1];
            long totalPrice = totalDiscountedPrice != null ? totalDiscountedPrice.longValue() : 0L;

            System.out.println(category);

            switch (num){
                case 1: {
                    operationDTO.setSale1(category);
                    operationDTO.setSale1Total(totalPrice);
                    break;
                }
                case 2: {
                    operationDTO.setSale2(category);
                    operationDTO.setSale2Total(totalPrice);
                    break;
                }
                case 3: {
                    operationDTO.setSale3(category);
                    operationDTO.setSale3Total(totalPrice);
                    break;
                }
                case 4: {
                    long total = operationDTO.getSale4Total() + totalPrice;
                    operationDTO.setSale4Total(total);
                    break;
                }
            }

            num++;

        }

        System.out.println(operationDTO);

        return operationDTO;
    }

    public OperationDTO countDailyOrderStats(OperationDTO operationDTO) {
        LocalDate startDay = LocalDate.now().minusDays(4);  // 4일 전
        LocalDate endDay = LocalDate.now();  // 오늘

        // 4일 전부터 오늘까지 날짜별로 초기화된 DTO를 넣음
        Map<LocalDate, OrderSummaryDTO> summaryMap = new TreeMap<>();
        for (int i = 0; i <= 4; i++) {
            LocalDate date = startDay.plusDays(i);
            summaryMap.put(date, OrderSummaryDTO.builder()
                    .date(date)
                    .orderTotal(0)
                    .creditTotal(0)
                    .cancelTotal(0)
                    .build());
        }

        LocalDateTime startOfToday = startDay.atStartOfDay();  // 4일 전부터의 시간대

        List<Object[]> results = orderItemRepository.countOrderStatsByDate(startOfToday);

        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            String status = (String) row[1];
            Long count = (Long) row[2];

            OrderSummaryDTO dto = summaryMap.getOrDefault(date, new OrderSummaryDTO(date, 0, 0, 0));

            if (status.equals("입금대기")) {
                dto.setOrderTotal(dto.getOrderTotal() + count);
            } else if (status.equals("구매확정") || status.equals("배송중")) {
                dto.setCreditTotal(dto.getCreditTotal() + count);
            } else {
                dto.setCancelTotal(dto.getCancelTotal() + count);
            }

            summaryMap.put(date, dto);  // 날짜별로 업데이트된 정보 삽입
        }

        // 최종 결과를 리스트로 변환하여 operationDTO에 저장
        List<OrderSummaryDTO> orderSummaryDTOList = new ArrayList<>(summaryMap.values());
        operationDTO.setSummaryDTOS(orderSummaryDTOList);

        return operationDTO;
    }

}
