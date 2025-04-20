package kr.co.lotteon.repository.article;

import kr.co.lotteon.entity.article.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Integer> {
}
