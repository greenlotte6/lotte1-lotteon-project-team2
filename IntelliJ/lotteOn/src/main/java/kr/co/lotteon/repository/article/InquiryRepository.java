package kr.co.lotteon.repository.article;

import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.repository.custom.InquiryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry,Integer>, InquiryRepositoryCustom {
}
