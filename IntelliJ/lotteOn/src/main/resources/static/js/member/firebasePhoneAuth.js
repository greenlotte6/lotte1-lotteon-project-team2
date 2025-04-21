// firebasePhoneAuth.js

// Firebase 설정
const firebaseConfig = {
    apiKey: "AIzaSyAuHVRYWq72wUqxdYRsft6kvxXhg03V2jM",
    authDomain: "lotteon-bd31e.firebaseapp.com",
    projectId: "lotteon-bd31e",
    storageBucket: "lotteon-bd31e.firebasestorage.app",
    messagingSenderId: "121588575858",
    appId: "1:121588575858:web:d870507a370ea07e3d5ceb",
    measurementId: "G-VQN8SQWW41"
};

firebase.initializeApp(firebaseConfig);

let confirmationResult;
let recaptchaVerifier;

// reCAPTCHA는 최초 한 번만 초기화해야 해!
document.addEventListener("DOMContentLoaded", function () {
    recaptchaVerifier = new firebase.auth.RecaptchaVerifier("recaptcha-container", {
        size: "invisible"
    });

    recaptchaVerifier.render().then((widgetId) => {
        window.recaptchaWidgetId = widgetId;
    });
});

// 인증 코드 전송
window.sendCode = function () {
    const phone = document.getElementById("phone").value;

    firebase.auth().signInWithPhoneNumber(phone, recaptchaVerifier)
        .then((result) => {
            confirmationResult = result;
            document.getElementById("phoneMessage").textContent = "인증 코드가 전송되었습니다.";
            document.getElementById("phoneMessage").style.color = "green";
        })
        .catch((error) => {
            console.error(error);
            document.getElementById("phoneMessage").textContent = "전송 실패: 번호 형식 또는 설정 오류";
            document.getElementById("phoneMessage").style.color = "red";
        });
};

// 인증 코드 확인 함수
window.verifyCode = function () {
    const code = document.getElementById("verifyCode").value;

    confirmationResult
        .confirm(code)
        .then((result) => {
            const user = result.user;
            document.getElementById("phoneMessage").textContent = "인증 성공!";
            document.getElementById("phoneMessage").style.color = "green";
            console.log("인증된 사용자 번호:", user.phoneNumber);
        })
        .catch((error) => {
            document.getElementById("phoneMessage").textContent = "인증 실패! 코드를 확인하세요.";
            document.getElementById("phoneMessage").style.color = "red";
        });
};