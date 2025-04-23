// 파일 위치 예시: /static/js/member/termsValidation.js

document.addEventListener("DOMContentLoaded", function () {
    const btnAgree = document.getElementById("btnAgree");

    btnAgree.addEventListener("click", function () {
        const agree1 = document.querySelector("input[name='agree1']").checked;
        const agree2 = document.querySelector("input[name='agree2']").checked;
        const agree3 = document.querySelector("input[name='agree3']").checked;

        if (!agree1 || !agree2 || !agree3) {
            alert("필수 약관에 모두 동의해야 다음 단계로 이동할 수 있습니다.");
            return;
        }

        // ✅ 모든 필수 항목 동의 시 이동
        window.location.href = "/user/member/registerSeller";
    });
});