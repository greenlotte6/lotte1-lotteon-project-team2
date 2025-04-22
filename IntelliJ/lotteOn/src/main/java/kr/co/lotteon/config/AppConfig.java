package kr.co.lotteon.config;

import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AppInfo appInfo(){
        return new AppInfo();
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);

        // ✅ 중첩 매핑 규칙 추가
        modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getSubCategory().getSubCateNo(), ProductDTO::setSubCateNo);
            mapper.map(src -> src.getSubCategory().getMainCategory().getMainCategoryName(), ProductDTO::setMainCategoryName);
        });

        return modelMapper;
    }
}
