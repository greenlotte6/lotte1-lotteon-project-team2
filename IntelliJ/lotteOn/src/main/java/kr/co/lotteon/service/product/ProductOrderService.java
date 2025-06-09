package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.order.OrderItemDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.order.OrderItemRepository;
import kr.co.lotteon.repository.order.OrderRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.service.admin.ProductService;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductOrderService {

    private final CouponIssueRepository couponIssueRepository;
    private final CartRepository cartRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final ProductService productService;
    private final UserService userService;


    // 장바구니 담고 주문하기 View //

    // 주문 상품 정보
    public CartDTO findByCartNo(int cartNo) {
        Optional<Cart> optCart = cartRepository.findByCartNo(cartNo);

        if (optCart.isPresent()) {
            Cart cart = optCart.get();
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            return cartDTO;
        }
        return null;
    }

    // 주문 사용자 정보
    public UserDetailsDTO findByUser (String uid) {
        Optional<User> optUser = userRepository.findByUid(uid);
        if (optUser.isEmpty()) {
            return null;
        }
        User user = optUser.get();

        Optional<kr.co.lotteon.entity.user.UserDetails> optUserDetails = userDetailsRepository.findByUser(user);

        if (optUserDetails.isPresent()) {
            kr.co.lotteon.entity.user.UserDetails userDetails = optUserDetails.get();
            UserDetailsDTO userDetailsDTO = modelMapper.map(userDetails, UserDetailsDTO.class);
            return userDetailsDTO;
        }
        return null;
    }

    // 발급 받은 쿠폰 정보
    public List<CouponIssueDTO> findAllByUser(String uid) {
        Optional<User> optUser = userRepository.findByUid(uid);
        if (optUser.isEmpty()) {
            return null;
        }
        User user = optUser.get();

        List<CouponIssue> couponIssueList = couponIssueRepository.findAllByUser(user);

        List<CouponIssueDTO> couponIssueDTOList = couponIssueList.stream()
                .filter(couponIssue -> couponIssue.getValidTo() != null &&
                        LocalDateTime.parse(couponIssue.getValidTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                .isAfter(LocalDateTime.now()))
                .map(couponIssue -> modelMapper.map(couponIssue, CouponIssueDTO.class))
                .collect(Collectors.toList());

        return couponIssueDTOList;
    }



    // 바로 주문하기 View
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

}
