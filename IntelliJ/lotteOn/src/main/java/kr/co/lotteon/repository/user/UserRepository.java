package kr.co.lotteon.repository.user;


import kr.co.lotteon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUid(String uid);    // 아이디 중복 체크용
    boolean existsByEmail(String email);    // 이메일 중복 체크도 가능

}


