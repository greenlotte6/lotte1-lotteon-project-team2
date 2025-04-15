document.addEventListener('DOMContentLoaded', function(){
    // 날짜 스크립트

    // 오늘 날짜를 YYYY-MM-DD 형식으로 반환
    function getToday() {
        const today = new Date();
        return today.toISOString().slice(0, 10);
    }

    // 날짜 계산 함수 (days: 음수면 과거, 양수면 미래)
    function getDateNDaysAgo(days) {
        const date = new Date();
        date.setDate(date.getDate() - days);
        return date.toISOString().slice(0, 10);
    }

    // 버튼 클릭 이벤트
    document.querySelectorAll('.filterDate').forEach(btn => {
        btn.addEventListener('click', function() {
            const period = parseInt(this.dataset.period, 10); // 7, 30, 90
            document.getElementById('endDate').value = getToday();
            document.getElementById('startDate').value = getDateNDaysAgo(period - 1);
        });
    });

    // 페이지 로드 시 기본값(오늘~오늘)
    document.getElementById('startDate').value = getToday();
    document.getElementById('endDate').value = getToday();


    // 날짜 스크립트 끝
});