package kr.co.lotteon.repository.config;

import kr.co.lotteon.entity.config.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Integer> {

    List<Banner> findByCateAndEndDay(String cate, LocalDate now);


    List<Banner> findByCateAndEndDayGreaterThan(String cate, LocalDate endDayIsGreaterThan);
}
