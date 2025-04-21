// categorySidebar.js
document.addEventListener("DOMContentLoaded", function () {
    const menuItems = document.querySelectorAll(".category-menu .has-sub");
  
    menuItems.forEach((item) => {
      const link = item.querySelector("a");
  
      link.addEventListener("click", function (e) {
        e.preventDefault();
  
        // 모든 메뉴 닫기 (단일 열기 방식)
        menuItems.forEach((el) => {
          if (el !== item) el.classList.remove("open");
        });
  
        // 현재 메뉴 토글
        item.classList.toggle("open");
      });
    });
  });