package kr.co.lotteon.dto.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDTO {
    // 장바구니, 주문하기로 넘어 갈 상품 정보 담는 용도

    private String prodNo;

    private int quantity;

    private Map<String, String> options;

}
