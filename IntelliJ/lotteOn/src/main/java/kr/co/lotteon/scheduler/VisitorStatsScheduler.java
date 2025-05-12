package kr.co.lotteon.scheduler;

import kr.co.lotteon.service.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class VisitorStatsScheduler {

    private final VisitorService visitorService;
    private final RedisTemplate<String, String> redisTemplate;

    // 테스트용 실행 횟수 제한
    private final AtomicInteger testRunCount = new AtomicInteger(0);

    /**
     * 매일 자정에 실행되는 배치 작업 (운영용)
     * 전날의 방문자 통계를 데이터베이스에 저장
     */
    // 테스트가 끝나면 이 주석을 해제하고 아래의 테스트 스케줄러는 주석 처리하세요
     @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void processDailyVisitorStats() {
         log.info("=== 일일 방문자 통계 스케줄러 실행 ===");
        executeVisitorStatsProcess();
    }

    /**
     * 테스트용: 1분마다 실행 (최대 10회)
     * 테스트 완료 후 이 메서드를 주석 처리하고 위의 자정 스케줄러를 활성화하세요
     */
//    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
//    public void testDailyVisitorStats() {
//
//
//        // 10회 실행 후 종료
//     if (currentCount > 10) {
//            log.info("테스트 완료 - 10회 실행 후 종료");
//            return;
//        }
//
//        executeVisitorStatsProcess();
//    }

    /**
     * 실제 처리 로직
     * 운영용과 테스트용 스케줄러가 공통으로 사용
     */
    private void executeVisitorStatsProcess() {
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            log.info("방문자 통계 처리 시작 - 대상 날짜: {}", yesterday);

            // 1. Redis에서 직접 데이터 확인 (디버깅용)
            String redisKey = String.format("visitor:%s", yesterday);
            boolean keyExists = redisTemplate.hasKey(redisKey);
            Long redisSize = redisTemplate.opsForSet().size(redisKey);
            log.info("Redis 키 '{}' - 존재: {}, 크기: {}", redisKey, keyExists, redisSize);

            // 2. 현재 모든 Redis 키 확인 (디버깅용)
            Set<String> allKeys = redisTemplate.keys("visitor:*");
            log.info("현재 Redis의 모든 방문자 키: {}", allKeys);

            // 3. VisitorService를 통해 방문자 수 조회
            int visitorCount = visitorService.getVisitorCountByDate(yesterday);
            log.info("서비스에서 조회된 방문자 수: {}", visitorCount);

            // 4. 방문자 수가 0이 아닐 때만 저장
            if (visitorCount > 0) {
                visitorService.saveDailyStats(yesterday, visitorCount);
                log.info("방문자 통계 저장 완료: 날짜={}, 방문자수={}", yesterday, visitorCount);

                // 오래된 Redis 데이터 삭제 (7일 이상 된 데이터)
                LocalDate cutoffDate = LocalDate.now().minusDays(7);
                visitorService.deleteOldVisitorData(cutoffDate);
                log.info("오래된 Redis 데이터 삭제: {} 이전 데이터", cutoffDate);

            } else {
                log.warn("방문자 수가 0명입니다. 저장하지 않습니다.");

                // 추가 디버깅 정보
                if (!keyExists) {
                    log.warn("Redis에 해당 날짜의 키가 존재하지 않습니다: {}", redisKey);
                } else {
                    log.warn("Redis 키는 존재하지만 크기가 0입니다");
                }
            }

        } catch (Exception e) {
            log.error("일일 방문자 통계 처리 중 오류 발생", e);
            // 에러 내용을 더 자세히 로그
            log.error("오류 스택 트레이스", e);
        }
    }

    /**
     * 테스트 카운터 리셋 (필요시 수동 호출)
     */
    public void resetTestCounter() {
        testRunCount.set(0);
        log.info("테스트 카운터가 리셋되었습니다");
    }
}