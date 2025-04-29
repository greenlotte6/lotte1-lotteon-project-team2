document.addEventListener("DOMContentLoaded", function () {
    const phoneInput = document.getElementById("phone");
    const btnSendCode = document.getElementById("btnSendCode");
    const codeBox = document.getElementById("codeBox");
    const codeInput = document.getElementById("code");
    const btnVerifyCode = document.getElementById("btnVerifyCode");
    const nextBtn = document.getElementById("nextBtn");

    let confirmationResult = null;
    let timer = null;
    let timeLimit = 180; // 3분 (초 단위)

    // ⭐ 1. 전화번호 입력 시 '인증번호 받기' 버튼 표시
    phoneInput.addEventListener("input", function () {
        btnSendCode.style.display = phoneInput.value.trim().length > 0 ? "block" : "none";
    });

    // ⭐ 2. '인증번호 받기' 버튼 클릭 시 Firebase로 인증번호 전송
    btnSendCode.addEventListener("click", function () {
        const phoneNumber = phoneInput.value.trim();
        const appVerifier = new firebase.auth.RecaptchaVerifier('recaptcha-container', { size: 'invisible' });

        firebase.auth().signInWithPhoneNumber(phoneNumber, appVerifier)
            .then((result) => {
                confirmationResult = result;
                alert("인증번호를 전송했습니다.");
                codeBox.style.display = "block"; // 인증번호 입력칸 보이기
                startTimer(); // ⏱️ 3분 타이머 시작
            })
            .catch((error) => {
                alert("인증 실패: " + error.message);
            });
    });

    // ⭐ 3. 인증번호 입력 시 '인증 확인' 버튼 표시
    codeInput.addEventListener("input", function () {
        btnVerifyCode.style.display = codeInput.value.trim().length > 0 ? "block" : "none";
    });

    // ⭐ 4. '인증 확인' 버튼 클릭 시 인증번호 검증
    btnVerifyCode.addEventListener("click", function () {
        const code = codeInput.value.trim();
        if (!confirmationResult) {
            alert("먼저 인증번호를 받아주세요.");
            return;
        }

        confirmationResult.confirm(code)
            .then((result) => {
                clearInterval(timer); // 타이머 정지
                timerBox.textContent = ""; // ✅ 타이머 텍스트 없애기
                alert("인증 성공!");
                nextBtn.disabled = false; // 다음 버튼 활성화
                nextBtn.style.display = "block";
            })
            .catch((error) => {
                alert("인증 실패: " + error.message);
            });
    });

    // ⭐ 5. 폼 제출 시 인증 여부 확인
    window.validateAuth = function () {
        return !nextBtn.disabled;
    }

    // ⭐ 6. 3분 타이머 함수
    function startTimer() {
        clearInterval(timer); // 기존 타이머 제거
        timeLimit = 180; // 3분 (180초)
        const timerBox = document.getElementById("timerBox");

        timer = setInterval(() => {
            const minutes = Math.floor(timeLimit / 60);
            const seconds = timeLimit % 60;

            timerBox.textContent = `남은 시간: ${minutes}:${seconds < 10 ? '0' + seconds : seconds}`;

            timeLimit--;

            if (timeLimit < 0) {
                clearInterval(timer);
                confirmationResult = null;
                alert("⏰ 인증 시간이 초과되었습니다. 다시 시도해주세요.");
                window.location.reload();
            }
        }, 1000);
    }
});