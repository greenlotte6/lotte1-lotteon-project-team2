document.addEventListener('DOMContentLoaded', function () {
    const categoryBtn = document.getElementById('categoryBtn');
    const categoryMenu = document.getElementById('categoryMenu');
    const subcategoryMenu = document.getElementById('subcategoryMenu');
    const categoryItems = categoryMenu.querySelectorAll('li');
    const subMenus = subcategoryMenu.querySelectorAll('ul');

    // 카테고리 아코디언 메뉴 토글
    const parents = document.querySelectorAll(".category .parent");
    parents.forEach(parent => {
        parent.style.cursor = "pointer";
        parent.addEventListener("click", () => {
            const currentChildren = parent.nextElementSibling;
            document.querySelectorAll(".category .children").forEach(children => {
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

    categoryBtn.addEventListener('click', function (e) {
        e.stopPropagation();
        const isVisible = categoryMenu.style.display === 'block';
        categoryMenu.style.display = isVisible ? 'none' : 'block';
        subcategoryMenu.style.display = 'none';
        categoryItems.forEach(i => i.classList.remove('active'));

        // 카테고리 메뉴가 보이게 될 때만 세부 카테고리 메뉴 위치 조정
        if (!isVisible) {
            // 카테고리 메뉴의 오른쪽에 세부 카테고리 메뉴 위치
            const rect = categoryMenu.getBoundingClientRect();
            const scrollTop = window.scrollY || document.documentElement.scrollTop;
            const scrollLeft = window.scrollX || document.documentElement.scrollLeft;

            const topPos = rect.top + scrollTop;
            const leftPos = rect.right + scrollLeft;

            subcategoryMenu.style.top = topPos + 'px';
            subcategoryMenu.style.left = leftPos + 'px';
            subcategoryMenu.style.display = 'block';

            // 모든 서브메뉴 숨기기
            subMenus.forEach(ul => {
                ul.style.display = 'none';
            });

            document.body.appendChild(subcategoryMenu);
        } else {
            subcategoryMenu.style.display = 'none';
        }
    });


    categoryItems.forEach(item => {
        item.addEventListener('click', function (e) {
            e.stopPropagation();

            categoryItems.forEach(i => i.classList.remove('active'));
            this.classList.add('active');

            const rect = this.getBoundingClientRect();
            const scrollTop = window.scrollY || document.documentElement.scrollTop;
            const scrollLeft = window.scrollX || document.documentElement.scrollLeft;

            const topPos = rect.top + scrollTop;
            const leftPos = rect.right + scrollLeft;

            subcategoryMenu.style.top = topPos + 'px';
            subcategoryMenu.style.left = leftPos + 'px';
            subcategoryMenu.style.display = 'block';

            const index = this.getAttribute('data-index');
            subMenus.forEach(ul => {
                ul.style.display = (ul.getAttribute('data-index') === index) ? 'block' : 'none';
            });

            document.body.appendChild(subcategoryMenu);
        });
    });


    document.addEventListener('click', function (e) {
        if (!categoryBtn.contains(e.target) &&
            !categoryMenu.contains(e.target) &&
            !subcategoryMenu.contains(e.target)) {
            categoryMenu.style.display = 'none';
            subcategoryMenu.style.display = 'none';
            categoryItems.forEach(i => i.classList.remove('active'));
        }
    });

});