package kr.co.lotteon.service.product;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    // 글 조회수
    @Transactional
    public void increaseHit(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();
        Product product = productRepository.findByProdNo(prodNo)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));
        product.setHit(product.getHit() + 1);
    }


    public ProductDTO OptionSplit(ProductDTO productDTO) {
        String prodNo = productDTO.getProdNo();
        Product product = productRepository.findById(prodNo).orElse(null);

        ProductDTO productDTO1 = modelMapper.map(product, ProductDTO.class);

        String[] option = new String[6];
        String[][] str = new String[6][10];

        // 첫 번째 옵션
        if(productDTO1.getProductDetail() != null) {

            if(productDTO1.getProductDetail().getOpt1() != null) {
                option[0] = productDTO1.getProductDetail().getOpt1();
                String[] optList = productDTO1.getProductDetail().getOpt1Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[0][i] = optList[i];
                }
            }

            // 두 번째 옵션
            if(productDTO1.getProductDetail().getOpt2() != null) {
                option[1] = productDTO1.getProductDetail().getOpt2();
                String[] optList = productDTO1.getProductDetail().getOpt2Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[1][i] = optList[i];
                }
            }

            // 세 번째 옵션
            if(productDTO1.getProductDetail().getOpt3() != null) {
                option[2] = productDTO1.getProductDetail().getOpt3();
                String[] optList = productDTO1.getProductDetail().getOpt3Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[2][i] = optList[i];
                }
            }

            // 네 번째 옵션
            if(productDTO1.getProductDetail().getOpt4() != null) {
                option[3] = productDTO1.getProductDetail().getOpt4();
                String[] optList = productDTO1.getProductDetail().getOpt4Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[3][i] = optList[i];
                }
            }

            // 다섯 번째 옵션
            if(productDTO1.getProductDetail().getOpt5() != null) {
                option[4] = productDTO1.getProductDetail().getOpt5();
                String[] optList = productDTO1.getProductDetail().getOpt5Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[4][i] = optList[i];
                }
            }

            // 여섯 번째 옵션
            if(productDTO1.getProductDetail().getOpt6() != null) {
                option[5] = productDTO1.getProductDetail().getOpt6();
                String[] optList = productDTO1.getProductDetail().getOpt6Cont().split(",");
                for (int i = 0; i < optList.length; i++) {
                    str[5][i] = optList[i];
                }
            }

            productDTO.setOption(option);
            productDTO.setOptions(str);
            return productDTO;
        }

        return productDTO;
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

    public void hitCountUp(String prodNo) {
        Optional<Product> productOptional = productRepository.findByProdNo(prodNo);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setHit(product.getHit() + 1);
            productRepository.save(product);
        }
    }

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

    @CacheEvict(value = "product-discount"  , allEntries = true)
    public void deleteDiscountCache() {
    }

    @CacheEvict(value = "product-top-review"  , allEntries = true)
    public void deleteRecommendationCache() {
    }

    @CacheEvict(value = "product-recent"  , allEntries = true)
    public void deleteRecentCache() {
    }

    @CacheEvict(value = "product-many-review"  , allEntries = true)
    public void deleteReviewManyCache() {
    }

    @CacheEvict(value = "autocomplete", allEntries = true)
    public void deleteSearchList(){

    }

    public void deleteRegisterCache() {
        deleteRecentCache();
        deleteSearchList();
    }





}

