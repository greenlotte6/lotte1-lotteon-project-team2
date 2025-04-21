package kr.co.lotteon.repository.feedback;

import kr.co.lotteon.entity.feedback.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange,Integer> {
}
