
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

    form.addEventListener("submit", function (e) {
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
    const emailMessage = document.getElementById("emailMessage");
    const btnCheckEmail = document.getElementById("btnCheckEmail");

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    btnCheckEmail.addEventListener("click", function () {
        const email = emailInput.value;

        if (!emailRegex.test(email)) {
            emailMessage.textContent = "올바른 이메일 형식이 아닙니다.";
            emailMessage.style.color = "red";
            return;
        }

        // TODO: 이메일 중복 체크 또는 인증 메일 전송 API 요청
        fetch(`/user/checkEmail?email=${email}`)
            .then(res => res.json())
            .then(isExist => {
                if (isExist) {
                    emailMessage.textContent = "이미 등록된 이메일입니다.";
                    emailMessage.style.color = "red";
                } else {
                    emailMessage.textContent = "사용 가능한 이메일입니다.";
                    emailMessage.style.color = "green";

                    // 실제 인증 메일 전송 로직을 이곳에 추가 가능
                }
            })
            .catch(err => {
                console.error("이메일 인증 실패", err);
                emailMessage.textContent = "서버 오류가 발생했습니다.";
                emailMessage.style.color = "red";
            });
    });
});