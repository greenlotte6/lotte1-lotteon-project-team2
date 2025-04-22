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

// 중복 초기화 방지
if (!firebase.apps.length) {
    firebase.initializeApp(firebaseConfig);
}

let confirmationResult;
let recaptchaVerifier;

document.addEventListener("DOMContentLoaded", () => {
    const recaptchaContainer = document.getElementById("recaptcha-container");

    if (recaptchaContainer) {
        // Invisible reCAPTCHA 초기화
        recaptchaVerifier = new firebase.auth.RecaptchaVerifier("recaptcha-container", {
            size: "invisible",
            callback: function(response) {
                console.log("✅ reCAPTCHA resolved:", response);
            },
            'expired-callback': function() {
                console.warn("⚠️ reCAPTCHA expired. 다시 시도해주세요.");
            }
        });

        recaptchaVerifier.render().then(widgetId => {
            window.recaptchaWidgetId = widgetId;
            window.recaptchaVerifier = recaptchaVerifier;  // ✅ 꼭 window에 저장
        });
    } else {
        console.error("❌ reCAPTCHA container가 없습니다.");
    }

    // 이벤트 바인딩
    document.getElementById("btnSendPhoneCode")?.addEventListener("click", sendCode);
    document.getElementById("btnVerifyPhoneCode")?.addEventListener("click", verifyCode);
});

// 인증 코드 전송
function sendCode() {
    const phone = document.getElementById("phone").value.trim();
    const msg = document.getElementById("phoneMessage");

    const intlPhonePattern = /^\+\d{10,15}$/; // 국제 전화번호 형식

    if (!intlPhonePattern.test(phone)) {
        msg.textContent = "전화번호는 국제 형식으로 입력해주세요. 예: +821012345678";
        msg.style.color = "red";
        return;
    }

    if (!window.recaptchaVerifier) {
        msg.textContent = "reCAPTCHA가 준비되지 않았습니다. 새로고침 후 다시 시도해주세요.";
        msg.style.color = "red";
        console.error("❌ reCAPTCHA verifier가 초기화되지 않았습니다.");
        return;
    }

    firebase.auth().signInWithPhoneNumber(phone, window.recaptchaVerifier)
        .then(result => {
            confirmationResult = result;
            msg.textContent = "인증 코드가 전송되었습니다.";
            msg.style.color = "green";
            console.log("📨 코드 전송 성공");
        })
        .catch(error => {
            console.error("🚨 인증 전송 에러:", error.code, error.message);
            msg.textContent = "전송 실패: " + error.message;
            msg.style.color = "red";
        });
}

// 인증 코드 확인
function verifyCode() {
    const code = document.getElementById("verifyCode").value.trim();
    const msg = document.getElementById("phoneMessage");

    if (!confirmationResult) {
        msg.textContent = "먼저 인증 코드를 요청해주세요.";
        msg.style.color = "red";
        return;
    }

    confirmationResult.confirm(code)
        .then(result => {
            const user = result.user;
            msg.textContent = "✅ 인증 성공!";
            msg.style.color = "green";
            console.log("🔐 인증된 사용자:", user.phoneNumber);
        })
        .catch(error => {
            console.error("❌ 인증 실패:", error);
            msg.textContent = "인증 실패! 코드를 다시 확인해주세요.";
            msg.style.color = "red";
        });
}