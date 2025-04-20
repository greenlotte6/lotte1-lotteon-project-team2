package kr.co.lotteon.repository.point;

import kr.co.lotteon.entity.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Integer> {
}
