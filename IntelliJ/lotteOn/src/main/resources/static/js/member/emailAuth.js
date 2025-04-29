document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const btnSendEmail = document.getElementById("btnSendEmail");
    const codeBox = document.getElementById("codeBox");
    const authCodeInput = document.getElementById("authCode");
    const btnVerifyCode = document.getElementById("btnVerifyCode");
    const nextBtn = document.getElementById("nextBtn");
    const timerBox = document.getElementById("timerBox");

    let serverAuthCode = "";
    let timer = null;
    let timeLimit = 180; // 3분

    // 이메일 입력하면 '인증번호 받기' 버튼 보이기
    emailInput.addEventListener("input", function () {
        if (emailInput.value.trim().length > 0) {
            btnSendEmail.style.display = "block";
        } else {
            btnSendEmail.style.display = "none";
        }
    });

    // 인증번호 전송
    btnSendEmail.addEventListener("click", function () {
        const email = emailInput.value.trim();

        if (email === "") {
            alert("이메일을 입력해주세요.");
            return;
        }

        fetch("/user/member/sendEmailAuth", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: email })
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    alert("인증번호를 이메일로 전송했습니다.");
                    serverAuthCode = data.authCode;
                    codeBox.style.display = "block";
                    startTimer(); // ⏱️ 타이머 시작
                } else {
                    alert("이메일 전송 실패: " + data.message);
                }
            })
            .catch(error => {
                console.error("에러 발생:", error);
                alert("오류가 발생했습니다.");
            });
    });

    // 인증번호 입력하면 '인증 확인' 버튼 보이기
    authCodeInput.addEventListener("input", function () {
        if (authCodeInput.value.trim().length > 0) {
            btnVerifyCode.style.display = "block";
        } else {
            btnVerifyCode.style.display = "none";
        }
    });

    // 인증 확인
    btnVerifyCode.addEventListener("click", function () {
        const enteredCode = authCodeInput.value.trim();

        if (enteredCode === serverAuthCode) {
            clearInterval(timer); // ⏹️ 타이머 종료
            timerBox.textContent = ""; // 타이머 표시 지우기
            alert("인증 성공!");
            nextBtn.disabled = false;
            nextBtn.style.display = "block";
        } else {
            alert("인증번호가 일치하지 않습니다.");
        }
    });

    // 3분 타이머 함수
    function startTimer() {
        clearInterval(timer);
        timeLimit = 180;

        timer = setInterval(() => {
            const minutes = Math.floor(timeLimit / 60);
            const seconds = timeLimit % 60;
            timerBox.textContent = `남은 시간: ${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

            timeLimit--;
            if (timeLimit < 0) {
                clearInterval(timer);
                serverAuthCode = "";
                timerBox.textContent = "⏰ 인증 시간이 초과되었습니다. 다시 시도해주세요.";
                nextBtn.disabled = true;
            }
        }, 1000);
    }
});