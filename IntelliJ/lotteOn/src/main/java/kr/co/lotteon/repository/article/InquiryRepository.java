package kr.co.lotteon.repository.article;

import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.custom.InquiryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry,Integer>, InquiryRepositoryCustom {
    Page<Inquiry> findByCateV1(String cateV1, Pageable pageable);

    Page<Inquiry> findAllByUser(User user, Pageable pageable);

    long countByUserAndState(User user, String state);

    Collection<Object> findTop5ByOrderByNoDesc();

    long countByWdate(LocalDate now);

    @Query("SELECT COUNT(i) FROM Inquiry i WHERE i.wdate BETWEEN :start AND :end")
    long countByWdateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
