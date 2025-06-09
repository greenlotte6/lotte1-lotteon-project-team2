package kr.co.lotteon.service.product;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.point.PointRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderSubmitService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final PointRepository pointRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final CartRepository cartRepository;


    // OrderDTO 준비
    public void prepareOrderDTO(OrderDTO orderDTO, Amount amount, UserDetails userDetails) {
        orderDTO.setTotalQuantity(amount.getQuantity());
        orderDTO.setUid(userDetails.getUsername());

        String receiverAddr1 = orderDTO.getReceiverAddr1();
        String receiverAddr2 = orderDTO.getReceiverAddr2();

        String fullAddr = ((receiverAddr1 != null) ? receiverAddr1.trim() : "") + " " +
                ((receiverAddr2 != null) ? receiverAddr2.trim() : "");
        orderDTO.setOrderAddr(fullAddr.trim());
    }


    //주문 등록
    public int registerOrder(OrderDTO orderDTO) {
        String uid =  orderDTO.getUid();
        Optional<User> userOpt = userRepository.findByUid(uid);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            orderDTO.setOrderSender(user.getName());
            orderDTO.setSenderHp(user.getHp());

            // 카드 번호(임시)
            String paymentContent = "1234 4567 7894 1234";
            orderDTO.setPaymentContent(paymentContent);

            Order order = modelMapper.map(orderDTO, Order.class);
            order.setUser(user);

            Order saveOrder = orderRepository.save(order);
            return saveOrder.getOrderNo();
        }
        return 0;
    }


    // 상세 주문 등록하기
    public void registerOrderItem(int orderNo, List<Integer> cartNos, List<Integer> itemPointList, HttpSession session) {
        OrderDTO orderDTO = findAllByOrderNo(orderNo);
        String status = "결제대기";

        boolean isCartOrder = cartNos != null && !cartNos.isEmpty();

        int loopCount = isCartOrder ? cartNos.size() : 1;

        for (int i = 0; i < loopCount; i++) {
            Integer itemPoint = itemPointList.get(i);
            Cart cart;
            Product product;
            String category;

            if (isCartOrder) {
                // 장바구니 주문
                Integer cartNo = cartNos.get(i);
                cart = cartRepository.findById(cartNo).orElseThrow(() -> new RuntimeException("Cart not found"));
                product = cart.getProduct();
                category = product.getSubCategory().getMainCategory().getMainCategoryName();
            } else {
                // 바로 주문
                CartDTO cartDTO = (CartDTO) session.getAttribute("cartDTO");
                ProductDTO productDTO = cartDTO.getProduct();
                category = productDTO.getMainCategoryName();

                cart = modelMapper.map(cartDTO, Cart.class);
                product = modelMapper.map(productDTO, Product.class);
            }

            Order order = Order.builder()
                    .orderNo(orderNo)
                    .payment(orderDTO.getPayment())
                    .build();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .opt1(cart.getOpt1()).opt2(cart.getOpt2())
                    .opt3(cart.getOpt3()).opt4(cart.getOpt4())
                    .opt5(cart.getOpt5()).opt6(cart.getOpt6())
                    .opt1Cont(cart.getOpt1Cont()).opt2Cont(cart.getOpt2Cont())
                    .opt3Cont(cart.getOpt3Cont()).opt4Cont(cart.getOpt4Cont())
                    .opt5Cont(cart.getOpt5Cont()).opt6Cont(cart.getOpt6Cont())
                    .itemCount(cart.getCartProdCount())
                    .itemPrice(product.getProdPrice())
                    .itemDiscount(product.getProdDiscount())
                    .itemPoint(itemPoint)
                    .category(category)
                    .orderStatus(status)
                    .build();
            orderItemRepository.save(orderItem);
        }
    }



    // 상품 재고, 판매량 계산
    public void changeSoldAndStock (List<Integer> cartNos) throws Exception {
        for(Integer cartNo : cartNos) {
            Optional<Cart> optCart = cartRepository.findById(cartNo);

            if(optCart.isPresent()) {
                Cart cart = optCart.get();
                String prodNo = cart.getProduct().getProdNo();
                int cartProdCount = cart.getCartProdCount();

                Optional<Product> optProduct = productRepository.findByProdNo(prodNo);
                if(optProduct.isPresent()) {
                    Product product = optProduct.get();

                    product.setProdSold(product.getProdSold() + cartProdCount);

                    if (product.getProdStock() >= cartProdCount) {
                        product.setProdStock(product.getProdStock() - cartProdCount);
                    } else {
                        throw new Exception("Insufficient stock for product: " + prodNo);
                    }
                    productRepository.save(product);
                }
            }
        }
    }


    // 쿠폰 사용 상태로 바꾸기
    public void changeState(Long issueNo) {
        if (issueNo == null || issueNo == 0L)
            return;

        Optional<CouponIssue> optCouponIssue = couponIssueRepository.findById(issueNo);

        if(optCouponIssue.isPresent()) {
            CouponIssue couponIssue = optCouponIssue.get();
            couponIssue.setState("사용");
            couponIssue.setUsedDate(LocalDate.now());
            couponIssueRepository.save(couponIssue);
        }
        else {
            log.error("Coupon not found for issueNo: {}", issueNo);
        }
    }


    // 포인트 사용 시 기록
    public UserDetailsDTO changePoint(Integer usedPoint, org.springframework.security.core.userdetails.UserDetails userDetails, int orderNo) {
        if (usedPoint == null || usedPoint == 0)
            return null;

        String uid = userDetails.getUsername();

        User user = User.builder()
                .uid(uid)
                .build();

        Optional<kr.co.lotteon.entity.user.UserDetails> optUserDetails = userDetailsRepository.findByUser(user);

        Point point = Point.builder()
                .point(usedPoint * (-1))
                .user(user)
                .pointDesc("상품 주문(주문 번호: " + orderNo + ") 차감")
                .expiryDate(null)
                .build();

        pointRepository.save(point);

        if (optUserDetails.isPresent()) {
            kr.co.lotteon.entity.user.UserDetails userDetail = optUserDetails.get();
            userDetail.setUserPoint(userDetail.getUserPoint() - usedPoint);
            userDetailsRepository.save(userDetail);
            UserDetailsDTO userDetailsDTO = modelMapper.map(userDetail, UserDetailsDTO.class);
            return userDetailsDTO;
        }
        return null;
    }


    // 카카오페이 주문용 DTO
    public Amount getAmount(int orderNo, UserDetails userDetails, OrderDTO orderDTO) {
        Amount amount = new Amount();
        String customOrderNo = "상품" + orderNo;
        amount.setItem_name(customOrderNo);
        amount.setPartner_order_id(String.valueOf(orderNo));
        amount.setPartner_user_id(userDetails.getUsername());
        amount.setTotal(orderDTO.getOrderTotalPrice());
        amount.setTax(orderDTO.getOrderTotalPrice() / 100 * 10);
        return amount;
    }


    // 장바구니 비우기
    @Transactional
    public void deleteAllByCartNo(List<Integer> cartNos) {
        for(Integer cartNo : cartNos) {
            Optional<Cart> optCart = cartRepository.findById(cartNo);
            if(optCart.isPresent()) {
                cartRepository.deleteByCartNo(cartNo);
            }
        }
    }


    // 주문 완료 상태로 변경
    @Transactional(rollbackOn = Exception.class)
    public void updateOrderStatusToCompleted(Integer orderNo) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderNo(orderNo);

        if (orderItems.isEmpty())
            return;

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderStatus("결제완료");
            orderItemRepository.save(orderItem);
        }
    }


    // 주문 완료 뷰
    public OrderDTO findAllByOrderNo(Integer orderNo) {
        Order order = orderRepository.findById(orderNo).get();
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        return orderDTO;
    }
}

