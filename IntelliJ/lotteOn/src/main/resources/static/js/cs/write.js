document.addEventListener('DOMContentLoaded', function() {
    // 1차 value와 2차 select 클래스 매핑
    const typeMap = {
        "회원": "memberSelect",
        "쿠폰/이벤트": "couponSelect",
        "주문/결제": "orderSelect",
        "배송": "deliverySelect",
        "취소/반품/환불": "cancelSelect",
        "여행/숙박/항공": "travelSelect",
        "안전거래": "safeSelect"
    };

    // 모든 2차 select 숨기기
    function hideAllType2() {
        document.querySelectorAll('.type2').forEach(function(sel) {
            sel.style.display = 'none';
            sel.selectedIndex = 0; // 선택 초기화
        });
    }

    // 페이지 로드 시 숨기기
    hideAllType2();

    // 1차 select 변경 이벤트
    document.getElementById('type1').addEventListener('change', function() {
        hideAllType2();
        var value = this.value;
        var className = typeMap[value];
        if (className) {
            var target = document.querySelector('.' + className);
            if (target) {
                target.style.display = 'inline-block';
            }
        }
    });
});
