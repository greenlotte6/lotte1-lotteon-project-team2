package kr.co.lotteon.service.visitor;

import kr.co.lotteon.entity.visitor.DailyVisitorStats;
import kr.co.lotteon.repository.visitor.DailyVisitorStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitorService {

    private final RedisTemplate<String, String> redisTemplate;
    private final DailyVisitorStatsRepository visitorStatsRepository;

    private static final String VISITOR_KEY = "visitor:%s";


    /**
     * 방문자 추적 (중복 방문 체크)
     * @param visitorId 방문자 식별자
     * @return 새 방문자인지 여부
     */

    public boolean trackVisitor(String visitorId) {
        try {
            LocalDate today = LocalDate.now();
            String dateStr = today.toString();
            String key = String.format(VISITOR_KEY, dateStr);

            // Redis Set에 방문자 ID 추가 (이미 있으면 false 반환)
            Long addResult =  redisTemplate.opsForSet().add(key, visitorId);
            boolean isNewVisitor = addResult != null && addResult > 0;

            return isNewVisitor;
        } catch (Exception e) {
            log.error("방문자 추적 중 오류 발생", e);
            return false;
        }
    }

/**
 * 오늘의 고유 방문자 수 조회
 * @return 방문자 수
 */
    public int getTodayVisitorCount() {
        LocalDate today = LocalDate.now();
        String dateStr = today.toString();
        String key = String.format(VISITOR_KEY, dateStr);

        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size.intValue() : 0;
    }




    /**
     * 특정 날짜의 방문자 통계 조회
     * @param date 조회할 날짜
     * @return 방문자 통계
     */

    public int getVisitorCountByDate(LocalDate date) {
        // 먼저 Redis에서 확인 (오늘, 어제 모두)
        String key = String.format(VISITOR_KEY, date.toString());
        Long size = redisTemplate.opsForSet().size(key);
        int redisCount = size != null ? size.intValue() : 0;

        if (redisCount > 0) {
            log.info("Redis에서 {} 날짜의 방문자 수 조회: {}", date, redisCount);
            return redisCount;
        }

        // Redis에 없으면 DB에서 확인
        Optional<DailyVisitorStats> stats = visitorStatsRepository.findByVisitDate(date);
        int dbCount = stats.map(DailyVisitorStats::getUniqueVisitors).orElse(0);
        log.info("DB에서 {} 날짜의 방문자 수 조회: {}", date, dbCount);
        return dbCount;
    }


    /**
     * 기간별 방문자 통계 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 날짜별 방문자 통계
     */
    public List<DailyVisitorStats> getVisitorStatsByPeriod(LocalDate startDate, LocalDate endDate) {
        return visitorStatsRepository.findByVisitDateBetweenOrderByVisitDateDesc(startDate, endDate);
    }

    /**
     * 특정 날짜의 방문자 통계를 데이터베이스에 저장
     * @param date 날짜
     * @param visitorCount 방문자 수
     */
    public void saveDailyStats(LocalDate date , int visitorCount) {
        visitorStatsRepository.findByVisitDate(date)
                .ifPresentOrElse(
                        stats -> {
                            stats.setUniqueVisitors(visitorCount);
                            visitorStatsRepository.save(stats);
                            log.info("방문자 통계 업데이트 : 날짜 ={} , 방문자수={}", date, visitorCount);
                        },
                        // 없으면 새로 생성
                        () ->  {
                            DailyVisitorStats stats = DailyVisitorStats.builder()
                                    .visitDate(date)
                                    .uniqueVisitors(visitorCount)
                                    .build();
                            visitorStatsRepository.save(stats);
                            log.info("방문자 통계 저장 : 날짜={}, 방문자수={}", date, visitorCount);
                        }
                );
    }


    /**
     * 총 방문자 수 집계
     */
    public int getTotalVisitorCount() {

        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        // 데이터베이스에서 모든 방문자 통계 조회 후 합계 계산 (일주일 통계)
        List<DailyVisitorStats> allStats = visitorStatsRepository.findByVisitDateBetween(start, end);
        return allStats.stream()
                .mapToInt(DailyVisitorStats::getUniqueVisitors)
                .sum();
    }


    /**
     * 오래된 방문자 데이터 삭제
     * @param date 이 날짜 이전의 데이터 삭제
     */
    public void deleteOldVisitorData(LocalDate date) {
        String key = String.format(VISITOR_KEY, date.toString());
        redisTemplate.delete(key);
        log.info("오래된 방문자 데이터 삭제: {}", key);
    }

}
