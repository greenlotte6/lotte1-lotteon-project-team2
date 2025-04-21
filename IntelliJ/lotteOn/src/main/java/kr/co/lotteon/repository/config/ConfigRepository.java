package kr.co.lotteon.repository.config;

import kr.co.lotteon.entity.config.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config,Integer> {
}
