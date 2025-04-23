document.addEventListener("DOMContentLoaded", function () {
  const tabs = document.querySelectorAll(".tab");
  const forms = document.querySelectorAll(".tab-form");

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      tabs.forEach((t) => t.classList.remove("active"));
      forms.forEach((f) => f.classList.remove("active"));

      tab.classList.add("active");
      const target = tab.dataset.tab;
      document.getElementById(target).classList.add("active");
    });
  });
});

// 일반회원 판매회원 버튼 활성화
document.addEventListener("DOMContentLoaded", function () {
  const memberBtns = document.querySelectorAll(".member-btn");
  const userForm = document.querySelector(".user-form");
  const sellerForm = document.querySelector(".seller-form");

  memberBtns.forEach((btn) => {
    btn.addEventListener("click", () => {
      // 버튼 활성화 토글
      memberBtns.forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");

      // 폼 전환
      if (btn.dataset.type === "user") {
        userForm.classList.add("active");
        sellerForm.classList.remove("active");
      } else {
        sellerForm.classList.add("active");
        userForm.classList.remove("active");
      }
    });
  });
});
