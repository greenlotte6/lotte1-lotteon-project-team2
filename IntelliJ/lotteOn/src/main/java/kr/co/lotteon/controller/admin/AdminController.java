package kr.co.lotteon.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.service.admin.adminService;
import kr.co.lotteon.service.product.ImageService;
import kr.co.lotteon.service.product.ProductService;
import kr.co.lotteon.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SellerService sellerService;
    private final ImageService imageService;
    private final ProductService productService;
    private final adminService adminService;

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
    public String policy() {
        return "/admin/config/policy";
    }

    // 카테고리 (미완성)
    @GetMapping("/config/category")
    public String category() {
        return "/admin/config/category";
    }

    // 버전관리
    @GetMapping("/config/version")
    public String version() {
        return "/admin/config/version";
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
    public String productList(){
        return "/admin/product/list";
    }

    @GetMapping("/product/register")
    public String productRegister(){
        return "/admin/product/register";
    }

    @PostMapping("/product/register")
    public String productRegister(ProductDTO productDTO, ProductDetailDTO productDetailDTO, ProductImageDTO productImageDTO){

        System.out.println(productDetailDTO);
        System.out.println(productImageDTO);
        System.out.println(productDTO);

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
    public String couponList(){
        return "/admin/coupon/list";
    }

    //쿠폰목록
    @GetMapping("/coupon/issued")
    public String issued(){
        return "/admin/coupon/issued";
    }
    
    /*
    * 관리자 고객센터 목록 (공지사항/자주묻는질문/문의하기/채용하기)
    * */

    //공지사항
    @GetMapping("/cs/notice/list")
    public String noticeList(){
        return "/admin/notice/list";
    }

    //자주묻는질문
    @GetMapping("/cs/faq/list")
    public String faqList(){
        return "/admin/faq/list";
    }

    //문의하기 목록
    @GetMapping("/cs/qna/list")
    public String qnaList(){
        return "/admin/qna/list";
    }

    //채용하기 목록
    @GetMapping("/cs/recruit/list")
    public String recruitList(){
        return "/admin/recruit/list";
    }

















}
