package kr.co.lotteon.service.cart;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final  ModelMapper modelMapper;
    //private final RedisTemplate<String, CartDTO> redisTemplate;

    // 장바구니에 상품 추가
    public int addToCart(ItemRequestDTO itemRequestDTO, UserDetails userDetails) {
        Product product = Product.builder()
                .prodNo(itemRequestDTO.getProdNo())
                .build();

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        Map<String, String> options = itemRequestDTO.getOptions();

        // 옵션 개수 확인 및 동적 비교
        String[] optParams = new String[6];
        String[] optContParams = new String[6];

        // 옵션 1~6에 대해 존재하는 값만 가져오기
        for (int i = 0; i < 6; i++) {
            optParams[i] = options.getOrDefault("opt" + (i + 1), "");
            optContParams[i] = options.getOrDefault("opt" + (i + 1) + "cont", "");
        }

        // 장바구니에서 기존 상품 있는지 찾기
        Optional<Cart> optCart = cartRepository.findByUserAndProductAndOptions(
                user.getUid(),
                product.getProdNo(),
                optParams[0], optContParams[0],
                optParams[1], optContParams[1],
                optParams[2], optContParams[2],
                optParams[3], optContParams[3],
                optParams[4], optContParams[4],
                optParams[5], optContParams[5]
        );

        try {
            if (optCart.isPresent()) {
                // 이미 있는 상품: 수량만 증가
                Cart cart = optCart.get();
                cart.setCartProdCount(cart.getCartProdCount() + itemRequestDTO.getQuantity());
                cartRepository.save(cart);
            } else {
                Cart cart = toCartEntity(itemRequestDTO, product, user);
                cartRepository.save(cart);
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }


    // itemRequestDTO -> Cart Entity 변환
    private Cart toCartEntity(ItemRequestDTO dto, Product product, User user) {
        Map<String, String> options = dto.getOptions();

        return Cart.builder()
                .user(user)
                .product(product)
                .cartProdCount(dto.getQuantity())
                .opt1(options.getOrDefault("opt1", ""))
                .opt1Cont(options.getOrDefault("opt1cont", ""))
                .opt2(options.getOrDefault("opt2", ""))
                .opt2Cont(options.getOrDefault("opt2cont", ""))
                .opt3(options.getOrDefault("opt3", ""))
                .opt3Cont(options.getOrDefault("opt3cont", ""))
                .opt4(options.getOrDefault("opt4", ""))
                .opt4Cont(options.getOrDefault("opt4cont", ""))
                .opt5(options.getOrDefault("opt5", ""))
                .opt5Cont(options.getOrDefault("opt5cont", ""))
                .opt6(options.getOrDefault("opt6", ""))
                .opt6Cont(options.getOrDefault("opt6cont", ""))
                .build();
    }

    // 장바구니 View
    public List<CartDTO> findAllByUid(UserDetails userDetails) {

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        List<Cart> items = cartRepository.findAllByUser(user);

        List<CartDTO> cartDTOList = items.stream()
                .map(item -> {
                    CartDTO dto = modelMapper.map(item, CartDTO.class);
                    dto.setUid(userDetails.getUsername());
                    return dto;
                })
                .collect(Collectors.toList());


        return cartDTOList;
    }

    // 장바구니 상품 삭제
    @Transactional
    public int deleteByCartNo(int cartNo) {
        try {
            cartRepository.deleteByCartNo(cartNo);
            return 1;
        } catch (Exception e) {
            log.error("Cart 삭제 중 예외 발생. cartNo: {}", cartNo, e);
            return 0;
        }
    }

    @Transactional
    public void deleteAllByCartNo(List<Integer> cartNos) {
        for(Integer cartNo : cartNos) {
            cartRepository.deleteByCartNo(cartNo);
        }
    }

    public void updateCartProdCount(Integer cartNo, int newQuantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartNo);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.setCartProdCount(newQuantity);
            cartRepository.save(cart);
        } else {
            throw new RuntimeException("장바구니 항목을 찾을 수 없습니다.");
        }
    }



}
