package kr.co.lotteon.repository.category;

import kr.co.lotteon.entity.category.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory,Integer> {

    List<MainCategory> findAllByOrderByOrderIndexAsc();

    List<MainCategory> findByStateOrderByOrderIndexAsc(String 활성);

    List<MainCategory> findByMainCategoryNameOrderByOrderIndexDesc(String mainName);
}
