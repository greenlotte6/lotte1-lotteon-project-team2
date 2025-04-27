package kr.co.lotteon.repository.article;

import kr.co.lotteon.entity.article.Recruit;
import kr.co.lotteon.repository.custom.RecruitRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit,Integer> , RecruitRepositoryCustom {
}
