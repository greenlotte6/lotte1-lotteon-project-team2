document.addEventListener('DOMContentLoaded', function() {
    // 모든 2차 select를 숨기는 함수
    function hideAllType2() {
        document.querySelectorAll('.type2').forEach(function(sel) {
            sel.style.display = 'none';
            sel.selectedIndex = 0; // 선택 초기화
        });
    }

    // 페이지 로드 시 모든 2차 select 숨기기
    hideAllType2();

    // 1차 select 변경 이벤트
    document.getElementById('type1').addEventListener('change', function() {
        hideAllType2(); // 모든 2차 select 숨기기
        var value = this.value;

        if (value) {
            // value에서 '/' 등 특수문자 제거 (클래스명에 쓸 수 없는 경우 대비)
            var className = value.replace(/[^\w]/g, '');
            // 예: value가 '쿠폰/이벤트'면 '쿠폰이벤트'로 변환

            // 해당하는 2차 select만 표시
            var target = document.querySelector('.' + className + 'Select');
            if (target) {
                target.style.display = 'inline-block';
            }
        }
    });



});
