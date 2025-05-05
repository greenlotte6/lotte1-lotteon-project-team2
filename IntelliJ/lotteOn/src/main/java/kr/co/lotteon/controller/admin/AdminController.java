package kr.co.lotteon.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.article.FaqDTO;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.article.NoticeDTO;
import kr.co.lotteon.dto.article.RecruitDTO;
import kr.co.lotteon.dto.category.MainCategoryDTO;
import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.config.ConfigDTO;
import kr.co.lotteon.dto.config.TermsDTO;
import kr.co.lotteon.dto.config.VersionDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
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
    public String config(Model model) {
        ConfigDTO configDTO = configService.findSite();
        model.addAttribute("config", configDTO);
        return "/admin/config/basic";
    }

    // 사이트 설정 정보 변경(제목, 소제목)
    @PostMapping("/config/site/modify")
    public String modifyConfig(@RequestParam("title") String title
                            , @RequestParam("subTitle") String subTitle) {
        configService.modifyTitleAndSubTitle(title, subTitle);
        return "redirect:/admin/config/basic";
    }

    // 사이트 설정 카피라이트 변경
    @PostMapping("/config/copyright/modify")
    public String modifyConfig(@RequestParam("copyright") String copyright) {
        configService.modifyCopyright(copyright);
        return "redirect:/admin/config/basic";
    }

    // 사이트 설정 회사 변경
    @PostMapping("/config/company/modify")
    public String modifyCompany(ConfigDTO configDTO) {
        configService.modifyCompany(configDTO);
        return "redirect:/admin/config/basic";
    }

    // 사이트 설정 고객센터 변경
    @PostMapping("/config/customer/modify")
    public String modifyCustomer(ConfigDTO configDTO) {
        configService.modifyCustomer(configDTO);
        return "redirect:/admin/config/basic";
    }

    // 사이트 헤더, 푸터, 파비콘 이미지 변경
    @PostMapping("/config/image/modify")
    public String modifyImage(ConfigDTO configDTO) {
        imageService.modifyConfigImage(configDTO);
        return "redirect:/admin/config/basic";
    }

    /*
    * 배너
    * */

    //배너설정
    @GetMapping("/config/banner")
    public String banner(@RequestParam(value = "cate", required = false) String cate, Model model) {

        configService.deleteBannerTimeout();
        String title = configService.SelectTitle(cate);
        List<BannerDTO> bannerDTOS = configService.findConfigBannerByCate(cate);
        model.addAttribute("title",title);
        model.addAttribute("bannerDTOS", bannerDTOS);
        return "/admin/config/banner";
    }
    
    
    //배너 설정하기
    @PostMapping("/config/banner/register")
    public String bannerRegister(BannerDTO bannerDTO) {

        // 이미지 저장 메서드
        BannerDTO newBannerDTO = imageService.saveBanner(bannerDTO);
        
        // 배너 저장 메서드
        configService.saveBanner(bannerDTO);
        return "redirect:/admin/config/banner";
    }

    // 배너 변경하기(활성/비활성)
    @GetMapping("/config/banner/change")
    public String bannerChange(@RequestParam("bno") int bno
                                , @RequestParam("cate") String cate
                                , @RequestParam("state") String state) {
        configService.changeBanner(bno, state);
        return "redirect:/admin/config/banner?cate=" + cate;
    }

    // 배너 삭제하기
    // 버전삭제
    @GetMapping("/config/banner/delete")
    public String deleteBanners(@RequestParam("deleteNo") List<Integer> deleteVnos) {

        //배너 이미지 지우기
        imageService.deleteBanner(deleteVnos);

        //배너 지우고
        configService.deleteBanner(deleteVnos);
        return "redirect:/admin/config/banner";
    }



    /*
    * 약관
    * */

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

    /*
    * 카테고리
    * */
    
    // 카테고리
    @GetMapping("/config/category")
    public String category(Model model) {

        List<MainCategoryDTO> categoryDTOS = configService.findAllCateGory();
        model.addAttribute("categoryDTOS", categoryDTOS);
        return "/admin/config/category";
    }

    // 메인 카테고리 등록
    @PostMapping("/config/category")
    public String category(@RequestParam("mainCateNo") List<Integer> mainCateNo,
                           @RequestParam("subCateNo") List<Integer> subCateNo
                        ,@RequestParam(value = "newCateNo", required = false) List<String> newCateNos
                        ,@RequestParam(value = "newSubCateNo", required = false) List<String> newSubCateNos
                        ,@RequestParam(value = "newSubCateNoV2", required = false) List<String> newSubCateNosV2) {

        // 카테고리 정렬(메인)
        int index = configService.sortOrderMain(mainCateNo);

        // 카테고리 추가
        configService.saveNewMain(newCateNos, index);

        // 카테고리 정렬(서브)
        index = configService.sortOrderSub(subCateNo);

        // 카테고리 추가(서브)
        if(newSubCateNos != null){
            index = configService.saveNewSub(newSubCateNos, index);
        }

        // 신규 메인 카테고리에 신규 서브 카테고리 추가
        if(newSubCateNosV2 != null){
            configService.saveNewSubV2(newSubCateNosV2, index);
        }
        System.out.println(newSubCateNosV2);

        return "redirect:/admin/config/category";

    }

    // 메인 카테고리 삭제
    @PostMapping("/config/category/delete/main")
    @ResponseBody
    public String deleteMainCategory(@RequestParam("mainCateNo") int mainCateNo) {
        configService.deleteMainCategory(mainCateNo);
        return "ok";
    }

    // 서브 카테고리 삭제
    @PostMapping("/config/category/delete/sub")
    @ResponseBody
    public String deleteSubCategory(@RequestParam("subCateNo") int subCateNo) {
        configService.deleteSubCategory(subCateNo);
        return"ok";
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
    public String shopList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectAllForSeller(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/shop/list";
    }

    //상점 변경하기

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

        return "redirect:/admin/shop/list";
    }

    //판매자 등급 변경
    @GetMapping("/shop/modify")
    public String modify(@RequestParam("uid") String uid,
                         @RequestParam("state") String state){
        adminService.modifySellerState(uid, state);
        return "redirect:/admin/shop/list";
    }

    //판매자 검색
    @GetMapping("/shop/search")
    public String shopSearch(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.searchAllForSeller(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/shop/listSearch";
    }

    //판매자 삭제
    @GetMapping("/shop/delete")
    public String shopDelete(@RequestParam("deleteNo") List<Integer> deleteNos){
        adminService.deleteShop(deleteNos);
        return "redirect:/admin/shop/list";
    }

    /*
     * 관리자 회원목록
     * */

    //회원목록
    @GetMapping("/member/list")
    public String memberList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectMemberForList(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/member/list";
    }

    // 회원검색
    @GetMapping("/member/search")
    public String memberSearchList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectMemberForList(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/member/listSearch";
    }

    @GetMapping("/member/state")
    public String memberState(@RequestParam("uid")  String uid,@RequestParam("state") String state){
        System.out.println(state);
        System.out.println(uid);
        return "redirect:/admin/member/list";
    }


    //포인트관리
    @GetMapping("/member/point")
    public String memberPoint(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectPointForList(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/member/point";
    }

    //포인트 검색
    @GetMapping("/member/point/search")
    public String memberPointSearch(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectPointForList(pageRequestDTO);
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        model.addAttribute(pageResponseDTO);
        return "/admin/member/pointSearch";
    }

    //포인트삭제
    @GetMapping("/point/delete")
    public String memberPointDelete(@RequestParam("deleteNo") List<Integer> deleteNos){
        adminService.deletePoint(deleteNos);

        return "redirect:/admin/member/point";
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

    @GetMapping("/product/search")
    public String productSearch(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO pageResponseDTO = adminService.selectAllForList(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/product/listSearch";
    }


    // 상품 삭제
    @GetMapping("/product/delete")
    public String productDelete(@RequestParam("no") String no){
        adminService.deleteProduct(no);
        return "redirect:/admin/product/list";
    }

    // 상품 전체 삭제
    @GetMapping("/product/delete/all")
    public String deleteProducts(@RequestParam("deleteNo") List<String> deleteNos) {
        // configService.deleteVersions(deleteVnos);
        for(String prodNo : deleteNos){
            adminService.deleteProduct(prodNo);
        }
        return "redirect:/admin/product/list";
    }

    // 상품 등록 페이지 이동
    @GetMapping("/product/register")
    public String productRegister(Model model){
        List<MainCategoryDTO> categoryDTOS = adminService.findAllMainCategory();
        model.addAttribute("categoryDTOS", categoryDTOS);
        return "/admin/product/register";
    }

    // 상품 등록(기능)
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

    // 상품 수정 페이지
    @GetMapping("/product/modify")
    public String productModify(@RequestParam("no") String no, Model model){
        ProductDTO productDTO = adminService.findProductByNo(no);
        List<MainCategoryDTO> categoryDTOS = adminService.findAllMainCategory();

        model.addAttribute("categoryDTOS", categoryDTOS);
        model.addAttribute("product", productDTO);

        return "/admin/product/modify";
    }

    // 상품 수정 기능
    @PostMapping("/product/modify")
    public String productModify(ProductDTO productDTO, ProductDetailDTO productDetailDTO, ProductImageDTO productImageDTO){

        //상품 저장
        Product savedProduct = adminService.ModifyProduct(productDTO);

        // 이미지 저장
        imageService.modifyImage(productImageDTO, savedProduct);

        PointDTO pointDTO = new PointDTO();

        //상품 상세 정보 저장
        adminService.modifyProductDetail(savedProduct, productDetailDTO);

        return "redirect:/admin/product/list";
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
    public String faqList(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = adminService.findAllFaq(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/faq/list";
    }

    //자주묻는질문 검색
    @GetMapping("/cs/faq/search")
    public String faqSearch(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = adminService.findAllFaqByType(pageRequestDTO);
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        model.addAttribute(pageResponseDTO);
        return "/admin/faq/listSearch";
    }

    //자주묻는질문 글작성(페이지)
    @GetMapping("/cs/faq/write")
    public String faqWrite(){
        return "/admin/faq/write";
    }

    //자주묻는질문 글작성(기능)
    @PostMapping("/cs/faq/write")
    public String faqWrite(FaqDTO faqDTO, Model model){

        // 카테고리 별 10개 제한 테스트
        Boolean count = adminService.limitFaq(faqDTO);

        if(!count){
            model.addAttribute("result", 100);
            return "/admin/faq/write";
        }

        adminService.saveFaq(faqDTO);
        return "redirect:/admin/cs/faq/list";
    }
    
    //자주묻는질문 삭제
    @GetMapping("/cs/faq/delete")
    public String faqDelete(@RequestParam("deleteNo") List<Integer> deleteNos) {
        adminService.deleteFaq(deleteNos);
        return "redirect:/admin/cs/faq/list";
    }

    //자주묻는질문 보기
    @GetMapping("/cs/faq/view")
    public String faqView(Model model, @RequestParam("no") int no){
        FaqDTO faqDTO = adminService.findFaqByNo(no);
        model.addAttribute(faqDTO);
        return "/admin/faq/view";
    }

    //자주묻는질문 수정
    @GetMapping("/cs/faq/modify")
    public String faqModify(@RequestParam("no") int no, Model model){
        FaqDTO faqDTO = adminService.findFaqByNo(no);
        model.addAttribute(faqDTO);
        return "/admin/faq/modify";
    }

    //자주묻는질문 수정(기능)
    @PostMapping("/cs/faq/modify")
    public String faqModify(FaqDTO faqDTO){
        adminService.modifyFaq(faqDTO);
        return "redirect:/admin/cs/faq/list";
    }


    //문의하기 목록
    @GetMapping("/cs/qna/list")
    public String qnaList(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO<InquiryDTO> responseDTO = csService.adminFindAll(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        return "/admin/qna/list";
    }

    //문의하기 보기
    @GetMapping("/cs/qna/view")
    public String qnaView(@RequestParam("no") int no, Model model){
        InquiryDTO inquiryDTO = adminService.findInquiryByNo(no);
        model.addAttribute(inquiryDTO);
        return "/admin/qna/view";
    }

    //문의하기 답변
    @GetMapping("/cs/qna/reply")
    public String qnaReply(@RequestParam("no") int no, Model model){
        InquiryDTO inquiryDTO = adminService.findInquiryByNo(no);
        model.addAttribute(inquiryDTO);
        return "/admin/qna/reply";
    }

    //문의하기 삭제
    @GetMapping("/cs/qna/delete")
    public String qnaDeleteList(@RequestParam("deleteNo") List<Integer> deleteNos) {
        adminService.deleteQnaByList(deleteNos);
        return "redirect:/admin/cs/qna/list";
    }

    //문의하기 답변
    @PostMapping("/cs/qna/reply")
    public String qnaReply(@RequestParam("no") int no, @RequestParam("answer") String answer){
        adminService.replyQna(no,answer);
        return "redirect:/admin/cs/qna/view?no=" + no;
    }
    
    //문의하기 검색
    @GetMapping("/cs/qna/search")
    public String qnaSearch(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = adminService.findAllQnaByType(pageRequestDTO);
        System.out.println(pageResponseDTO);
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        model.addAttribute(pageResponseDTO);
        return "/admin/qna/listSearch";
    }

    //채용하기 목록
    @GetMapping("/cs/recruit/list")
    public String recruitList(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = adminService.findAllRecruit(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/recruit/list";
    }

    //채용하기 검색
    @GetMapping("/cs/recruit/search")
    public String recruitSearch(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO pageResponseDTO = adminService.findAllRecruit(pageRequestDTO);
        model.addAttribute(pageResponseDTO);
        return "/admin/recruit/listSearch";
    }

    //채용하기 등록
    @PostMapping("/cs/recruit/register")
    public String recruitRegister(RecruitDTO recruitDTO, @AuthenticationPrincipal UserDetails userDetails){
        adminService.saveRecruit(recruitDTO, userDetails);
        return "redirect:/admin/cs/recruit/list";
    }

    //채용하기 삭제
    @GetMapping("/cs/recruit/delete")
    public String recruitDeleteList(@RequestParam("deleteNo") List<Integer> deleteNos) {
        adminService.deleteRecruitByList(deleteNos);
        return "redirect:/admin/cs/recruit/list";
    }












}
