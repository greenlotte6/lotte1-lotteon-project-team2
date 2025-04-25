package kr.co.lotteon.service.product;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 글 조회수
    @Transactional
    public void increaseHit(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();
        Product product = productRepository.findByProdNo(prodNo)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        product.setHit(product.getHit() + 1);
    }

}

