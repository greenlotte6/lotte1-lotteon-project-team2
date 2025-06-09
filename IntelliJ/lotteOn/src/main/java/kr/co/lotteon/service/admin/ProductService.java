package kr.co.lotteon.service.admin;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;



    public ProductDTO findByNo(String prodNo) {
        Optional<Product> productOptional = productRepository.findByProdNo(prodNo);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            return productDTO;
        }
        return null;
    }

    public void directSoldAndStock(CartDTO cartDTO) throws Exception {

        ProductDTO productDTO = cartDTO.getProduct();
        Product product = productRepository.findById(productDTO.getProdNo()).get();

        int cartProdCount = cartDTO.getCartProdCount();

        product.setProdSold(product.getProdSold() + cartProdCount);

        if (product.getProdStock() >= cartProdCount) {
            product.setProdStock(product.getProdStock() - cartProdCount);
        } else {
            throw new Exception("Insufficient stock for product: " + product.getProdNo());
        }
        productRepository.save(product);

    }


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



    // 할인 상품 top 8 출력 - 메인
    public List<ProductDTO> findDiscountTop8() {
        log.info("할인 상품 출력");
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findTop8ByOrderByProdDiscountDesc();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setSNameThumb3(product.getProductImage().getSNameThumb3());

            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    // 조회수 높은 상품 8개
    //@CacheEvict(value = "product-hit"  , allEntries = true)
    @Cacheable(value = "product-hit")
    public List<ProductDTO> findHitTop8() {

        log.info("조회수 높은 상품 출력");
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findTop8ByOrderByHitDesc();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    // 리뷰 총점 높은 상품 8개
    @Cacheable(value = "product-top-review")
    public List<ProductDTO> findReviewTop8() {
        log.info("리뷰 총점 높은 상품 출력");

        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findTop8ByOrderByRatingTotalDesc();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setSNameThumb3(product.getProductImage().getSNameThumb3());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    // 리뷰 많은 상품 8개
    @Cacheable(value = "product-many-review")
    public List<ProductDTO> findReviewManyTop8() {

        log.info("리뷰 많은 상품 출력");

        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findTop8ByOrderByReviewCountDesc();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setSNameThumb3(product.getProductImage().getSNameThumb3());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    // 베스트 상품 5개 / 판매량 높은순

    @Cacheable(value = "product-best")
    public List<ProductDTO> findBestTop5() {

        log.info("베스트 상품 출력");

        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findTop5ByOrderByProdSoldDesc();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setSNameThumb3(product.getProductImage().getSNameThumb3());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    // 최신 상품 8개
    @Cacheable(value = "product-recent")
    public List<ProductDTO> findRecentTop8() {
        log.info("최신 상품 출력");
        List<ProductDTO> productDTOS = new ArrayList<>();
        List<Product> products = productRepository.findTop8ByOrderByRegDateDesc();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setSNameThumb3(product.getProductImage().getSNameThumb3());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    // 할인 상품 캐시 삭제
    @CacheEvict(value = "product-discount"  , allEntries = true)
    public void deleteDiscountCache() {}

    //리뷰 높은 상품 캐시 삭제
    @CacheEvict(value = "product-top-review"  , allEntries = true)
    public void deleteRecommendationCache() {}

    // 최신 상품 캐시 삭제
    @CacheEvict(value = "product-recent"  , allEntries = true)
    public void deleteRecentCache() {}

    // 리뷰 많은 상품 캐시 삭제
    @CacheEvict(value = "product-many-review"  , allEntries = true)
    public void deleteReviewManyCache() {}

    // 검색 캐시 삭제
    @CacheEvict(value = "autocomplete", allEntries = true)
    public void deleteSearchListCache(){}

    // 인기 상품 캐시 삭제
    @CacheEvict(value = "product-hit", allEntries = true)
    public void deleteHitCache() {}

    // 베스트 상품 캐시 삭제
    @CacheEvict(value = "product-best", allEntries = true)
    public void deleteBestCache() {}

    // 베스트 상품 목록 캐시 무효화
    @CacheEvict(value = "bestProductList", allEntries = true)
    public void deleteBestListCache() {}

    // 정렬된 상품 목록 캐시 무효화
    @CacheEvict(value = "sortedProductList", allEntries = true)
    public void deleteSortedProductListCache() {}

}

