package kr.co.lotteon.controller.article;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.service.article.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CsController {

    private final CsService csService;




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

    @GetMapping("/cs/notice/service")
    public String noticeService() {
        return "/cs/notice/service";
    }

    @GetMapping("/cs/notice/safeDeal")
    public String noticeSafeDeal() {
        return "/cs/notice/safeDeal";
    }

    @GetMapping("/cs/notice/event")
    public String noticeEvent() {
        return "/cs/notice/event";
    }

    @GetMapping("/cs/notice/dangerProduct")
    public String noticeDangerProduct() {return "/cs/notice/dangerProduct";}


    @GetMapping("/cs/faq/list")
    public String faqList() {
        return "/cs/faq/list";
    }

    @GetMapping("/cs/faq/view")
    public String faqView() {
        return "/cs/faq/view";
    }

    @GetMapping("/cs/faq/coupon")
    public String faqCoupon() {
        return "/cs/faq/coupon";
    }

    @GetMapping("/cs/faq/order")
    public String faqOrder() {
        return "/cs/faq/order";
    }

    @GetMapping("/cs/faq/delivery")
    public String faqDelivery() {
        return "/cs/faq/delivery";
    }

    @GetMapping("/cs/faq/cancel")
    public String faqCancel() {
        return "/cs/faq/cancel";
    }

    @GetMapping("/cs/faq/trip")
    public String faqTrip() {
        return "/cs/faq/trip";
    }

    @GetMapping("/cs/faq/safeDeal")
    public String faqSafeDeal() {
        return "/cs/faq/safeDeal";
    }





    @GetMapping("/cs/qna/list")
    public String qnaList() {return "/cs/qna/list";}

    @GetMapping("/cs/qna/view")
    public String qnaView() {
        return "/cs/qna/view";
    }

    @GetMapping("/cs/qna/coupon")
    public String qnaCoupon() {
        return "/cs/qna/coupon";
    }

    @GetMapping("/cs/qna/order")
    public String qnaOrder() {
        return "/cs/qna/order";
    }

    @GetMapping("/cs/qna/delivery")
    public String qnaDelivery() {
        return "/cs/qna/delivery";
    }

    @GetMapping("/cs/qna/cancel")
    public String qnaCancel() {
        return "/cs/qna/cancel";
    }

    @GetMapping("/cs/qna/trip")
    public String qnaTrip() {
        return "/cs/qna/trip";
    }

    @GetMapping("/cs/qna/safeDeal")
    public String qnaSafeDeal() {
        return "/cs/qna/safeDeal";
    }

    @GetMapping("/cs/qna/write")
    public String qnaWrite() {
        return "/cs/qna/write";
    }


    @PostMapping("/cs/qna/write")
    public String qnaWrite(InquiryDTO inquiryDTO, Model model){

        model.addAttribute("username", csService.userGet());

        csService.register(inquiryDTO);

        return "redirect:/cs/qna/list";
    }


}
