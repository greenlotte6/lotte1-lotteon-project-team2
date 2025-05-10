package kr.co.lotteon.service.order;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.service.product.ProductService;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final UserService userService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public CartDTO findByCartNo(int cartNo) {
        Optional<Cart> optCart = cartRepository.findByCartNo(cartNo);

        if (optCart.isPresent()) {
            Cart cart = optCart.get();
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            return cartDTO;
        }
        return null;
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

    public OrderDTO findAllByOrderNo(Integer orderNo) {
        Order order = orderRepository.findById(orderNo).get();
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        return orderDTO;
    }


    public void registerOrderItem(int orderNo, List<Integer> cartNos) {
        for (Integer cartNo : cartNos) {
            Cart cart = cartRepository.findById(cartNo).get();
            Product product = cart.getProduct();

            String category = product.getSubCategory().getMainCategory().getMainCategoryName();

            Order order = Order.builder()
                    .orderNo(orderNo)
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
                    .category(category)
                    .build();

            orderItemRepository.save(orderItem);

        }
    }

    // CartDTO 생성 로직을 makeCart 메서드로 분리
    public List<CartDTO> makeCart(ItemRequestDTO itemRequestDTO, UserDetails userDetails, Map<String, String> options) {

        List<CartDTO> cartDTOList = new ArrayList<>();
        String uid = userDetails.getUsername();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setProdNo(itemRequestDTO.getProdNo());
        cartDTO.setCartProdCount(itemRequestDTO.getQuantity());
        cartDTO.setUid(uid);

        if (options != null) {
            cartDTO.setOpt1(options.get("opt1"));
            cartDTO.setOpt1Cont(options.get("opt1Cont"));
            cartDTO.setOpt2(options.get("opt2"));
            cartDTO.setOpt2Cont(options.get("opt2Cont"));
            cartDTO.setOpt3(options.get("opt3"));
            cartDTO.setOpt3Cont(options.get("opt3Cont"));
            cartDTO.setOpt4(options.get("opt4"));
            cartDTO.setOpt4Cont(options.get("opt4Cont"));
            cartDTO.setOpt5(options.get("opt5"));
            cartDTO.setOpt5Cont(options.get("opt5Cont"));
            cartDTO.setOpt6(options.get("opt6"));
            cartDTO.setOpt6Cont(options.get("opt6Cont"));
        }

        ProductDTO productDTO = productService.findByNo(itemRequestDTO.getProdNo());
        cartDTO.setProduct(productDTO);

        UserDTO userDTO = userService.findById(userDetails.getUsername());
        cartDTO.setUser(userDTO);

        cartDTOList.add(cartDTO);

        return cartDTOList;
    }

    public void directOrderItem(int orderNo, CartDTO cartDTO) {

        ProductDTO productDTO = cartDTO.getProduct();
        String category = productDTO.getMainCategoryName();

        Cart cart = modelMapper.map(cartDTO, Cart.class);
        Product product = modelMapper.map(productDTO, Product.class);

        Order order = Order.builder()
                .orderNo(orderNo)
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
                .category(category)
                .build();

        orderItemRepository.save(orderItem);
    }


}
