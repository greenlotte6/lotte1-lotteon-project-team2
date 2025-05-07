package kr.co.lotteon.dto.kakao;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KakaoReadyResponse {

    private String tid; // 결제 고유 번호
    private String next_redirect_mobile_url; // 모바일 결제 URL
    private String next_redirect_pc_url; // PC 결제 URL
    private String created_at; // 생성 시간


    private String partner_order_id;
    private String partner_user_id;

}
