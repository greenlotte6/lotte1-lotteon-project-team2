package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.entity.product.ProductDetail;
import kr.co.lotteon.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ModelMapper modelMapper;

    public ProductDetailDTO findByProdNo(PageRequestDTO pageRequestDTO) {
        String prodNo = pageRequestDTO.getProdNo();

        Optional<ProductDetail> optProductDetail = productDetailRepository.findByProduct_ProdNo(prodNo);

        if (optProductDetail.isPresent()) {
            ProductDetail productDetail = optProductDetail.get();
            return modelMapper.map(productDetail, ProductDetailDTO.class);
        }

        return null;
    }
}
