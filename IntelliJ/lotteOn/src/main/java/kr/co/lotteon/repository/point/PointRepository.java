package kr.co.lotteon.repository.point;

import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Integer> {

    Page<Point> findAllByUser(User user, Pageable pageable);
}
