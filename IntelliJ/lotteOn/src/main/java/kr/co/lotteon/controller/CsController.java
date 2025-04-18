package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CsController {

    @GetMapping("/cs/index")
    public String index() {
        return "/cs/index";
    }

    @GetMapping("/cs/notice/list")
    public String noticeList() {
        return "/cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String noticeView() {
        return "/cs/notice/view";
    }

    @GetMapping("/cs/faq/list")
    public String faqList() {
        return "/cs/faq/list";
    }

    @GetMapping("/cs/faq/view")
    public String faqView() {
        return "/cs/faq/view";
    }

    @GetMapping("/cs/qna/list")
    public String qnaList() {
        return "/cs/faq/list";
    }

    @GetMapping("/cs/qna/view")
    public String qnaView() {
        return "/cs/qna/view";
    }

    @GetMapping("/cs/qna/write")
    public String qnaWrite() {
        return "/cs/qna/write";
    }
}
