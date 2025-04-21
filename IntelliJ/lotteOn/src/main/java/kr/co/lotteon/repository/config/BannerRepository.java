package kr.co.lotteon.repository.config;

import kr.co.lotteon.entity.config.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Integer> {
}
