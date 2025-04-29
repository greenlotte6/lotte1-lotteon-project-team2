package kr.co.lotteon.config;

import jakarta.annotation.PostConstruct;
import kr.co.lotteon.entity.config.Config;
import kr.co.lotteon.entity.config.Version;
import kr.co.lotteon.repository.config.ConfigRepository;
import kr.co.lotteon.repository.config.VersionRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppInfo {

    @Autowired
    private VersionRepository versionRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Value("${spring.application.name}") //application.yml 파일에 속성값으로 초기화
    private String appName;

    // application의 버전 정보
    @Value("${spring.application.version}")
    private String appVersionSub;


    // 데이터베이스의 버전정보
    private String appVersion;
    private String title;
    private String subTitle;
    private String copyright;

    // 처음 시작
    @PostConstruct
    public void init(){
        Version version = versionRepository.findTopByOrderByWdateDesc();
        Optional<Config> configOpt = configRepository.findById(1);

        if(configOpt.isPresent()){
            Config config = configOpt.get();
            subTitle = config.getSubTitle();
            title = config.getTitle();
            copyright = config.getCopyright();
        }else{
            title = "롯데온";
            subTitle = "2조";
            copyright = "카피라이트";
        }

        if(version == null){
            appVersion = appVersionSub;
        }else {
            appVersion = version.getVersion();
        }
    }

    // 버전 변경 시
    public void chageVersion(){
        Version version = versionRepository.findTopByOrderByWdateDesc();
        Optional<Config> configOpt = configRepository.findById(1);

        if(version == null){
            appVersion = appVersionSub;
        }else {
            appVersion = version.getVersion();
        }

        if(configOpt.isPresent()){
            Config config = configOpt.get();
            subTitle = config.getSubTitle();
            title = config.getTitle();
            copyright = config.getCopyright();
        }else{
            title = "롯데온";
            subTitle = "2조";
            copyright = "카피라이트";
        }
    }

}
