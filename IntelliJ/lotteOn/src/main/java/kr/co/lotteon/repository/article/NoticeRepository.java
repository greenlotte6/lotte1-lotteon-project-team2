package kr.co.lotteon.repository.article;

import kr.co.lotteon.entity.article.Inquiry;
import kr.co.lotteon.entity.article.Notice;
import kr.co.lotteon.repository.custom.NoticeRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Integer> , NoticeRepositoryCustom {
    Page<Notice> findByCate(Pageable pageable, String cate);

    Collection<Object> findTop5ByOrderByNoDesc();
}
