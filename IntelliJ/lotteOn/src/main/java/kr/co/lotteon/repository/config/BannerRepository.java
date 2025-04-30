package kr.co.lotteon.repository.config;

import kr.co.lotteon.entity.config.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Integer> {

    List<Banner> findByCateAndEndDay(String cate, LocalDate now);


    List<Banner> findByCateAndEndDayGreaterThan(String cate, LocalDate endDayIsGreaterThan);

    @Query("""
    SELECT b FROM Banner b 
    WHERE b.state = '활성' 
      AND b.cate = :cate 
      AND (CURRENT_DATE > b.startDay OR (CURRENT_DATE = b.startDay AND CURRENT_TIME >= b.startTime)) 
      AND (CURRENT_DATE < b.endDay OR (CURRENT_DATE = b.endDay AND CURRENT_TIME <= b.endTime)) 
    ORDER BY b.bno DESC
    """)
    List<Banner> findBannerByCate(@Param("cate") String cate);
}
