package kr.co.lotteon.repository.feedback;

import kr.co.lotteon.entity.feedback.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRepository extends JpaRepository<Return,Integer> {
}
