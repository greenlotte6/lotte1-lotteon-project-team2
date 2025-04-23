package kr.co.lotteon.service.product;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    // 상품 + 판매자 + 쿠폰 + 배너
    public ProductDTO findByProdNo(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();
        Optional<Product> optProduct = productRepository.findByProdNo(prodNo);

        if(optProduct.isPresent()) {
            Product product = optProduct.get();
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            return productDTO;
        }
        return null;
    }

    // 글 조회수
    @Transactional
    public void increaseHit(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();
        Product product = productRepository.findByProdNo(prodNo)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        product.setHit(product.getHit() + 1);
    }

}

