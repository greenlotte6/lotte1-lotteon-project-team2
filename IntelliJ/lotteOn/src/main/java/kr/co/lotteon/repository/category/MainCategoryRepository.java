package kr.co.lotteon.repository.category;

import kr.co.lotteon.entity.category.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory,Integer> {
}
