package kr.co.lotteon.service.order;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductRepository;
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

}
