const track = document.querySelector(".slider-track");
const prevBtn = document.querySelector(".prev-btn");
const nextBtn = document.querySelector(".next-btn");

let currentIndex = 0;
const slideCount = document.querySelectorAll(".slide").length;
const visibleCount = 4;
const maxIndex = slideCount - visibleCount;

const slideWidth = document.querySelector(".slide").offsetWidth + 20; // 20px gap 포함

function updateSlide() {
  track.style.transform = `translateX(-${slideWidth * currentIndex}px)`;
}

nextBtn.addEventListener("click", () => {
  if (currentIndex < maxIndex) {
    currentIndex++;
    updateSlide();
  }
});

prevBtn.addEventListener("click", () => {
  if (currentIndex > 0) {
    currentIndex--;
    updateSlide();
  }
});
