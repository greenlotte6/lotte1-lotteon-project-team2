package kr.co.lotteon.repository.user;

import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,Integer> {
    UserDetails findByUser(User user);
}
