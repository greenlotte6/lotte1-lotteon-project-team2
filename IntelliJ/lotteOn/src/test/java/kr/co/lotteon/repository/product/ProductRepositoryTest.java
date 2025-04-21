package kr.co.lotteon.repository.product;

import jakarta.transaction.Transactional;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.seller.Seller;
import kr.co.lotteon.repository.category.SubCategoryRepository;
import kr.co.lotteon.repository.seller.SellerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {
/*
    @Autowired
    private ProductRepository repository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Transactional
    @Rollback(false)
    @Test
    void save(){
        Product product = new Product();

        Optional<SubCategory> subCategory = subCategoryRepository.findById(1);
        SubCategory subCate = null;
        if(subCategory.isPresent()){
            subCate = subCategory.get();
            System.out.println("존재");
        }else{
            System.out.println("없음");
        }


        Seller seller = sellerRepository.findById(1).get();

        product.setSeller(seller);
        product.setProdNo(1);
        product.setSubCategory(subCate);
        product.setProdName("상품1");

        System.out.println(product);

        repository.save(product);

    }
    */

}