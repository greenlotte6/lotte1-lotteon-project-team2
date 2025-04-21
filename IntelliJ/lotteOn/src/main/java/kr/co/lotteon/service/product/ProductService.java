//package kr.co.lotteon.service.product;
//
//import com.querydsl.core.Tuple;
//import kr.co.lotteon.dto.page.PageRequestDTO;
//import kr.co.lotteon.dto.page.PageResponseDTO;
//import kr.co.lotteon.dto.product.ProductDTO;
//import kr.co.lotteon.entity.product.Product;
//import kr.co.lotteon.repository.product.ProductRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final ModelMapper modelMapper;
//
//
//    // 상품 목록
//    public PageResponseDTO selectAllForList(PageRequestDTO pageRequestDTO) {
//        Pageable pageable = pageRequestDTO.getPageable("no");
//
//        Page<Tuple> pageProduct = productRepository.selectAllForList(pageRequestDTO, pageable);
//
//        List<ProductDTO> productDTOList = pageProduct.getContent().stream().map(tuple -> {
//            Product product = tuple.get(0, Product.class);
//            String cname = tuple.get(1, String.class);
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setCname(cname);
//            return productDTO;
//        }).toList();
//
//        int total = (int) pageProduct.getTotalElements();
//
//        return PageResponseDTO.<ProductDTO>builder()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(productDTOList)
//                .total(total)
//                .build();
//    }
//
//    // 베스트 상품
//    public List<ProductDTO> selectBestAllForList(String cate) {
//        Page<Tuple> pageProduct = productRepository.selectBestAllForList(cate);
//
//        List<ProductDTO> productDTOList = pageProduct.getContent().stream().map(tuple -> {
//            Product product = tuple.get(0, Product.class);
//            String cname = tuple.get(1, String.class);
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setCname(cname);
//            return productDTO;
//        }).toList();
//
//        return productDTOList;
//    }
//
//    // 상품 목록 정렬
//    public PageResponseDTO  sortedProducts(PageRequestDTO pageRequestDTO) {
//        Pageable pageable = pageRequestDTO.getPageable("no");
//
//        Page<Tuple> pageProduct = productRepository.sortedProducts(pageRequestDTO, pageable);
//
//        List<ProductDTO> productDTOList = pageProduct.getContent().stream().map(tuple -> {
//            Product product = tuple.get(0, Product.class);
//            String cname = tuple.get(1, String.class);
//            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//            productDTO.setCname(cname);
//            return productDTO;
//        }).toList();
//
//        int total = (int) pageProduct.getTotalElements();
//
//        return PageResponseDTO.<ProductDTO>builder()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(productDTOList)
//                .total(total)
//                .build();
//    }
//
//
//
//
//}
