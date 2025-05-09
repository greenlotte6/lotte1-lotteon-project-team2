package kr.co.lotteon.repository.visitor;

import kr.co.lotteon.entity.visitor.DailyVisitorStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyVisitorStatsRepository extends JpaRepository<DailyVisitorStats, Long> {

    Optional<DailyVisitorStats> findByVisitDate(LocalDate visitDate);

    List<DailyVisitorStats> findByVisitDateBetweenOrderByVisitDateDesc(LocalDate startDate, LocalDate endDate);

    List<DailyVisitorStats> findByVisitDateBetween(LocalDate start, LocalDate end);
}
