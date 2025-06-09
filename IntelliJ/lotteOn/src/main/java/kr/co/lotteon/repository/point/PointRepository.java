package kr.co.lotteon.repository.point;

import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PointRepository extends JpaRepository<Point, Integer> {

    Page<Point> findAllByUser(User user, Pageable pageable);

    @Query("SELECT SUM(p.point) FROM Point p WHERE p.user = :user AND p.expiryDate > :now")
    Long getSumPointByUserAndExpiryDateAfter(@Param("user") User user, @Param("now") LocalDateTime now);

    @Query("SELECT SUM(p.point) FROM Point p WHERE p.user.uid = :uid AND p.pointNo > :pointNo")
    Integer findSumOfFuturePoints(String uid, String pointNo);

    @Query("SELECT SUM(p.point) FROM Point p WHERE p.user.uid = :uid")
    Integer findTotalPointByUid(String uid);

    Page<Point> findAllByUserAndPointDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
