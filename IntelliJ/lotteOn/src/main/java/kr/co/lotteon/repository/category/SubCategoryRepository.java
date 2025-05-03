package kr.co.lotteon.repository.category;

import kr.co.lotteon.entity.category.MainCategory;
import kr.co.lotteon.entity.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer> {

    List<SubCategory> findByMainCategory(MainCategory main);

    void deleteByMainCategory(MainCategory mainCategory);

    List<SubCategory> findByMainCategoryAndStateOrderByOrderIndexAsc(MainCategory mainCategory, String 활성);
}
