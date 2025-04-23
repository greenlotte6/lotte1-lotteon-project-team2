package kr.co.lotteon.repository.article;

import kr.co.lotteon.entity.article.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry,Integer>{

    Page<Inquiry> findByCateV1(String cateV1, Pageable pageable);
}
