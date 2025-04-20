document.addEventListener("DOMContentLoaded", function () {
  const parents = document.querySelectorAll(".category .parent");

  parents.forEach((parent) => {
    parent.style.cursor = "pointer";

    parent.addEventListener("click", () => {
      const currentChildren = parent.nextElementSibling;

      document.querySelectorAll(".category .children").forEach((children) => {
        if (children !== currentChildren) {
          children.style.display = "none";
        }
      });

      if (currentChildren && currentChildren.classList.contains("children")) {
        const isVisible = currentChildren.style.display === "block";
        currentChildren.style.display = isVisible ? "none" : "block";
      }
    });
  });

  document.getElementById("listViewBtn").addEventListener("click", () => {
    document.getElementById("listView").style.display = "block";
    document.getElementById("gridView").style.display = "none";
  });

  document.getElementById("gridViewBtn").addEventListener("click", () => {
    document.getElementById("listView").style.display = "none";
    document.getElementById("gridView").style.display = "block";
  });
});
