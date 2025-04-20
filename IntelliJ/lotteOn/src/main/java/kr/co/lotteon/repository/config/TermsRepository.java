package kr.co.lotteon.repository.config;

import kr.co.lotteon.entity.config.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms,Integer> {
}
