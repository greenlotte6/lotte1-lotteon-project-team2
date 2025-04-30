package kr.co.lotteon.service.admin;

import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;


    public ProductDTO OptionSplit(ProductDTO productDTO) {
        String prodNo = productDTO.getProdNo();
        Product product = productRepository.findById(prodNo).orElse(null);

        ProductDTO productDTO1 = modelMapper.map(product, ProductDTO.class);
        System.out.println(productDTO1);

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


}
