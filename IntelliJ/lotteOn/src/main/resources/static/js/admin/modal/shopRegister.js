// modalShopValidation.js 또는 기존 JS 파일에 추가

document.addEventListener("DOMContentLoaded", function() {
    // 모달 내 요소 선택
    const shopForm = document.querySelector(".shop");
    const bizRegNoInput = document.querySelector('.modal_shop input[name="bizRegNo"]');
    const commerceNoInput = document.querySelector('.modal_shop input[name="commerceNo"]');

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
        // 입력 필드의 다음 요소 확인 (span 설명 텍스트 있음)
        let nextElement = input.nextElementSibling;
        let errorSpan = null;

        // 다음 요소가 span이면 그 다음에 에러 메시지 추가
        if (nextElement && nextElement.tagName.toLowerCase() === 'span') {
            errorSpan = nextElement.nextElementSibling;
            if (!errorSpan || !errorSpan.classList.contains('error-message')) {
                errorSpan = document.createElement('span');
                errorSpan.className = 'error-message';
                nextElement.after(errorSpan);
            }
        } else {
            // span이 없으면 바로 다음에 에러 메시지 추가
            errorSpan = nextElement;
            if (!errorSpan || !errorSpan.classList.contains('error-message')) {
                errorSpan = document.createElement('span');
                errorSpan.className = 'error-message';
                input.after(errorSpan);
            }
        }

        errorSpan.textContent = message;
        errorSpan.style.color = isValid ? "green" : "red";
        errorSpan.style.display = "block";
        errorSpan.style.fontSize = "12px";
        errorSpan.style.marginTop = "5px";

        return errorSpan;
    }

    // 사업자등록번호 이벤트 리스너
    if (bizRegNoInput) {
        // 통신판매업번호 안내 텍스트 수정 (HTML에 12자리로 되어 있으나 실제로는 XXX-XX-XXXXX 형식 필요)
        let bizRegNoSpan = bizRegNoInput.nextElementSibling;
        if (bizRegNoSpan && bizRegNoSpan.tagName.toLowerCase() === 'span') {
            bizRegNoSpan.textContent = "-포함 12자리 입력 (XXX-XX-XXXXX)";
        }

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
        // 통신판매업번호 안내 텍스트 수정 (HTML에 12자리로 되어 있으나 실제로는 XX-XXX-XX 형식 필요)
        let commerceNoSpan = commerceNoInput.nextElementSibling;
        if (commerceNoSpan && commerceNoSpan.tagName.toLowerCase() === 'span') {
            commerceNoSpan.textContent = "-포함 8자리 입력 (XX-XXX-XX)";
        }

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
    if (shopForm) {
        shopForm.addEventListener("submit", function(e) {
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

            // 비밀번호 확인 검증 (간단한 예시)
            const passInput = document.querySelector('.modal_shop input[name="pass"]');
            const passConfirmInput = document.querySelector('.modal_shop input[type="text"]:not([name])');

            if (passInput && passConfirmInput) {
                if (passInput.value !== passConfirmInput.value) {
                    let errorSpan = document.createElement('span');
                    errorSpan.className = 'error-message';
                    errorSpan.textContent = "비밀번호가 일치하지 않습니다.";
                    errorSpan.style.color = "red";
                    errorSpan.style.display = "block";
                    errorSpan.style.fontSize = "12px";

                    // 이미 에러 메시지가 있는지 확인
                    const existingError = passConfirmInput.nextElementSibling;
                    if (existingError && existingError.classList.contains('error-message')) {
                        existingError.textContent = errorSpan.textContent;
                        existingError.style.color = errorSpan.style.color;
                    } else {
                        passConfirmInput.after(errorSpan);
                    }

                    isValid = false;
                }
            }

            // 유효성 검사에 실패한 경우 폼 제출 방지
            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    // 모달 닫기 버튼
    const closeBtn = document.querySelector('.close_btn');
    if (closeBtn) {
        closeBtn.addEventListener('click', function() {
            const modal = document.querySelector('.modal');
            if (modal) {
                modal.style.display = 'none';
            }
        });
    }
});