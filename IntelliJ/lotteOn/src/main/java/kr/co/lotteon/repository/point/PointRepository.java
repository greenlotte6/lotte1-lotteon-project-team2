package kr.co.lotteon.repository.point;

import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PointRepository extends JpaRepository<Point, Integer> {

    @Query("SELECT p FROM Point p " +
            "WHERE p.user = :user " +
            "AND p.pointDesc LIKE %:desc% " +
            "AND p.pointDate BETWEEN :start AND :end")
    Page<Point> searchPoints(
            @Param("user") User user,
            @Param("desc") String desc,
            @Param("start") String startDate,
            @Param("end") String endDate,
            Pageable pageable
    );
    
    Page<Point> findAllByUser(User user, Pageable pageable);


    @Query("SELECT SUM(p.point) FROM Point p WHERE p.user = :user AND p.expiryDate > :now")
    Long getSumPointByUserAndExpiryDateAfter(@Param("user") User user, @Param("now") LocalDateTime now);
}
