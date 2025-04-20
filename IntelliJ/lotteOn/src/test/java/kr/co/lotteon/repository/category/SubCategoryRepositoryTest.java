package kr.co.lotteon.repository.category;

import jakarta.transaction.Transactional;
import kr.co.lotteon.entity.category.MainCategory;
import kr.co.lotteon.entity.category.SubCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/*
* 카테고리 테스트
* */

@SpringBootTest
class SubCategoryRepositoryTest {

    /*

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Test
    void registerCategory(){


        MainCategory maincate = MainCategory.builder()
                .mainCategoryName("여성패션")
                .build();

        MainCategory savedCategory = mainCategoryRepository.save(maincate);

        SubCategory subcate = SubCategory.builder()
                .mainCategory(savedCategory)
                .subCategoryName("브랜드의류")
                .build();

        subCategoryRepository.save(subcate);

    }

    @Transactional
    @Test
    void findByCategoryName(){
        SubCategory subCategory = subCategoryRepository.findById(1).get();
        System.out.println(subCategory);
    }

    */


}