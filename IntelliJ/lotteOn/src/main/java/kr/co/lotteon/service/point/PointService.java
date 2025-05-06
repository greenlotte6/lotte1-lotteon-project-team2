package kr.co.lotteon.service.point;

import kr.co.lotteon.repository.point.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    public void changePoint(int usedPoint,int earnedPoint, UserDetails userDetails) {

    }


}
