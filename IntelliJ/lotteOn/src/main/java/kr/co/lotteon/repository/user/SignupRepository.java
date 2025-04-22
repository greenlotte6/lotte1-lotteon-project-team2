package kr.co.lotteon.repository.user;


import kr.co.lotteon.entity.user.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignupRepository extends JpaRepository<SignUp, Integer> {
}
