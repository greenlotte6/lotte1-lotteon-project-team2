package kr.co.lotteon.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.config.TermsDTO;
import kr.co.lotteon.dto.config.VersionDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.service.admin.adminService;
import kr.co.lotteon.service.article.CsService;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.product.ImageService;
import kr.co.lotteon.service.product.ProductService;
import kr.co.lotteon.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SellerService sellerService;
    private final ImageService imageService;
    private final ConfigService configService;
    private final ProductService productService;
    private final adminService adminService;
    private final CsService csService;

    // 관리자 메인
    @GetMapping
    public String main() {
        return "/admin/main";
    }

    /*
     * 환경설정 기본설정
     * */

    //기본설정
    @GetMapping("/config/basic")
    public String config() {
        return "/admin/config/basic";
    }

    //배너설정
    @GetMapping("/config/banner")
    public String banner() {
        return "/admin/config/banner";
    }

    //약관관리
    @GetMapping("/config/policy")
    public String policy(Model model) {

        TermsDTO termsDTO = configService.findTerms();
        model.addAttribute("terms", termsDTO);
        return "/admin/config/policy";
    }

    //약관변경
    @ResponseBody
    @PostMapping("/terms/modify")
    public void modifyTerms(@RequestBody Map<String, String> payload) {
        String cate = payload.get("cate");
        String content = payload.get("content");

        configService.modify(cate, content);

    }




    // 카테고리 (미완성)
    @GetMapping("/config/category")
    public String category() {
        return "/admin/config/category";
    }

    // 버전관리
    @GetMapping("/config/version")
    public String version(PageRequestDTO pageRequestDTO, Model model) {

        PageResponseDTO pageResponseDTO = configService.selectAll(pageRequestDTO);
        model.addAttribute("verions",pageResponseDTO);

        return "/admin/config/version";
    }

    // 버전등록
    @PostMapping("/config/version/register")
    public String registerVersion(VersionDTO versionDTO, @AuthenticationPrincipal UserDetails userDetails) {

        configService.saveVersion(versionDTO, userDetails);
        return "redirect:/admin/config/version";
    }

    // 버전삭제
    @PostMapping("/config/version/delete")
    public String deleteVersions(@RequestParam("deleteVno") List<Integer> deleteVnos) {
        configService.deleteVersions(deleteVnos);
        return "redirect:/admin/config/version";
    }



    /*
     * 관리자 상점목록
     * */

    //상점 목록
    @GetMapping("/shop/list")
    public String shopList(){
        return "/admin/shop/list";
    }

    //관리자 매출현황
    @GetMapping("/shop/sales")
    public String shopSales(){
        return "/admin/shop/sales";
    }

    //판매자 등록/상점 등록
    @PostMapping("/shop/register")
    public String shopRegister(UserDTO userDTO, SellerDTO sellerDTO, HttpServletRequest req){

        String regip = req.getRemoteAddr();
        userDTO.setRegip(regip);
        sellerService.saveSeller(userDTO, sellerDTO);

        return "/admin/main";
    }

    /*
     * 관리자 회원목록
     * */

    //회원목록
    @GetMapping("/member/list")
    public String memberList(){
        return "/admin/member/list";
    }

    //포인트관리
    @GetMapping("/member/point")
    public String memberPoint(){
        return "/admin/member/point";
    }

    /*
     * 관리자 상품 목록
     * */

    // 상품현황
    @GetMapping("/product/list")
    public String productList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectAllForList(pageRequestDTO);

        model.addAttribute(pageResponseDTO);
        return "/admin/product/list";
    }

    @GetMapping("/product/delete")
    public String productDelete(@RequestParam("no") String no){

        adminService.deleteProduct(no);
        return "redirect:/admin/product/list";
    }

    @GetMapping("/product/register")
    public String productRegister(){
        return "/admin/product/register";
    }

    @PostMapping("/product/register")
    public String productRegister(ProductDTO productDTO, ProductDetailDTO productDetailDTO, ProductImageDTO productImageDTO){

        // 상품 저장
        Product savedProduct = adminService.saveProduct(productDTO);

        // 이미지 저장
        imageService.saveImage(productImageDTO, savedProduct);

        // 상품 상세 정보 저장
        adminService.saveProductDetail(productDetailDTO, savedProduct);

        return "redirect:/admin/product/register";
    }

    /*
     * 관리자 주문현황
     * */

    //주문현황
    @GetMapping("/order/list")
    public String orderList(){
        return "/admin/order/list";
    }

    //주문현황
    @GetMapping("/order/delivery")
    public String delivery(){
        return "/admin/order/delivery";
    }

    /*
     * 관리자 쿠폰 목록
     * */

    //쿠폰목록
    @GetMapping("/coupon/list")
    public String couponList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectAllForCoupon(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/coupon/list";
    }

    //쿠폰발급목록 (유저가 발급한 쿠폰)
    @GetMapping("/coupon/issued")
    public String issued(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectAllForIssuedCoupon(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/coupon/issued";
    }

    //쿠폰발급목록 (검색)
    @GetMapping("/coupon/issued/search")
    public String issuedSearch(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectAllForIssuedCoupon(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        System.out.println(pageResponseDTO);
        return "/admin/coupon/issuedSearch";
    }

    //쿠폰등록
    @PostMapping("/coupon/register")
    public String reigster(CouponDTO couponDTO, @AuthenticationPrincipal UserDetails userDetails){
        adminService.saveCoupon(couponDTO,userDetails);
        return "redirect:/admin/coupon/list";
    }

    // 쿠폰만료시키기
    @GetMapping("/coupon/expiry")
    public String couponExpiry(@RequestParam("cno") Long cno){
        adminService.ExpiryCoupon(cno);
        return "redirect:/admin/coupon/list";
    }

    // 발급된 쿠폰 만료 시키기
    @GetMapping("/couponIssue")
    public String couponIssue(@RequestParam("issueNo") Long issueNo){
        adminService.ExpiryCouponIssue(issueNo);
        return "redirect:/admin/coupon/issued";
    }

    


    /*
     * 관리자 고객센터 목록 (공지사항/자주묻는질문/문의하기/채용하기)
     * */

    //공지사항
    @GetMapping("/cs/notice/list")
    public String noticeList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.findAllNotice(pageRequestDTO);
        model.addAttribute(pageResponseDTO);

        return "/admin/notice/list";
    }

    @GetMapping("/cs/notice/write")
    public String noticeWrite(){
        return "/admin/notice/write";
    }

    @PostMapping("/cs/notice/write")
    public String  noticeWrite(NoticeDTO noticeDTO,
                               @AuthenticationPrincipal UserDetails userDetails,
                               HttpServletRequest req){

        noticeDTO.setRegip(req.getRemoteAddr());
        adminService.saveNotice(noticeDTO ,userDetails);

        return "redirect:/admin/cs/notice/list";
    }

    @GetMapping("/cs/notice/view")
    public String noticeView(Model model, @RequestParam("no") String no){
        NoticeDTO noticeDTO = adminService.findNoticeByNo(no);
        model.addAttribute(noticeDTO);
        return "/admin/notice/view";
    }

    @GetMapping("/cs/notice/modify")
    public String noticeModify(@RequestParam("no") String no, Model model){

        NoticeDTO noticeDTO =  adminService.findNoticeByNo(no);
        model.addAttribute(noticeDTO);
        return "/admin/notice/modify";
    }

    @PostMapping("/cs/notice/modify")
    public String  noticeModify(NoticeDTO noticeDTO){
        System.out.println(noticeDTO);
        System.out.println(noticeDTO);
        System.out.println(noticeDTO);
        adminService.modify(noticeDTO);
        return "redirect:/admin/cs/notice/list";
    }


    @GetMapping("/cs/notice/delete")
    public String noticeDelete(@RequestParam("no") String no){
        adminService.deleteNoticeByNo(no);
        return "redirect:/admin/cs/notice/list";
    }

    @GetMapping("/cs/notice/deleteList")
    public String noticeDeleteList(@RequestParam("deleteNo") List<Integer> deleteNos) {
        adminService.deleteNoticeByList(deleteNos);
        return "redirect:/admin/cs/notice/list";
    }


    //자주묻는질문
    @GetMapping("/cs/faq/list")
    public String faqList(){
        return "/admin/faq/list";
    }

    //문의하기 목록
    @GetMapping("/cs/qna/list")
    public String qnaList(Model model, PageRequestDTO pageRequestDTO){

        PageResponseDTO<InquiryDTO> responseDTO = csService.adminFindAll(pageRequestDTO);

        model.addAttribute("responseDTO", responseDTO);

        return "/admin/qna/list";
    }

    //채용하기 목록
    @GetMapping("/cs/recruit/list")
    public String recruitList(){
        return "/admin/recruit/list";
    }

















}
