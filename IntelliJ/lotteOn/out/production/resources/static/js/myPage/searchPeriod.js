document.addEventListener('DOMContentLoaded', function(){
    // 오늘 날짜를 YYYY-MM-DD 형식으로 반환
    function getToday() {
        const today = new Date();
        return today.toISOString().slice(0, 10);
    }

    // N일 전 날짜 반환 (양수: 과거, 음수: 미래)
    function getDateNDaysAgo(days) {
        const date = new Date();
        date.setDate(date.getDate() - days);
        return date.toISOString().slice(0, 10);
    }

    // N개월 전 1일 반환
    function getFirstDayNMonthsAgo(n) {
        const date = new Date();
        date.setMonth(date.getMonth() - n);
        date.setDate(1);
        return date.toISOString().slice(0, 10);
    }

    // 시작일과 종료일이 1년 이내인지 체크 (Date 객체로 비교)
    function isWithinOneYear(start, end) {
        if (!start || !end) return false;
        const startDate = new Date(start);
        const endDate = new Date(end);
        const diff = endDate - startDate;
        return diff >= 0 && diff <= 365 * 24 * 60 * 60 * 1000;
    }

    // 최신 5개월 이내인지 체크 (시작일 기준, Date 객체로 비교)
    function isWithinLast5Months(start) {
        if (!start) return false;
        const startDate = new Date(start);
        const firstDay5MonthsAgo = new Date(getFirstDayNMonthsAgo(5));
        return startDate >= firstDay5MonthsAgo;
    }

    // 라디오 버튼 change 이벤트
    document.querySelectorAll('input[name="choice"]').forEach(radio => {
        radio.addEventListener('change', function() {
            let startDate, endDate;
            endDate = getToday();

            if (this.value === "7") { // 1주일
                startDate = getDateNDaysAgo(6);
            } else if (this.value === "15") { // 15일
                startDate = getDateNDaysAgo(14);
            } else if (this.value === "31") { // 1개월
                startDate = getDateNDaysAgo(30);
            } else if (this.value === "3") { // 직접입력
                document.getElementById('startDate').value = "";
                document.getElementById('endDate').value = "";
                return;
            }

            // 최신 5개월 이내 체크
            if (!isWithinLast5Months(startDate)) {
                alert("최신 5개월까지만 조회 가능합니다.");
                return;
            }

            // 1년 이내 체크
            if (!isWithinOneYear(startDate, endDate)) {
                alert("최대 1년치만 조회할 수 있습니다.");
                return;
            }

            document.getElementById('startDate').value = startDate;
            document.getElementById('endDate').value = endDate;
        });
    });

    // 초기값: 오늘~오늘
    document.getElementById('startDate').value = getToday();
    document.getElementById('endDate').value = getToday();
});
