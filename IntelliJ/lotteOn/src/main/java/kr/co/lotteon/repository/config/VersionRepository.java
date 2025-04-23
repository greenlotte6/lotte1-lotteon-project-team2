package kr.co.lotteon.repository.config;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.config.Version;
import kr.co.lotteon.repository.custom.VersionRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version,Integer>, VersionRepositoryCustom {
    Version findTopByOrderByWdateDesc();
}
