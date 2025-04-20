package kr.co.lotteon.repository.config;

import kr.co.lotteon.entity.config.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version,Integer> {
}
