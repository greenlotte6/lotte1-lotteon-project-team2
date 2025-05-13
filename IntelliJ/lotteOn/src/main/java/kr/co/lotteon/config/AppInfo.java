package kr.co.lotteon.config;

import jakarta.annotation.PostConstruct;
import kr.co.lotteon.dto.category.SubCategoryDTO;
import kr.co.lotteon.dto.config.ConfigDTO;
import kr.co.lotteon.dto.config.VersionDTO;
import kr.co.lotteon.entity.config.Config;
import kr.co.lotteon.entity.config.Version;
import kr.co.lotteon.repository.config.ConfigRepository;
import kr.co.lotteon.repository.config.VersionRepository;
import kr.co.lotteon.service.config.ConfigService;
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

    @Autowired
    private ConfigService configService;

    @Value("${spring.application.name}") //application.yml 파일에 속성값으로 초기화
    private String appName;

    // application의 버전 정보
    @Value("${spring.application.version}")
    private String appVersionSub;


    // 데이터베이스의 버전정보
    private String appVersion;

    private SubCategoryDTO subCategory;

    // 사이트 정보
    private String title;
    private String subTitle;
    private String copyright;
    private String headerLogo; //헤더 로고(브라우저 탭, 헤더 푸터 노출 로고)
    private String footerLogo; //푸터 로고(브라우저 탭, 헤더 푸터 노출 로고)
    private String favicon;    //파비콘

    /*
     * 기업 정보
     * */
    private String companyName; //상호명
    private String ceoName; //대표이사
    private String businessNumber; //사업자등록번호
    private String onlineSalesNumber; //통신판매업신고번호
    private String addr1; //기본주소
    private String addr2; //상세주소

    /*
     * 고객센터 정보/카피라이트
     * */
    private String hp; //전화번호
    private String workingHours; //업무시간
    private String email; //이메일
    private String efinDispute; //전자금융거래 분쟁담당

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
            companyName = config.getCompanyName();
            ceoName = config.getCeoName();
            businessNumber = config.getBusinessNumber();
            onlineSalesNumber = config.getOnlineSalesNumber();
            addr1 = config.getAddr1();
            addr2 = config.getAddr2();
            hp = config.getHp();
            workingHours = config.getWorkingHours();
            email = config.getEmail();
            efinDispute = config.getEfinDispute();
            headerLogo = config.getHeaderLogo();
            footerLogo = config.getFooterLogo();
            favicon = config.getFavicon();
        }else{
            title = "롯데온";
            subTitle = "2조";
            copyright = "카피라이트";
            companyName = "롯데쇼핑주식회사";
            ceoName="홍길동";
            businessNumber = "123-12-12345";
            onlineSalesNumber = "2024-서울강남-1234";
            addr1 = "서울특별시강남구테헤란로";
            addr2 = "(삼성동 , WeWork 빌딩) 7층";
            hp = "02-1234-5678";
            workingHours = "평일 09:00 ~ 18:00";
            email = "lotteon@lotte.net";
            efinDispute = "1234-1234";
        }

        if(version == null){
            appVersion = appVersionSub;
        }else {
            appVersion = version.getVersion();
        }
    }

    // 버전 변경 시
    public void chageVersion(){

        // 버전 캐싱 처리
        VersionDTO version = configService.findTopByOrderByWdateDesc();
        
        // 환경설정 캐싱 처리
        ConfigDTO config = configService.findById();
        // Optional<Config> configOpt = configRepository.findById(1);

        if(version == null){
            appVersion = appVersionSub;
        }else {
            appVersion = version.getVersion();
        }

        if(config != null){
            subTitle = config.getSubTitle();
            title = config.getTitle();
            copyright = config.getCopyright();
            companyName = config.getCompanyName();
            ceoName = config.getCeoName();
            businessNumber = config.getBusinessNumber();
            onlineSalesNumber = config.getOnlineSalesNumber();
            addr1 = config.getAddr1();
            addr2 = config.getAddr2();
            hp = config.getHp();
            workingHours = config.getWorkingHours();
            email = config.getEmail();
            efinDispute = config.getEfinDispute();
            headerLogo = config.getHeaderLogo();
            footerLogo = config.getFooterLogo();
            favicon = config.getFavicon();
        }else{
            title = "롯데온";
            subTitle = "2조";
            copyright = "카피라이트";
            companyName = "롯데쇼핑주식회사";
            ceoName="홍길동";
            businessNumber = "123-12-12345";
            onlineSalesNumber = "2024-서울강남-1234";
            addr1 = "서울특별시강남구테헤란로";
            addr2 = "(삼성동 , WeWork 빌딩) 7층";
            hp = "02-1234-5678";
            workingHours = "평일 09:00 ~ 18:00";
            email = "lotteon@lotte.net";
            efinDispute = "1234-1234";
        }
    }

    public void callCategory() {



    }
}
