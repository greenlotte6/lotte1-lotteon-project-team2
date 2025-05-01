package kr.co.lotteon.controller;

import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.service.config.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {

    private final ConfigService configService;

    @GetMapping("/")
    public String home(Model model){
        BannerDTO banner = configService.findBanner("MAIN1");
        List<BannerDTO> bannerDTOS = configService.findBannerByCate("MAIN2");

        model.addAttribute("banner", banner);
        model.addAttribute("bannerDTOS", bannerDTOS);

        System.out.println(bannerDTOS);

        return "/index";
    }

}
