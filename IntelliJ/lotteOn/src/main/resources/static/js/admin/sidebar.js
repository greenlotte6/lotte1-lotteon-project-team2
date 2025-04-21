document.addEventListener('DOMContentLoaded', function () {
    const toggles = document.querySelectorAll('.menu-toggle');

    toggles.forEach(toggle => {
        toggle.addEventListener('click', function (e) {
            e.preventDefault();

            // 현재 클릭된 toggle 외 다른 submenu는 닫기 (원하면 제거 가능)
            toggles.forEach(otherToggle => {
                if (otherToggle !== toggle) {
                    otherToggle.classList.remove('active');
                    const otherSubmenu = otherToggle.nextElementSibling;
                    if (otherSubmenu && otherSubmenu.classList.contains('submenu')) {
                        otherSubmenu.style.display = 'none';
                    }
                }
            });

            // 현재 submenu toggle
            const submenu = this.nextElementSibling;
            if (submenu && submenu.classList.contains('submenu')) {
                const isVisible = submenu.style.display === 'block';
                submenu.style.display = isVisible ? 'none' : 'block';
                this.classList.toggle('active', !isVisible);
            }
        });
    });

});