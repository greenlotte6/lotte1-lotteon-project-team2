document.addEventListener("DOMContentLoaded", function () {
  const newPassword = document.getElementById("new-password");
  const confirmPassword = document.getElementById("confirm-password");

  const pwdError = document.getElementById("pwd-error");
  const confirmError = document.getElementById("confirm-error");

  const pwdRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+{}[\]:;<>,.?/~`-])[A-Za-z\d!@#$%^&*()_+{}[\]:;<>,.?/~`-]{8,15}$/;

  // 새 비밀번호 실시간 검사
  newPassword.addEventListener("input", () => {
    const value = newPassword.value;
    if (!pwdRegex.test(value)) {
      pwdError.textContent = "8~15자, 영문/숫자/특수문자 포함해야 합니다.";
    } else {
      pwdError.textContent = "";
    }
  });

  // 비밀번호 확인 실시간 일치 검사
  confirmPassword.addEventListener("input", () => {
    if (newPassword.value !== confirmPassword.value) {
      confirmError.textContent = "비밀번호가 일치하지 않습니다.";
    } else {
      confirmError.textContent = "";
    }
  });

  // 👁 눈 아이콘 toggle
  document.querySelectorAll(".eye-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      const input = btn.previousElementSibling;
      input.type = input.type === "password" ? "text" : "password";
    });
  });
});
