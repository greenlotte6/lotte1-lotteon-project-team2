package kr.co.lotteon.scheduler;

import kr.co.lotteon.service.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class VisitorStatsScheduler {

    private final VisitorService visitorService;

    /**
     * 매일 자정에 실행되는 배치 작업
     * 전날의 방문자 통계를 데이터베이스에 저장
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void processDailyVisitorStats() {
        try {
            // 어제 날짜
            LocalDate yesterday = LocalDate.now().minusDays(1);

            // VisitorService를 통해 어제 방문자 수 조회
            int visitorCount = visitorService.getVisitorCountByDate(yesterday);

            // 데이터베이스에 저장
            visitorService.saveDailyStats(yesterday, visitorCount);

            log.info("일일 방문자 통계 처리 완료: 날짜={}", yesterday);

            // 오래된 Redis 데이터 삭제 (선택사항)
            // VisitorService에 메서드 추가 필요
            visitorService.deleteOldVisitorData(LocalDate.now().minusDays(7));

        } catch (Exception e) {
            log.error("일일 방문자 통계 처리 중 오류 발생", e);
        }
    }
}
