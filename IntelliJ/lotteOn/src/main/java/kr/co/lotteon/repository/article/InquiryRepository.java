package kr.co.lotteon.repository.article;

import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.custom.InquiryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry,Integer>, InquiryRepositoryCustom {
    Page<Inquiry> findByCateV1(String cateV1, Pageable pageable);

    Page<Inquiry> findAllByUser(User user, Pageable pageable);

    long countByUserAndState(User user, String state);
}
