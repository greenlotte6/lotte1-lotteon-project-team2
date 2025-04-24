document.addEventListener("DOMContentLoaded", function () {
  // 탭 (아이디/비밀번호 찾기) 버튼 클릭
  const tabs = document.querySelectorAll(".tab");
  const tabForms = document.querySelectorAll(".tab-form");

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      // 모든 탭 비활성화
      tabs.forEach((t) => t.classList.remove("active"));
      tabForms.forEach((form) => form.classList.remove("active"));

      // 현재 탭 활성화
      tab.classList.add("active");
      const targetId = tab.dataset.tab;
      document.getElementById(targetId).classList.add("active");

      // 탭이 바뀌면 해당 폼의 회원유형 기본값을 일반회원으로
      const targetForm = document.getElementById(targetId);
      const memberBtns = targetForm.querySelectorAll(".member-btn");
      const userForm = targetForm.querySelector(".user-form");
      const sellerForm = targetForm.querySelector(".seller-form");

      memberBtns.forEach((btn) => btn.classList.remove("active"));
      memberBtns[0].classList.add("active"); // 일반회원 버튼 활성화

      if (userForm && sellerForm) {
        userForm.classList.add("active");
        sellerForm.classList.remove("active");
      }
    });
  });

  // 회원 유형 (일반/판매) 버튼 클릭
  const allMemberTypeSections = document.querySelectorAll(".member-type");

  allMemberTypeSections.forEach((section) => {
    const memberBtns = section.querySelectorAll(".member-btn");

    memberBtns.forEach((btn) => {
      btn.addEventListener("click", () => {
        const parentTabForm = btn.closest(".tab-form");
        const userForm = parentTabForm.querySelector(".user-form");
        const sellerForm = parentTabForm.querySelector(".seller-form");

        memberBtns.forEach((b) => b.classList.remove("active"));
        btn.classList.add("active");

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
});
