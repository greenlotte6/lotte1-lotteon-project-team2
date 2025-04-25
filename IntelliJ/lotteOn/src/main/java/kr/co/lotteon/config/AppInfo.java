package kr.co.lotteon.config;

import jakarta.annotation.PostConstruct;
import kr.co.lotteon.entity.config.Version;
import kr.co.lotteon.repository.config.VersionRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppInfo {

    @Autowired
    private VersionRepository versionRepository;

    @Value("${spring.application.name}") //application.yml 파일에 속성값으로 초기화
    private String appName;

    // application의 버전 정보
    @Value("${spring.application.version}")
    private String appVersionSub;

    // 데이터베이스의 버전정보
    private String appVersion;
    @PostConstruct
    public void init(){
        Version version = versionRepository.findTopByOrderByWdateDesc();

        if(version == null){
            appVersion = appVersionSub;
        }else {
            appVersion = version.getVersion();
        }
    }

    public void chageVersion(){
        Version version = versionRepository.findTopByOrderByWdateDesc();

        if(version == null){
            appVersion = appVersionSub;
        }else {
            appVersion = version.getVersion();
        }
    }

}
