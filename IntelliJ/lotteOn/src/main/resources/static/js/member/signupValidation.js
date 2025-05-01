// 기존 코드는 유지하고 새로운 기능 추가

// 아이디 유효성 검사 (기존 코드)
document.addEventListener("DOMContentLoaded", function () {
    const btnCheckUid = document.getElementById("btnCheckUid");
    const uidInput = document.getElementById("uid");
    const uidMessage = document.getElementById("uidMessage");

    btnCheckUid.addEventListener("click", function () {
        const uid = uidInput.value;

        fetch(`/user/user/checkUid?uid=${uid}`)
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

// 비밀번호 유효성 검사 (기존 코드)
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

// 이메일 인증 유효성 검사 (기존 코드)
document.addEventListener("DOMContentLoaded", function () {
    const emailInput = document.getElementById("email");
    const emailMessage = document.getElementById("emailMessage");
    const btnCheckEmail = document.getElementById("btnCheckEmail");
    const codeBox = document.getElementById("codeBox");

    if (emailInput && btnCheckEmail) { // 요소가 존재하는지 확인
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        btnCheckEmail.addEventListener("click", function () {
            const email = emailInput.value;

            if (!emailRegex.test(email)) {
                emailMessage.textContent = "올바른 이메일 형식이 아닙니다.";
                emailMessage.style.color = "red";
                return;
            }

            fetch('/email/sendCode', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({ email: email })
            })
                .then(res => res.text())
                .then(msg => {
                    emailMessage.textContent = msg;
                    emailMessage.style.color = "green";

                    codeBox.style.display = "block";
                })
                .catch(err => {
                    console.error("이메일 인증 실패", err);
                    emailMessage.textContent = "서버 오류가 발생했습니다.";
                    emailMessage.style.color = "red";
                });
        });

        const btnVerifyCode = document.getElementById("btnVerifyCode");
        const emailCodeInput = document.getElementById("emailCode");
        const codeMessage = document.getElementById("codeMessage");

        if (btnVerifyCode && emailCodeInput) {
            btnVerifyCode.addEventListener("click", function () {
                const inputCode = emailCodeInput.value;

                fetch('/email/verifyCode', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({ code: inputCode })
                })
                    .then(res => res.text())
                    .then(msg => {
                        codeMessage.textContent = msg;
                        codeMessage.style.color = (msg.includes("성공") ? "green" : "red");
                    })
                    .catch(err => {
                        console.error("인증 확인 실패", err);
                        codeMessage.textContent = "서버 오류가 발생했습니다.";
                        codeMessage.style.color = "red";
                    });
            });
        }
    }
});

// 새로 추가: 사업자등록번호와 통신판매업번호 유효성 검사
document.addEventListener("DOMContentLoaded", function() {
    // 요소 선택
    const registerForm = document.querySelector(".registerForm");
    const bizRegNoInput = document.querySelector('input[name="bizRegNo"]');
    const commerceNoInput = document.querySelector('input[name="commerceNo"]');

    // 사업자등록번호 유효성 검사 함수
    function validateBusinessNumber(bizRegNo) {
        // 형식 검사 (XXX-XX-XXXXX, 하이픈 포함 12자리)
        const pattern = /^\d{3}-\d{2}-\d{5}$/;
        if (!pattern.test(bizRegNo)) {
            return false;
        }

        // 사업자등록번호 체크 알고리즘
        const numberOnly = bizRegNo.replace(/-/g, '');
        const checkKeys = [1, 3, 7, 1, 3, 7, 1, 3, 5];
        let sum = 0;

        for (let i = 0; i < 9; i++) {
            sum += parseInt(numberOnly.charAt(i)) * checkKeys[i];
        }

        sum += parseInt((parseInt(numberOnly.charAt(8)) * 5) / 10);
        const checkDigit = (10 - (sum % 10)) % 10;

        return checkDigit === parseInt(numberOnly.charAt(9));
    }

    // 통신판매업번호 유효성 검사 함수
    function validateCommerceNumber(commerceNo) {
        // 형식 검사 (XX-XXX-XX, 하이픈 포함 8자리)
        const pattern = /^\d{2}-\d{3}-\d{2}$/;
        return pattern.test(commerceNo);
    }

    // 에러 메시지 표시 함수
    function showErrorMessage(input, message, isValid) {
        // 이미 에러 메시지가 있는지 확인
        let errorSpan = null;

        // input 다음 요소가 description 클래스이면 그 다음에 에러 메시지가 있는지 확인
        if (input.nextElementSibling && input.nextElementSibling.classList.contains('description')) {
            errorSpan = input.nextElementSibling.nextElementSibling;
            if (!errorSpan || !errorSpan.classList.contains('error-message')) {
                errorSpan = document.createElement('span');
                errorSpan.className = 'error-message';
                input.nextElementSibling.after(errorSpan);
            }
        } else {
            // input 바로 다음에 에러 메시지가 있는지 확인
            errorSpan = input.nextElementSibling;
            if (!errorSpan || !errorSpan.classList.contains('error-message')) {
                errorSpan = document.createElement('span');
                errorSpan.className = 'error-message';
                input.after(errorSpan);
            }
        }

        errorSpan.textContent = message;
        errorSpan.style.color = isValid ? "green" : "red";

        return errorSpan;
    }

    // 사업자등록번호 이벤트 리스너
    if (bizRegNoInput) {
        bizRegNoInput.addEventListener("focusout", function() {
            const value = this.value.trim();

            // 값이 비어있으면 검사하지 않음
            if (value === '') {
                return;
            }

            const isValid = validateBusinessNumber(value);
            if (!isValid) {
                showErrorMessage(this, "유효하지 않은 사업자등록번호입니다. XXX-XX-XXXXX 형식으로 입력해주세요.", false);
            } else {
                showErrorMessage(this, "유효한 사업자등록번호입니다.", true);
            }
        });
    }

    // 통신판매업번호 이벤트 리스너
    if (commerceNoInput) {
        commerceNoInput.addEventListener("focusout", function() {
            const value = this.value.trim();

            // 값이 비어있으면 검사하지 않음
            if (value === '') {
                return;
            }

            const isValid = validateCommerceNumber(value);
            if (!isValid) {
                showErrorMessage(this, "유효하지 않은 통신판매업번호입니다. XX-XXX-XX 형식으로 입력해주세요.", false);
            } else {
                showErrorMessage(this, "유효한 통신판매업번호입니다.", true);
            }
        });
    }

    // 폼 제출 시 유효성 검사
    if (registerForm) {
        registerForm.addEventListener("submit", function(e) {
            let isValid = true;

            // 사업자등록번호 검증 (값이 있는 경우에만)
            if (bizRegNoInput && bizRegNoInput.value.trim() !== '') {
                if (!validateBusinessNumber(bizRegNoInput.value)) {
                    showErrorMessage(bizRegNoInput, "유효하지 않은 사업자등록번호입니다.", false);
                    isValid = false;
                }
            }

            // 통신판매업번호 검증 (값이 있는 경우에만)
            if (commerceNoInput && commerceNoInput.value.trim() !== '') {
                if (!validateCommerceNumber(commerceNoInput.value)) {
                    showErrorMessage(commerceNoInput, "유효하지 않은 통신판매업번호입니다.", false);
                    isValid = false;
                }
            }

            // 유효성 검사에 실패한 경우 폼 제출 방지
            if (!isValid) {
                e.preventDefault();
            }
        });
    }
});


// 사업자 등록번호 중복확인 기능

// signupValidation.js 파일에 추가할 내용
document.addEventListener("DOMContentLoaded", function() {
    const btnCheckBizRegNo = document.getElementById("btnCheckBizRegNo");
    const bizRegNoInput = document.getElementById("bizRegNo");
    const bizRegNoMessage = document.getElementById("bizRegNoMessage");

    if(btnCheckBizRegNo) {
        btnCheckBizRegNo.addEventListener("click", function() {
            const bizRegNo = bizRegNoInput.value.trim();

            // 입력 값 검증
            if(!bizRegNo) {
                bizRegNoMessage.textContent = "사업자등록번호를 입력해주세요.";
                bizRegNoMessage.className = "error-message";
                return;
            }

            // 형식 검증 (###-##-##### 형식)
            if(!bizRegNo.match(/^\d{3}-\d{2}-\d{5}$/)) {
                bizRegNoMessage.textContent = "형식이 올바르지 않습니다. (예: 123-45-67890)";
                bizRegNoMessage.className = "error-message";
                return;
            }

            // 서버에 중복 확인 요청
            fetch("/user/member/checkBizRegNo", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ bizRegNo: bizRegNo })
            })
                .then(response => response.json())
                .then(data => {
                    if(data.available) {
                        bizRegNoMessage.textContent = "사용 가능한 사업자등록번호입니다.";
                        bizRegNoMessage.className = "error-message success-message";
                    } else {
                        bizRegNoMessage.textContent = "이미 등록된 사업자등록번호입니다.";
                        bizRegNoMessage.className = "error-message";
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    bizRegNoMessage.textContent = "확인 중 오류가 발생했습니다. 다시 시도해주세요.";
                    bizRegNoMessage.className = "error-message";
                });
        });
    }
});

// HTML 수정 코드는 별도로 제공됩니다