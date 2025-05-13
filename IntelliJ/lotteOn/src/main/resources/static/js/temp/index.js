// 임시 저장 -  메인 스크립트


    document.addEventListener('DOMContentLoaded', function () {
    const categoryBtn = document.getElementById('categoryBtn');
    const categoryMenu = document.getElementById('categoryMenu');
    const subcategoryMenu = document.getElementById('subcategoryMenu');
    const categoryItems = categoryMenu.querySelectorAll('li');
    const subMenus = subcategoryMenu.querySelectorAll('ul');

    categoryBtn.addEventListener('click', function (e) {
    e.stopPropagation();
    const isVisible = categoryMenu.style.display === 'block';
    categoryMenu.style.display = isVisible ? 'none' : 'block';
    subcategoryMenu.style.display = 'none';
    categoryItems.forEach(i => i.classList.remove('active'));
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
