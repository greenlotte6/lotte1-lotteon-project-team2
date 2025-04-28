package kr.co.lotteon.controller.article;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.article.FaqDTO;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.service.article.CsService;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CsController {


    private final CsService csService;
    private final UserService userService;



    @GetMapping("/cs/index")
    public String index(PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(5);
        // 공지사항 리스트 출력
        PageResponseDTO<NoticeDTO> noticeResponseDTO = csService.noticeFindAll(pageRequestDTO);

        model.addAttribute("noticeResponseDTO", noticeResponseDTO);

        // 문의하기 리스트 출력
        PageResponseDTO<InquiryDTO> inquiryResponseDTO = csService.inquiryFindAll(pageRequestDTO);
        pageRequestDTO.setSize(5);

        model.addAttribute("inquiryResponseDTO", inquiryResponseDTO);
        return "/cs/index";
    }

    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, PageRequestDTO pageRequestDTO, @RequestParam("cate") String cate) {

        PageResponseDTO<NoticeDTO> responseDTO = csService.noticeFindAll(pageRequestDTO, cate);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("cate", cate);


        return "/cs/notice/list";
    }

    @GetMapping("/cs/notice/listAll")
    public String noticeListAll(Model model, PageRequestDTO pageRequestDTO) {

        pageRequestDTO.setSize(10);

        PageResponseDTO<NoticeDTO> responseDTO = csService.noticeFindAll(pageRequestDTO);


        model.addAttribute("responseDTO", responseDTO);

        return "/cs/notice/listAll";
    }


    @GetMapping("/cs/notice/view")
    public String noticeView(Model model, @RequestParam("no") int no) {

        NoticeDTO noticeDTO = csService.noticeFindById(no);

        model.addAttribute("noticeDTO", noticeDTO);


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
    public String faqList(Model model, PageRequestDTO pageRequestDTO, @RequestParam("cateV1") String cateV1) {

        log.info("cateV1: {}", cateV1);
        model.addAttribute("cateV1", cateV1);

        PageResponseDTO<FaqDTO> responseDTO = csService.faqFindAll(pageRequestDTO, cateV1);
        model.addAttribute("responseDTO", responseDTO);

        log.info("responseDTO: {}", responseDTO);

        // 2차 카테고리 목록 조회
        List<String> cateV2List = csService.findCateV2ListByCateV1(cateV1);
        model.addAttribute("cateV2List", cateV2List);

        log.info("cateV2List: {}", cateV2List);

        // 2차 카테고리별 리스트 조회
        Map<String, List<FaqDTO>> cateV2FaqMap = new LinkedHashMap<>();
        for(String cateV2 : cateV2List) {
            List<FaqDTO> faqDTOList = csService.faqFindAllByCateV1AndCateV2(cateV1, cateV2);
            cateV2FaqMap.put(cateV2, faqDTOList);
        }
        model.addAttribute("cateV2FaqMap", cateV2FaqMap);

        log.info("cateV2FaqMap: {}", cateV2FaqMap);


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
    public String qnaList(@RequestParam("cateV1") String cateV1, Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<InquiryDTO> responseDTO = csService.findCateV1All(pageRequestDTO, cateV1);
        model.addAttribute("cateV1", cateV1);
        model.addAttribute("responseDTO", responseDTO);
        return "/cs/qna/list";
    }

    @GetMapping("/cs/qna/view")
    public String qnaView(Model model, @RequestParam("no") int no) {

        InquiryDTO inquiryDTO = csService.findById(no);

        model.addAttribute("inquiryDTO", inquiryDTO);

        return "/cs/qna/view";
    }

    @GetMapping("/cs/qna/coupon")
    public String qnaCoupon(@RequestParam("cateV1") String cateV1,Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<InquiryDTO> responseDTO = csService.findCateV1All(pageRequestDTO, cateV1);
        model.addAttribute("cateV1", cateV1);
        model.addAttribute("responseDTO", responseDTO);

        return "/cs/qna/coupon";
    }

    @GetMapping("/cs/qna/order")
    public String qnaOrder() {
        return "/cs/qna/order";
    }

    @GetMapping("/cs/qna/delivery")
    public String qnaDelivery(@RequestParam("cateV1") String cateV1,Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<InquiryDTO> responseDTO = csService.findCateV1All(pageRequestDTO, cateV1);
        model.addAttribute("cateV1", cateV1);
        model.addAttribute("responseDTO", responseDTO);

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
    public String qnaSafeDeal(@RequestParam("cateV1") String cateV1,Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<InquiryDTO> responseDTO = csService.findCateV1All(pageRequestDTO, cateV1);
        model.addAttribute("cateV1", cateV1);
        model.addAttribute("responseDTO", responseDTO);

        return "/cs/qna/safeDeal";
    }

    @GetMapping("/cs/qna/write")
    public String qnaWrite() {
        return "/cs/qna/write";
    }

    // 문의하기 작성
    @PostMapping("/cs/qna/write")
    public String qnaWrite(InquiryDTO inquiryDTO, HttpServletRequest request, @RequestParam("writer") String uid, @RequestParam("cateV1") String cateV1) throws UnsupportedEncodingException {

        String regip = request.getRemoteAddr();
        inquiryDTO.setRegip(regip);

        // UserDTO 조회
        UserDTO user = userService.findById(uid);
        inquiryDTO.setUser(user);

        csService.register(inquiryDTO);

        log.info("cateV1: {}", cateV1);

        // 한글 파라미터 인코딩
        return "redirect:/cs/qna/list?cateV1=" + URLEncoder.encode(cateV1, "UTF-8");
    }


}
