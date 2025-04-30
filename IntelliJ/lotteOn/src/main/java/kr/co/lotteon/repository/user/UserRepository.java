package kr.co.lotteon.repository.user;


import kr.co.lotteon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUid(String uid);    // 아이디 중복 체크용
    boolean existsByEmail(String email);    // 이메일 중복 체크도 가능

    Optional<User> findByNameAndHp(String name, String hp);

    Optional<User> findByUid(String uid);

    Optional<User> findByEmail(String email);

    Optional<User> findByUidAndPass(String uid, String pass); // ✅ 이거 추가



}


