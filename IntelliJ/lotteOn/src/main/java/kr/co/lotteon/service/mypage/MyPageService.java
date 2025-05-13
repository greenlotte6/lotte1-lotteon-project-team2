package kr.co.lotteon.service.mypage;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.feedback.ExchangeDTO;
import kr.co.lotteon.dto.feedback.ReturnDTO;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.order.OrderInfoDTO;
import kr.co.lotteon.dto.order.OrderItemDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.feedback.Exchange;
import kr.co.lotteon.entity.feedback.Return;
import kr.co.lotteon.entity.feedback.Review;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.feedback.ExchangeRepository;
import kr.co.lotteon.repository.feedback.ReturnRepository;
import kr.co.lotteon.repository.feedback.ReviewRepository;
import kr.co.lotteon.repository.order.OrderInfoRepository;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {

    private final InquiryRepository inquiryRepository;
    private final ReviewRepository reviewRepository;
    private final PointRepository pointRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ReturnRepository returnRepository;
    private final OrderItemRepository orderItemRepository;
    private final ExchangeRepository exchangeRepository;
    private final ProductRepository productRepository;

    @Value("${spring.servlet.multipart.location}")
    private String UploadPath;

    // private final String UploadPath = "C:/Users/lotte2/Desktop/workspace/lotte1-lotteon-project-team2/IntelliJ/lotteOn/uploads";


    public PageResponseDTO<InquiryDTO> inquiryFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO) {

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

    public long getPendingInquiryCount(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return inquiryRepository.countByUserAndState(user, "검토중");
    }

    public long getCouponCount(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        LocalDate today = LocalDate.now();

        return couponRepository.countByUserAndValidToAfter(user, today);
    }

    public String getPoint(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        LocalDateTime now = LocalDateTime.now();

        Long point = pointRepository.getSumPointByUserAndExpiryDateAfter(user, now);

        if(point == null) {
            point = 0L;
        }

        // 1,000 단위로 콤마 추가
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(point);
    }



    public PageResponseDTO<CouponDTO> couponFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO) {

        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("cno");

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 유효기간이 오늘 이후인 데이터만 조회
        Page<Coupon> pageCoupon = couponRepository.findAllByUserAndValidToAfter(user, today, pageable);

        List<CouponDTO> couponDTOList = pageCoupon.getContent().stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageCoupon.getTotalElements();

        return PageResponseDTO.<CouponDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(couponDTOList)
                .total(total)
                .build();

    }

    public void pointRegister(PointDTO pointDTO, UserDTO userDTO, OrderItemDTO orderItemDTO) {
        User user = modelMapper.map(userDTO, User.class);

        Point point = modelMapper.map(pointDTO, Point.class);

        OrderItem orderItem = modelMapper.map(orderItemDTO, OrderItem.class);

        int getPoint = orderItem.getItemPoint();
        //int orderNO = orderItem.getOrder().getOrderNo();

        point.setUser(user);
        point.setPoint(getPoint);
        point.setPointDesc("구매확정");
        point.setPointDate(LocalDateTime.now());
        point.setExpiryDate(LocalDateTime.now().plusDays(365));
        //point.setOrderNo(orderNO);

        pointRepository.save(point);

    }


    public PageResponseDTO<ReviewDTO> reviewFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO) {

        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("rno");

        Page<Review> pageReview = reviewRepository.findAllByWriter(user, pageable);

        List<ReviewDTO> reviewList = pageReview.getContent().stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();

    }

    public PageResponseDTO<PointDTO> pointFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Pageable pageable = pageRequestDTO.getPageable("pointNo");

        // 오늘 날짜
        //LocalDateTime today = LocalDateTime.now();

        // 유효기간이 오늘 이후인 데이터만 조회
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
    }



    public UserDTO findByUid(String uid) {

        Optional<User> optUser = userRepository.findByUid(uid);

        if (optUser.isPresent()) {
            User user = optUser.get();
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return userDTO;
        }

        return null;

    }

    public OrderItemDTO FindByItemNo(Long itemNo) {

        Optional<OrderItem> optOrderItem = orderItemRepository.findByItemNo(itemNo);

        if (optOrderItem.isPresent()) {
            OrderItem orderitem = optOrderItem.get();
            OrderItemDTO orderItemDTO = modelMapper.map(orderitem, OrderItemDTO.class);

            return orderItemDTO;
        }

        return null;
    }

    public OrderItemDTO findByItemNo(long itemNo) {

        Optional<OrderItem> optOrderItem = orderItemRepository.findByItemNo(itemNo);

        if (optOrderItem.isPresent()) {
            OrderItem orderItem = optOrderItem.get();
            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);

            return orderItemDTO;
        }

        return null;

    }

    public void modify(UserDTO userDTO) {

        boolean exists = userRepository.existsByUid(userDTO.getUid());

        if(exists){
            User user = modelMapper.map(userDTO, User.class);

            userRepository.save(user);
        }

    }

    public void withdraw(UserDTO userDTO){

        User user = userRepository.findByUid(userDTO.getUid())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        user.setLeaveDate(LocalDateTime.now()); // JPA 사용 시
        userRepository.save(user);

    }


    // 전화번호를 3등분하여 DTO에 세팅
    public void splitPhone(UserDTO userDTO) {
        String phone = userDTO.getHp();

        if (phone == null || phone.isBlank()) {
            userDTO.setPhonePart1("");
            userDTO.setPhonePart2("");
            userDTO.setPhonePart3("");
            return;
        }

        if (phone.startsWith("+")) {
            // 국제번호 처리
            String number = phone.replaceAll("[^0-9]", ""); // +821012345678 → 821012345678

            if (number.startsWith("82")) {
                number = "0" + number.substring(2); // 한국
            } else if (number.startsWith("1")) {
                number = number.substring(1); // 미국
            }

            if (number.length() >= 10) {
                userDTO.setPhonePart1(number.substring(0, 3));
                userDTO.setPhonePart2(number.substring(3, 7));
                userDTO.setPhonePart3(number.substring(7));
            }

        } else if (phone.contains("-")) {
            // 지역번호 형식
            String[] parts = phone.split("-");
            userDTO.setPhonePart1(parts.length > 0 ? parts[0] : "");
            userDTO.setPhonePart2(parts.length > 1 ? parts[1] : "");
            userDTO.setPhonePart3(parts.length > 2 ? parts[2] : "");
        } else {
            // fallback
            userDTO.setPhonePart1("");
            userDTO.setPhonePart2("");
            userDTO.setPhonePart3("");
        }


    }


    // 3등분된 번호를 하나로 합쳐 저장용 포맷으로 변환
    public String joinPhone(UserDTO userDTO) {
        String p1 = userDTO.getPhonePart1();
        String p2 = userDTO.getPhonePart2();
        String p3 = userDTO.getPhonePart3();

        if (p1.startsWith("0")) {
            // 한국 휴대폰: 010 → +82
            return "+82" + p1.substring(1) + p2 + p3;
        } else if (p1.length() == 3 && p2.length() > 0 && p3.length() > 0) {
            // 지역번호 그대로 저장
            return p1 + "-" + p2 + "-" + p3;
        } else {
            return "";
        }
    }


    public PageResponseDTO<OrderInfoDTO> orderInfoPaging(PageRequestDTO pageRequestDTO, String uid) {

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = orderRepository.orderInfoPaging(pageRequestDTO, pageable, uid);

        List<OrderInfoDTO> DTOList = pageObject.getContent().stream().map(tuple -> {

            OrderItem orderItem = tuple.get(0, OrderItem.class);
            OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
            ProductDTO productDTO = orderItemDTO.getProduct();

            String image = tuple.get(4, String.class);
            orderInfoDTO.setProductImage(image);

            productDTO.setProductDetail(null);
            productDTO.setProductImage(null);
            orderItemDTO.setProduct(null);
            productDTO.setSubCategory(null);

            orderInfoDTO.setSeller(productDTO.getSeller());
            productDTO.setSeller(null);

            orderInfoDTO.setProduct(productDTO);
            orderInfoDTO.setOrderItem(orderItemDTO);


            Order order = tuple.get(1, Order.class);
            int orderNo = order.getOrderNo();

            LocalDateTime orderDate = tuple.get(2, LocalDateTime.class);
            String orderStatus = tuple.get(3, String.class);

            Seller seller = tuple.get(5, Seller.class);
            User user = tuple.get(6, User.class);


            orderInfoDTO.setOrderNo(orderNo);
            orderInfoDTO.setOrderDate(orderDate);
            orderInfoDTO.setOrderStatus(orderStatus);
            orderInfoDTO.setOrder(modelMapper.map(order, OrderDTO.class));
            orderInfoDTO.setSeller(modelMapper.map(seller, SellerDTO.class));
            orderInfoDTO.setUser(modelMapper.map(user, UserDTO.class));

            return orderInfoDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageResponseDTO.<OrderInfoDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();

    }

    public PageResponseDTO<OrderInfoDTO> searchOrder(PageRequestDTO pageRequestDTO, String uid) {

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageObject = orderRepository.orderInfoPagingSearch(pageRequestDTO, pageable, uid);

        List<OrderInfoDTO> DTOList = pageObject.getContent().stream().map(tuple -> {

            OrderItem orderItem = tuple.get(0, OrderItem.class);
            OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
            ProductDTO productDTO = orderItemDTO.getProduct();

            String image = tuple.get(4, String.class);
            orderInfoDTO.setProductImage(image);

            productDTO.setProductDetail(null);
            productDTO.setProductImage(null);
            orderItemDTO.setProduct(null);
            productDTO.setSubCategory(null);

            orderInfoDTO.setSeller(productDTO.getSeller());
            productDTO.setSeller(null);

            orderInfoDTO.setProduct(productDTO);
            orderInfoDTO.setOrderItem(orderItemDTO);


            Order order = tuple.get(1, Order.class);
            int orderNo = order.getOrderNo();

            LocalDateTime orderDate = tuple.get(2, LocalDateTime.class);
            String orderStatus = tuple.get(3, String.class);

            Seller seller = tuple.get(5, Seller.class);
            User user = tuple.get(6, User.class);


            orderInfoDTO.setOrderNo(orderNo);
            orderInfoDTO.setOrderDate(orderDate);
            orderInfoDTO.setOrderStatus(orderStatus);
            orderInfoDTO.setOrder(modelMapper.map(order, OrderDTO.class));
            orderInfoDTO.setSeller(modelMapper.map(seller, SellerDTO.class));
            orderInfoDTO.setUser(modelMapper.map(user, UserDTO.class));

            return orderInfoDTO;
        }).toList();

        int total = (int) pageObject.getTotalElements();

        return PageResponseDTO.<OrderInfoDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(DTOList)
                .total(total)
                .build();

    }



    public boolean confirmPurchase(Long itemNo) {
        // 주문 아이템 조회
        OrderItem orderItem = orderItemRepository.findById(itemNo).orElse(null);
        if (orderItem != null) {
            // 상태 변경
            orderItem.setOrderStatus("구매확정");
            orderItemRepository.save(orderItem);
            return true;

        }else{
            return false;
        }

    }


    public void exchangeRequest(ExchangeDTO exchangedto, Long itemNo,String uploadType,  UserDTO userDTO, MultipartFile file) {

        OrderItem orderItem = orderItemRepository.findById(itemNo).orElse(null);
        User user = modelMapper.map(userDTO, User.class);

        Exchange exchange = modelMapper.map(exchangedto, Exchange.class);

        exchange.setUser(user);
        exchange.setOrderItem(orderItem);
        orderItem.setOrderStatus("교환신청");


        try{
            if (file != null && !file.isEmpty()) {
                String oName = file.getOriginalFilename();
                String fileName1 = saveFile(file, uploadType);

                exchange.setSName(fileName1);
                exchange.setOName(oName);
            }
        }catch (Exception e){
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }

        exchangeRepository.save(exchange);

    }

    public void returnRequest(ReturnDTO returnDTO, Long itemNo, String uploadType , UserDTO userDTO, MultipartFile file){

        OrderItem orderItem = orderItemRepository.findById(itemNo).orElse(null);
        User user = modelMapper.map(userDTO, User.class);

        Return aReturn = modelMapper.map(returnDTO, Return.class);

        aReturn.setUser(user);
        aReturn.setOrderItem(orderItem);
        orderItem.setOrderStatus("반품신청");




        try{
            if (file != null && !file.isEmpty()) {
                String oName = file.getOriginalFilename();
                String fileName1 = saveFile(file, uploadType);

                aReturn.setSName(fileName1);
                aReturn.setOName(oName);
            }
        }catch (Exception e){
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }


        returnRepository.save(aReturn);

    }


    public void reviewRegister(ReviewDTO reviewDTO, UserDTO userDTO, String productId, String uploadType , MultipartFile file1, MultipartFile file2) {


        User user = modelMapper.map(userDTO, User.class);

        Product product = productRepository.findByProdNo(productId).orElse(null);

        Review review = modelMapper.map(reviewDTO, Review.class);

        review.setWriter(user);
        review.setProduct(product);

        try{
            if(file1 != null && !file1.isEmpty()){

                String oNameFile1 = file1.getOriginalFilename();

                String fileName1 = saveFile(file1, uploadType);
                review.setSNameImage1(fileName1);
                review.setONameImage1(oNameFile1);
            }

            if(file2 != null && !file2.isEmpty()){

                String oNameFile2 = file2.getOriginalFilename();

                String fileName2 = saveFile(file2, uploadType);
                review.setSNameImage2(fileName2);
                review.setONameImage2(oNameFile2);
            }


        }catch(IOException e){
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }



        reviewRepository.save(review);

    }

    public void inquiryRegister(UserDTO userDTO, InquiryDTO inquiryDTO){

        User user = modelMapper.map(userDTO, User.class);

        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);

        inquiry.setUser(user);

        inquiryRepository.save(inquiry);

    }


    private String saveFile(MultipartFile file, String UploadType) throws IOException {
        if(file == null || file.isEmpty()){
            return null;
        }

        String originalFileName = file.getOriginalFilename();
        String ext = "";
        int idx = originalFileName.lastIndexOf('.');
        if(idx > 0) ext = originalFileName.substring(idx);

        // 파일명 중복 방지
        String uuid = UUID.randomUUID().toString();
        String savedFileName = uuid + ext;

        String path = "/" + UploadType;

        java.io.File fileUploadDir = new java.io.File(UploadPath + "/" + UploadType);

        if(!fileUploadDir.exists()){
            // 파일 업로드 디렉터리가 존재하지 않으면 생성
            fileUploadDir.mkdirs();
        }

        // 파일 업로드 디렉터리 시스템 경로 구하기
        String fileUploadPath = fileUploadDir.getAbsolutePath();

        // 파일 저장
        try {
            file.transferTo(new java.io.File(fileUploadPath, savedFileName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return savedFileName;
    }


}

