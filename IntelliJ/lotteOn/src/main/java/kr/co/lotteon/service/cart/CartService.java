package kr.co.lotteon.service.cart;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    // 장바구니 뷰 보여주기
    public ProductDTO findAllByProdNo(ItemRequestDTO itemRequestDTO) {
        String prodNo = itemRequestDTO.getProdNo();

        Optional<Product> optProduct = productRepository.findByProdNo(prodNo);

        if (optProduct.isPresent()) {

            Product product = optProduct.get();
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            return productDTO;
        }
        return null;
    }


    // 장바구니에 상품 추가
    public int addToCart(ItemRequestDTO itemRequestDTO, UserDetails userDetails) {

        Product product = Product.builder()
                .prodNo(itemRequestDTO.getProdNo())
                .build();

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        Cart cart = toCartEntity(itemRequestDTO, product, user);

        try {
            cartRepository.save(cart);
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
                .opt1(options.getOrDefault("opt1", null))
                .opt1Cont(options.getOrDefault("opt1cont", null))
                .opt2(options.getOrDefault("opt2", null))
                .opt2Cont(options.getOrDefault("opt2cont", null))
                .opt3(options.getOrDefault("opt3", null))
                .opt3Cont(options.getOrDefault("opt3cont", null))
                .opt4(options.getOrDefault("opt4", null))
                .opt4Cont(options.getOrDefault("opt4cont", null))
                .opt5(options.getOrDefault("opt5", null))
                .opt5Cont(options.getOrDefault("opt5cont", null))
                .opt6(options.getOrDefault("opt6", null))
                .opt6Cont(options.getOrDefault("opt6cont", null))
                .build();
    }

}
