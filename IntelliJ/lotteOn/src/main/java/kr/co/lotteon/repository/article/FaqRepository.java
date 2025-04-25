package kr.co.lotteon.repository.article;

import kr.co.lotteon.entity.article.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<Faq,Integer> {
    Page<Faq> findByCateV1(Pageable pageable, String cateV1);
}
