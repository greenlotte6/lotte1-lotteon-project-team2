package kr.co.lotteon.repository.user;

import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> , UserRepositoryCustom {
    boolean existsByUid(String uid);    // 아이디 중복 체크용

    Optional<User> findByNameAndHp(String name, String hp);

    Optional<User> findByUid(String uid);

    Optional<User> findByEmail(String email);


    @Query("SELECT COUNT(u) FROM User u WHERE u.regDate BETWEEN :start AND :end")
    long countByRegDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Optional<Object> findByHp(String hp);
}


