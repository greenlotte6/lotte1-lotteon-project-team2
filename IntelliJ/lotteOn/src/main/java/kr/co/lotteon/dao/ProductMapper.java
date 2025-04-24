package kr.co.lotteon.dao;

import kr.co.lotteon.dto.product.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    ProductDTO selectProductByProdNo(@Param("prodNo") String prodNo);
}
