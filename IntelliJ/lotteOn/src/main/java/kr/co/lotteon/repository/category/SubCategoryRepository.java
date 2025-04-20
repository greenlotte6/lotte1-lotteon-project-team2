package kr.co.lotteon.repository.category;

import kr.co.lotteon.entity.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer> {
}
