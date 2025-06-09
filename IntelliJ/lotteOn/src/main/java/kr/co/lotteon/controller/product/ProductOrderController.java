package kr.co.lotteon.controller.product;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.service.product.ProductOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductOrderController {

    private final ProductOrderService productOrderService;


    // 장바구니 담고 주문하기 View
    @GetMapping("/product/order")
    public String order(@RequestParam("cartNo") List<Integer> cartNos,
                        Model model) {

        List<CartDTO> cartDTOList = new ArrayList<>();

        for (int i = 0; i < cartNos.size(); i++) {
            Integer cartNo = cartNos.get(i);

            CartDTO cartDTO = productOrderService.findByCartNo(cartNo);

            cartDTOList.add(cartDTO);
        }

        String uid = cartDTOList.get(0).getUser().getUid();

        UserDetailsDTO userDetailsDTO = productOrderService.findByUser(uid);
        List<CouponIssueDTO> couponIssueDTOList = productOrderService.findAllByUser(uid);

        model.addAttribute(cartDTOList);
        model.addAttribute(couponIssueDTOList);
        model.addAttribute(userDetailsDTO);

        return  "/product/order/order";
    }

    // 바로 주문하기 View
    @PostMapping("/product/order/direct")
    public String order(@RequestParam Map<String, String> options,
                        ItemRequestDTO itemRequestDTO,
                        @AuthenticationPrincipal UserDetails userDetails,
                        HttpSession session,
                        Model model){

        List<CartDTO> cartDTOList = productOrderService.makeCart(itemRequestDTO, userDetails, options);

        String uid = userDetails.getUsername();
        UserDetailsDTO userDetailsDTO = productOrderService.findByUser(uid);
        List<CouponIssueDTO> couponIssueDTOList = productOrderService.findAllByUser(uid);

        model.addAttribute(cartDTOList);
        model.addAttribute(couponIssueDTOList);
        model.addAttribute(userDetailsDTO);

        session.setAttribute("cartDTO", cartDTOList.get(0));

        return "/product/order/order";
    }

}
