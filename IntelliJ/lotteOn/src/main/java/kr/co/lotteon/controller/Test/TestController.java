package kr.co.lotteon.controller.Test;


import kr.co.lotteon.dao.ProductMapper;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.order.OrderItemDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.service.admin.TestService;
import kr.co.lotteon.service.article.InquiryService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.feedback.ReviewService;
import kr.co.lotteon.service.product.ProductDetailService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController {

    private final ProductMapper productMapper;
    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final CouponService couponService;
    private final TestService testService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/productTest/view")
    public String view(PageRequestDTO pageRequestDTO, Model model) {
        pageRequestDTO.setSize(5);
        String prodNo = pageRequestDTO.getProdNo();
        // 상품 + 상품이미지
        ProductDTO productDTO = productMapper.selectProductByProdNo(prodNo);

        // 상품 번호
        productDTO = testService.OptionSplit(productDTO);
        System.out.println(productDTO);

        // 쿠폰
        List<CouponDTO> couponDTOList = couponService.findAllByCompany(productDTO.getCompany());
        // 상품 상세
        ProductDetailDTO productDetailDTO = productDetailService.findByProdNo(pageRequestDTO);
        // 리뷰
        PageResponseDTO reviewPageResponseDTO = reviewService.selectAllForList(pageRequestDTO);

        // qna
        //PageResponseDTO inquiryPageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);
        model.addAttribute(productDTO);
        model.addAttribute(couponDTOList);
        model.addAttribute(productDetailDTO);
        model.addAttribute("reviewPageResponseDTO", reviewPageResponseDTO);

        return "/product/beauty/perfume/viewTest";
    }

    // 주문정보 호출하기
    @GetMapping("/test/user/orders")
    public String orders(@AuthenticationPrincipal UserDetails userDetails) {
        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDTO> orderDTOS = new ArrayList<>();

        // 주문정보
        for (Order order : orders) {
            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
            // 주문 번호에 따른 아이템의 Product 들고오기
            for(OrderItemDTO orderItemDTO : orderDTO.getOrderItems()){
                OrderItem orderItem = orderItemRepository.findById(orderItemDTO.getItemNo()).get();
                orderItemDTOS.add(orderItemDTO);
            }
            orderDTO.setOrderItems(orderItemDTOS);
            orderDTOS.add(orderDTO);
        }

        System.out.println(orderDTOS);

        return "/admin/main";

    }
}
