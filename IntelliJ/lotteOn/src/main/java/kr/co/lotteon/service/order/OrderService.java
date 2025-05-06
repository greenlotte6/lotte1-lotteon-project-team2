package kr.co.lotteon.service.order;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

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
}
