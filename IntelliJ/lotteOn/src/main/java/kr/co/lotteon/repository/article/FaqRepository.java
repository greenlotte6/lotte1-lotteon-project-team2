package kr.co.lotteon.repository.article;

import kr.co.lotteon.dto.article.FaqDTO;
import kr.co.lotteon.entity.article.Faq;
import kr.co.lotteon.repository.custom.FaqRepositoryCustom;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq,Integer> , FaqRepositoryCustom {
    Page<Faq> findByCateV1(Pageable pageable, String cateV1);

    // 2차 카테고리 목록 조회
    @Query("SELECT DISTINCT f.cateV2 FROM Faq f WHERE f.cateV1 = :cateV1")
    List<String> findDistinctCateV2ByCateV1(@Param("cateV1") String cateV1);

    // 2차 카테고리별 리스트 조회
    List<Faq> findByCateV1AndCateV2(String cateV1, String cateV2);

    int countByCateV2(String cateV2);
}
