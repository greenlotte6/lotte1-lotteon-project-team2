package kr.co.lotteon.service.mypage;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.feedback.Review;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.article.InquiryRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.feedback.ReviewRepository;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

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

    public PageResponseDTO<PointDTO> searchPoint(UserDTO userDTO, PageRequestDTO pageRequestDTO, String startDate, String endDate, String search) {

        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("pointNo");

        Page<Point> pagePoint = pointRepository.searchPoints(user, search, startDate, endDate, pageable);

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
/*
    public PageResponseDTO<OrderDTO> orderFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO) {

        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("orderNo");

        Page<Order> pageOrder = orderRepository.findAllByUser(user, pageable);

        List<OrderDTO> orderDTOList = pageOrder.getContent().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageOrder.getTotalElements();

        return PageResponseDTO.<OrderDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(orderDTOList)
                .total(total)
                .build();

    }

    public PageResponseDTO<OrderItemDTO> orderItemFindAll(UserDTO userDTO, PageRequestDTO pageRequestDTO) {

        User user = modelMapper.map(userDTO, User.class);

        Pageable pageable = pageRequestDTO.getPageable("itemNo");

        Page<OrderItem> pageOrderItem = orderItemRepository.findAllByUser(user, pageable);

        List<OrderItemDTO> orderItemDTOList = pageOrderItem.getContent().stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                .collect(Collectors.toList());

        int total = (int) pageOrderItem.getTotalElements();

        return PageResponseDTO.<OrderItemDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(orderItemDTOList)
                .total(total)
                .build();

    }
*/

    public UserDTO findByUid(String uid) {

        Optional<User> optUser = userRepository.findByUid(uid);

        if (optUser.isPresent()) {
            User user = optUser.get();
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return userDTO;
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

    /*
    public void countOrder(UserDTO userDTO){

    }

    public void countCoupon(UserDTO userDTO){

    }

    public void countInquiry(UserDTO userDTO){

    }
*/


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


}