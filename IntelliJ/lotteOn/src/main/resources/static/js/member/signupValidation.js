
// 아이디 유효성 검사
document.addEventListener("DOMContentLoaded", function () {
    const btnCheckUid = document.getElementById("btnCheckUid");
    const uidInput = document.getElementById("uid");
    const uidMessage = document.getElementById("uidMessage");

    btnCheckUid.addEventListener("click", function () {
        const uid = uidInput.value;

        fetch(`/user/checkUid?uid=${uid}`)
            .then(res => res.json())
            .then(isExist => {
                if (isExist) {
                    console.log("중복 여부 :" , isExist)
                    uidMessage.textContent = "이미 사용 중인 아이디입니다.";
                    uidMessage.style.color = "red";
                } else {
                    uidMessage.textContent = "사용 가능한 아이디입니다.";
                    uidMessage.style.color = "green";
                }
            })
            .catch(err => {
                console.error("중복확인 실패:", err);
            });
    });
});



// 비밀번호 유효성 검사
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector(".registerForm");
    const passInput = document.getElementById("pass");
    const pass2Input = document.getElementById("pass2");
    const passError = document.getElementById("passError");
    const pass2Error = document.getElementById("pass2Error");

    const passRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;

    form.pass2.addEventListener("focusout", function (e) {
        const pass = passInput.value;
        const pass2 = pass2Input.value;

        let valid = true;

        if (!passRegex.test(pass)) {
            passError.textContent = "비밀번호는 영문, 숫자, 특수문자 포함 8~16자여야 합니다.";
            passError.style.color = "red";
            valid = false;
        } else {
            passError.textContent = "";
        }

        if (pass !== pass2) {
            pass2Error.textContent = "비밀번호가 일치하지 않습니다.";
            pass2Error.style.color = "red";
            valid = false;
        } else {
            pass2Error.textContent = "";
        }

        if (!valid) {
            e.preventDefault(); // 제출 중지
        }
    });
});



// 이메일 인증 유효성 검사
document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const btnCheckEmail = document.getElementById("btnCheckEmail");
    const emailMessage = document.getElementById("emailMessage");

    const codeBox = document.getElementById("codeBox");
    const emailCode = document.getElementById("emailCode");
    const btnVerifyCode = document.getElementById("btnVerifyCode");
    const codeMessage = document.getElementById("codeMessage");

    // 이메일 인증코드 전송
    btnCheckEmail.addEventListener("click", function () {
        const email = emailInput.value;

        fetch("/email/sendCode", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ email })
        })
            .then(res => res.text())
            .then(msg => {
                emailMessage.textContent = msg;
                emailMessage.style.color = "green";
                codeBox.style.display = "block"; // 인증코드 입력창 보이기
            })
            .catch(err => {
                emailMessage.textContent = "인증코드 전송 실패";
                emailMessage.style.color = "red";
            });
    });

    // 인증코드 확인
    btnVerifyCode.addEventListener("click", function () {
        const code = emailCode.value;

        fetch("/email/verifyCode", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            credentials : "include",
            body: new URLSearchParams({ inputCode: code })
        })
            .then(res => res.text())
            .then(result => {
                if (result === "success") {
                    codeMessage.textContent = "인증 완료!";
                    codeMessage.style.color = "green";
                } else {
                    codeMessage.textContent = "인증 실패. 다시 시도하세요.";
                    codeMessage.style.color = "red";
                }
            });
    });
});