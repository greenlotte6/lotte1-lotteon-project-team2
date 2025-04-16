document.addEventListener('DOMContentLoaded', function(){
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

    // 라디오 버튼 change 이벤트
    document.querySelectorAll('input[name="choice"]').forEach(radio => {
        radio.addEventListener('change', function() {
            if (this.value === "31") { // 최근 1개월
                document.getElementById('endDate').value = getToday();
                document.getElementById('startDate').value = getDateNDaysAgo(30);
            } else if (this.value === "93") { // 최근 3개월
                document.getElementById('endDate').value = getToday();
                document.getElementById('startDate').value = getDateNDaysAgo(92);
            } else if (this.value === "3") { // 직접입력

                document.getElementById('startDate').value = "";
                document.getElementById('endDate').value = "";
            }
        });
    });

    
    document.getElementById('startDate').value = getToday();
    document.getElementById('endDate').value = getToday();
});