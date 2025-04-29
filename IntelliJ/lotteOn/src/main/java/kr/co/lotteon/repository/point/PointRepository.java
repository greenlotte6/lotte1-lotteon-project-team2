package kr.co.lotteon.repository.point;

import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Integer> {

    Page<Point> findAllByUser(User user, Pageable pageable);

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
}
