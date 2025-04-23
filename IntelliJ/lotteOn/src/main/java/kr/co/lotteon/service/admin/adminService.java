package kr.co.lotteon.service.admin;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductDetail;
import kr.co.lotteon.entity.product.ProductImage;
import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.repository.category.SubCategoryRepository;
import kr.co.lotteon.repository.product.CartRepository;
import kr.co.lotteon.repository.product.ProductDetailRepository;
import kr.co.lotteon.repository.product.ProductImageRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.seller.SellerRepository;
import kr.co.lotteon.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class adminService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CartRepository cartRepository;

    private final ModelMapper modelMapper;


    /*
     * 관리자 페이지 (상품 등록 메서드)
     * */
    public Product saveProduct(ProductDTO productDTO) {

        // 판매자, 서브카테고리 호출
        Seller seller = sellerRepository.findByCompany(productDTO.getCompany());
        SubCategory subCategory = subCategoryRepository.findById(productDTO.getSubCateNo()).get();

        Product product = modelMapper.map(productDTO, Product.class);

        product.setSeller(seller);
        product.setSubCategory(subCategory);

        // 제품 번호를 위한 준비
        // 년도 + 메인카테고리 번호 + 서브카테고리 번호
        String subNo = String.valueOf(subCategory.getSubCateNo());
        if(subNo.length()==1){
            subNo = "0" + subNo;
        }

        String mainNo = String.valueOf(subCategory.getMainCategory().getMainCateNo());
        if(mainNo.length()==1){
            mainNo = "0" + mainNo;
        }
        String year = String.valueOf(LocalDate.now().getYear());

        long prodNo = Long.parseLong(year + mainNo + subNo + "001");

        //findByProdNoStartingWith
        while(true){

            String select = String.valueOf(prodNo);
            List<Product> optionalProduct = productRepository.findByProdNoStartingWith(select);
            if(optionalProduct.size()>0){
                prodNo += 1;
            }

            if(optionalProduct.size()==0){
                product.setProdNo(String.valueOf(prodNo));
                return productRepository.save(product);
            }
        }


    }

    /*
     * 제품 상세 정보 저장 메서드
     * */
    public void saveProductDetail(ProductDetailDTO productDetailDTO, Product savedProduct) {

        ProductDetail productDetail = modelMapper.map(productDetailDTO, ProductDetail.class);
        productDetail.setProduct(savedProduct);
        productDetailRepository.save(productDetail);
    }

    public PageResponseDTO selectAllForList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("no");

        Page<Tuple> pageProduct = productRepository.selectAllForListByRole(pageRequestDTO, pageable);

        List<ProductDTO> productDTOList = pageProduct.getContent().stream().map(tuple -> {
            Product product = tuple.get(0, Product.class);
            String company = tuple.get(1,  String.class);
            ProductImage productImage = tuple.get(2,  ProductImage.class);

            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            productDTO.setCompany(company);

            ProductImageDTO productImageDTO = modelMapper.map(productImage, ProductImageDTO.class);
            productDTO.setProductImageDTO(productImageDTO);

            return productDTO;
        }).toList();

        int total = (int) pageProduct.getTotalElements();

        log.info("total: {}", total);
        log.info("productDTOList: {}", productDTOList);

        return PageResponseDTO.<ProductDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(productDTOList)
                .total(total)
                .build();

    }

    /*
     * 제품 삭제
     * */

    @Transactional
    public void deleteProduct(String no) {

        // 제품 엔티티 출력
        Optional<Product> optProduct = productRepository.findById(no);

        /*
         * 상품을 삭제할 때
         * */
        if(optProduct.isPresent()){
            Product product = optProduct.get();

            productDetailRepository.deleteByProduct(product);
            productImageRepository.deleteByProduct(product);
            cartRepository.deleteByProduct(product);
            productRepository.deleteById(no);
        }


        // 제품 삭제

        // productDetailRepository.deleteByPro



    }
}
