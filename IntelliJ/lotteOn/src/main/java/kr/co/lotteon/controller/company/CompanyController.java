package kr.co.lotteon.controller.company;

import kr.co.lotteon.dto.article.RecruitDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/company/index")
    public String index() {
        return "/company/index";
    }

    @GetMapping("/company/culture")
    public String culture() {
        return "/company/culture";
    }

    @GetMapping("/company/story")
    public String story() {
        return "/company/story";
    }

    @GetMapping("/company/recruit")
    public String recruit(Model model, PageRequestDTO pageRequestDTO) {
        PageResponseDTO pageResponseDTO = companyService.findAllRecruit(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/company/employment";
    }

    @GetMapping("/company/recruit/view")
    public String recruitView(Model model, @RequestParam("no") int no) {
        RecruitDTO recruitDTO = companyService.findByRecruitByNo(no);
        model.addAttribute("recruit", recruitDTO);
        return "/company/employmentView";
    }

    @GetMapping("/company/media")
    public String media() {
        return "/company/media";
    }
}
