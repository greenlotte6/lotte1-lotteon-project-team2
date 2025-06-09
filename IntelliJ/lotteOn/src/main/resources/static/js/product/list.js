let index = 0;

// 숫자 포맷 (가격 단위 변환 등)
function formatNumber(number) {
    return number.toLocaleString();
}

// 현재 뷰 타입 반환 (grid or list)
function getView() {
    const wrapper = document.getElementById('productWrapper');
    return wrapper.classList.contains('grid') ? 'grid' : 'list';
}

// 슬라이드 이동 처리
function moveSlide(direction) {
    const slides = document.getElementById('slides');
    const totalSlides = Math.ceil(slides.children.length / 2);
    index += direction;
    if (index < 0) index = totalSlides - 1;
    if (index >= totalSlides) index = 0;
    slides.style.transform = `translateX(-${index * 100}%)`;
}

// 그리드/리스트 뷰 전환
function toggleView(view) {
    const wrapper = document.getElementById('productWrapper');
    const toGridBtn = document.getElementById('toGrid');
    const toListBtn = document.getElementById('toList');

    wrapper.classList.remove('list', 'grid');
    wrapper.classList.add(view);

    if (view === 'grid') {
        toGridBtn.style.display = 'none';
        toListBtn.style.display = 'inline-block';
    } else {
        toGridBtn.style.display = 'inline-block';
        toListBtn.style.display = 'none';
    }

    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.set('view', view);
    history.replaceState(null, null, currentUrl);
}

// 상품 클릭 시 상세페이지로 이동
function attachProductClickHandler(containerSelector, cardSelector) {
    const container = document.querySelector(containerSelector);
    if (!container) return;

    container.addEventListener('click', function (e) {
        e.preventDefault();
        let target = e.target.closest(cardSelector);
        if (!target) return;
        const prodNo = target.dataset.prodno;
        if (prodNo) {
            location.href = `/product/view?prodNo=${prodNo}`;
        }
    });
}

// 정렬 옵션 초기화
function resetOtherSortOptions(currentSortType) {
    salesPeriodSelect.selectedIndex = 0;
    reviewPeriodSelect.selectedIndex = 0;
}

// 페이지네이션 렌더링
function updatePagination(data) {
    const pageContainer = document.querySelector('.page');
    pageContainer.innerHTML = '';

    if (data.prev) {
        const prevLink = document.createElement('a');
        prevLink.href = `#`;
        prevLink.classList.add('prev');
        prevLink.textContent = '<';
        prevLink.addEventListener('click', (e) => {
            e.preventDefault();
            loadProducts(`&pg=${data.start - 1}&sortType=${data.sortType}&period=${data.period}`);
        });
        pageContainer.appendChild(prevLink);
    }

    for (let i = data.start; i <= data.end; i++) {
        const pageLink = document.createElement('a');
        pageLink.href = `#`;
        pageLink.textContent = i;
        if (i === data.pg) {
            pageLink.classList.add('current');
        } else {
            pageLink.classList.add('num');
            pageLink.addEventListener('click', (e) => {
                e.preventDefault();
                loadProducts(`&pg=${i}&sortType=${data.sortType}&period=${data.period}`);
            });
        }
        pageContainer.appendChild(pageLink);
    }

    if (data.next) {
        const nextLink = document.createElement('a');
        nextLink.href = `#`;
        nextLink.classList.add('next');
        nextLink.textContent = '>';
        nextLink.addEventListener('click', (e) => {
            e.preventDefault();
            loadProducts(`&pg=${data.end + 1}&sortType=${data.sortType}&period=${data.period}`);
        });
        pageContainer.appendChild(nextLink);
    }
}

// 상품 목록 비동기 로드 함수
function loadProducts(params) {
    const currentUrl = new URL(window.location.href);
    const subCateNo = currentUrl.searchParams.get('subCateNo');
    const sortTypeParam = currentUrl.searchParams.get('sortType');
    const periodParam = currentUrl.searchParams.get('period') || '';
    const pgParam = new URLSearchParams(params).get('pg') || '1';

    const url = `/product/ajaxList?subCateNo=${encodeURIComponent(subCateNo)}&view=${encodeURIComponent(getView())}&pg=${encodeURIComponent(pgParam)}&sortType=${encodeURIComponent(sortTypeParam)}&period=${encodeURIComponent(periodParam)}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const productListContainer = document.querySelector('#productWrapper .product-list');
            productListContainer.innerHTML = '';
            data.dtoList.forEach(product => {
                const productItem = document.createElement('div');
                productItem.classList.add('product-item');
                productItem.setAttribute('data-prodno', product.prodNo);
                productItem.innerHTML = `
                <img class="product-img" src="${product.snameList}" alt="${product.prodName}" />
                <div class="product-info">
                    <span class="vendor-name">${product.company}</span> <span class="vendor-rank">${product.rank}</span><br>
                    <span class="brand">${product.prodBrand}</span><br>
                    <span class="pname">${product.prodName}</span>
                    <div class="meta">
                        <span>★ ${product.ratingAvg !== null ? product.ratingAvg.toFixed(1) : 0}</span>&nbsp;|&nbsp;리뷰 <span>${product.reviewCount}</span>
                    </div>
                    ${product.prodDeliveryFee === 0 ? '<span class="badge-free">무료배송</span>' : `<span class="badge-fee">${formatNumber(product.prodDeliveryFee)}원</span>`}
                </div>
                <div class="product-price">
                    <del>${formatNumber(product.prodPrice)}원</del><br>
                    <span class="discount">${product.prodDiscount}%</span>
                    <span>${formatNumber(product.prodPrice * (100 - product.prodDiscount) / 100)}원</span>
                    <div class="icons">
                        <button><img src="/images/product/icon_favorite.png" alt="찜"></button>
                        <button><img src="/images/product/icon_cart.png" alt="장바구니"></button>
                    </div>
                </div>
            `;
                productListContainer.appendChild(productItem);
            });
            updatePagination(data);
            attachProductClickHandler('#productWrapper', '.product-item');
        })
        .catch(error => {
            console.error('Ajax 요청 실패:', error);
        });
}

// DOMContentLoaded 시 초기 실행
document.addEventListener("DOMContentLoaded", function () {

    // 상품 클릭 핸들러 등록
    attachProductClickHandler('.best-grid', '.best-card');
    attachProductClickHandler('#productWrapper', '.product-item');

    const salesPeriodSelect = document.getElementById('salesPeriodSelect');
    const reviewPeriodSelect = document.getElementById('reviewPeriodSelect');
    const sortLinks = document.querySelectorAll('.nav a[data-sort]');

    // 판매순 정렬 필터 변경
    salesPeriodSelect.addEventListener('change', function() {
        const period = this.value;
        const currentUrl = new URL(window.location.href);
        currentUrl.searchParams.set('sortType', 'mostSales');
        currentUrl.searchParams.set('period', period);
        currentUrl.searchParams.set('pg', '1');
        history.pushState(null, null, currentUrl);
        loadProducts('');
        resetOtherSortOptions('mostSales');
    });

    // 리뷰순 정렬 필터 변경
    reviewPeriodSelect.addEventListener('change', function() {
        const period = this.value;
        const currentUrl = new URL(window.location.href);
        currentUrl.searchParams.set('sortType', 'manyReviews');
        currentUrl.searchParams.set('period', period);
        currentUrl.searchParams.set('pg', '1');
        history.pushState(null, null, currentUrl);
        loadProducts('');
        resetOtherSortOptions('manyReviews');
    });

    // 기타 정렬 클릭 처리
    sortLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            const sortType = this.getAttribute('data-sort');
            let period = '';
            if (sortType === 'lowPrice' || sortType === 'highPrice') {
                period = salesPeriodSelect.value;
                reviewPeriodSelect.selectedIndex = 0;
            } else if (sortType === 'highRating') {
                period = reviewPeriodSelect.value;
                salesPeriodSelect.selectedIndex = 0;
            } else if (sortType === 'latest') {
                period = '';
                salesPeriodSelect.selectedIndex = 0;
                reviewPeriodSelect.selectedIndex = 0;
            }

            const currentUrl = new URL(window.location.href);
            currentUrl.searchParams.set('sortType', sortType);
            currentUrl.searchParams.set('period', period);
            currentUrl.searchParams.set('pg', '1');
            history.pushState(null, null, currentUrl);
            loadProducts('');
        });
    });

    // 초기 상품 목록 불러오기
    loadProducts(`&sortType=salesCount&period=`);
});
