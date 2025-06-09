document.addEventListener("DOMContentLoaded", function () {

    // 판매자별 테이블 접기
    const container = document.getElementById('tables-container');
    if (!container) return;

    const tables = Array.from(container.querySelectorAll('.order_content'));
    if (tables.length === 0) return;

    const vendorGroups = {};

    tables.forEach((table) => {
        const vendorElement = table.querySelector('.vendor-name');
        const vendor = vendorElement ? vendorElement.innerText.trim() : null;
        if (!vendor) return;

        if (!vendorGroups[vendor]) {
            vendorGroups[vendor] = [];
        }
        vendorGroups[vendor].push(table);
    });

    container.innerHTML = '';

    Object.entries(vendorGroups).forEach(([vendor, vendorTables]) => {
        const section = document.createElement('div');
        section.className = 'vendor-section';

        const header = document.createElement('div');
        header.className = 'vendor-header';

        const titleSpan = document.createElement('span');
        titleSpan.className = 'vendor-title';
        titleSpan.innerText = vendor;

        const iconSpan = document.createElement('span');
        iconSpan.className = 'vendor-toggle-icon';
        iconSpan.innerText = '▼';

        header.appendChild(titleSpan);
        header.appendChild(iconSpan);

        const group = document.createElement('div');
        group.className = 'vendor-group';

        vendorTables.forEach((table, i) => {
            if (i !== 0) {
                table.style.display = 'none';
            }
            group.appendChild(table);
        });

        header.addEventListener('click', () => {
            const isOpen = header.classList.toggle('open');
            iconSpan.innerText = isOpen ? '▲' : '▼';

            const allTablesInGroup = group.querySelectorAll('.order_content');
            allTablesInGroup.forEach((t, i) => {
                if (i === 0) return;
                t.style.display = isOpen ? 'table' : 'none';
            });
        });

        section.appendChild(header);
        section.appendChild(group);
        container.appendChild(section);
    });


    // 각 상품 계산
    document.querySelectorAll('.order_content').forEach(table => {
        const row = table.querySelector('tr:nth-of-type(2)');
        if (!row) return;

        const originalEl = row.querySelector('.price_original');
        const saleEl = row.querySelector('.price_total');
        const discountEl = row.querySelector('.discount-rate');
        const quantityEl = row.querySelector('.quantity');

        const quantity = parseInt(quantityEl?.innerText || '1', 10);
        const originalUnit = parseInt(originalEl?.dataset.unit || '0', 10);
        const discountRate = parseFloat((discountEl?.innerText || '0').replace('%', '')) || 0;

        if (!isNaN(discountRate) && originalUnit > 0 && quantity > 0) {
            const original = originalUnit * quantity;
            originalEl.innerText = original.toLocaleString();

            const discountedUnitPrice = originalUnit * (1 - discountRate / 100);

            const flooredDiscountedUnitPrice = Math.floor(discountedUnitPrice);

            const sale = flooredDiscountedUnitPrice * quantity;
            saleEl.dataset.unit = sale;
            saleEl.innerText = sale.toLocaleString();

            const pointEl = row.querySelector('input.point');

            const discountedPriceForPoint = parseInt(saleEl?.dataset.unit || '0', 10);

            if (!isNaN(discountedPriceForPoint) && quantity > 0 && pointEl) {
                const userRank = pointEl.dataset.rank;

                let pointRate = 0;

                switch (userRank) {
                    case 'VVIP':
                        pointRate = 0.05;
                        break;
                    case 'VIP':
                        pointRate = 0.04;
                        break;
                    case 'GOLD':
                        pointRate = 0.03;
                        break;
                    case 'SILVER':
                        pointRate = 0.02;
                        break;
                    case 'FAMILY':
                        pointRate = 0.01;
                        break;
                    default:
                        pointRate = 0;
                        break;
                }
                const point = Math.floor(discountedPriceForPoint * pointRate);
                pointEl.value = point.toString();
            }
        }
    });


    // 전체 포인트 요약 업데이트
    let totalEarnedPoint = 0;
    document.querySelectorAll('.point').forEach(pointEl => {
        const pointValue = pointEl?.value.replace(/[^\d]/g, ''); // value 속성에서 숫자만 추출
        console.log('pointValue:', pointValue); // 추출된 값 확인
        const point = parseInt(pointValue, 10);

        if (!isNaN(point)) {
            totalEarnedPoint += point;
        }
    });

    document.getElementById('summary-point').innerText = totalEarnedPoint.toLocaleString();
    document.querySelector('input[name="totalPoint"]').value = totalEarnedPoint;


    // 쿠폰 모달 창
    const modal = document.getElementById("couponModal");
    const showCouponModalButton = document.getElementById("showCouponModalButton");
    const closeButton = modal?.querySelector(".close-button");

    let selectedCouponBenefit = null;
    let previouslySelectedItem = null;
    let selectedCouponData = null;

    // 모달 열기 버튼 클릭
    if (showCouponModalButton && modal) {
        showCouponModalButton.onclick = function () {
            const vendorElements = document.querySelectorAll(".vendor-name");
            const vendorSet = new Set();

            vendorElements.forEach(el => {
                vendorSet.add(el.textContent.trim());
            });

            const couponItems = document.querySelectorAll(".coupon-item");

            couponItems.forEach(item => {
                const issuedBy = item.getAttribute("data-issued-by")?.trim();
                const couponState = item.getAttribute("data-state")?.trim();

                item.style.display = (vendorSet.has(issuedBy) || issuedBy === "관리자") && couponState === "미사용" ? "block" : "none";

                item.classList.remove("selected");
                item.style.border = "1px solid #ccc";

                item.onclick = function () {
                    if (previouslySelectedItem) {
                        previouslySelectedItem.style.border = "1px solid #ccc";
                        previouslySelectedItem.classList.remove("selected");
                    }

                    this.classList.add("selected");
                    this.style.border = "2px solid red";

                    selectedCouponBenefit = this.querySelector("p")?.innerText;
                    previouslySelectedItem = this;

                    // 선택된 쿠폰의 issueNo, issuedBy, benefit 값을 저장
                    selectedCouponData = {
                        issueNo: this.dataset.issueNo,
                        issuedBy: this.getAttribute("data-issued-by"),
                        benefit: selectedCouponBenefit
                    };
                };
            });

            // 모달 열릴 때마다 applyCouponButton을 다시 찾아서 이벤트 바인딩
            const applyCouponButton = modal.querySelector(".modal-buttons button");

            if (applyCouponButton) {
                applyCouponButton.onclick = function () {

                    if (selectedCouponData) {

                        document.getElementById("couponData").value = selectedCouponData.issueNo;

                        selectedCouponBenefit = this.querySelector("p")?.innerText;
                        previouslySelectedItem = this;

                        document.getElementById('summary-coupon-discount').innerText = '-' + selectedCouponData.benefit;

                        updateOrderSummary();

                        modal.style.display = "none";

                        selectedCouponBenefit = null;
                        previouslySelectedItem = null;
                        selectedCouponData = null;
                    } else {
                        alert("적용할 쿠폰을 선택해주세요.");
                    }
                };
            }
            modal.style.display = "block";
        };
    }

    // 모달 닫기 버튼
    if (closeButton && modal) {
        closeButton.onclick = function () {
            modal.style.display = "none";
            const couponItems = modal.querySelectorAll(".coupon-item");
            couponItems.forEach(item => {
                item.classList.remove("selected");
                item.style.border = "1px solid #ccc";
            });
        };
    }

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
            const couponItems = modal.querySelectorAll(".coupon-item");
            couponItems.forEach(item => {
                item.classList.remove("selected");
                item.style.border = "1px solid #ccc";
            });
        }
    };


    // 결제 요약 업데이트
    function updateOrderSummary() {

        let totalCount = 0;
        let totalOriginal = 0;
        let totalDiscount = 0;
        let initialTotalShipping = 0;
        const vendorShippingMap = {};
        const vendorFreeShipping = {};

        document.querySelectorAll('.order_content').forEach(table => {
            const row = table.querySelector('tr:nth-of-type(2)');
            if (!row) return;

            const quantity = parseInt(row.querySelector('.quantity')?.innerText || '1', 10);
            const originalUnit = parseInt(row.querySelector('.price_original')?.dataset.unit || '0', 10);
            const discountRate = parseFloat((row.querySelector('.discount-rate')?.innerText || '0').replace('%', '')) || 0;
            const shippingCost = parseInt(row.querySelector('.shipping')?.innerText.replace(/[^0-9]/g, '') || '0', 10);
            const vendorName = row.querySelector('.vendor-name')?.innerText.trim();
            const isFreeShipping = !!row.querySelector('.free-shipping');

            const discountAmount = Math.floor(originalUnit * (discountRate / 100));

            totalCount += quantity;
            totalOriginal += originalUnit * quantity;
            totalDiscount += discountAmount * quantity;

            if (vendorName) {
                if (!(vendorName in vendorFreeShipping)) {
                    vendorFreeShipping[vendorName] = true;
                }
                if (!isFreeShipping) {
                    vendorFreeShipping[vendorName] = false;
                }

                if (!vendorFreeShipping[vendorName] && shippingCost > 0) {
                    if (!vendorShippingMap[vendorName]) {
                        vendorShippingMap[vendorName] = 0;
                    }
                    vendorShippingMap[vendorName] = Math.max(vendorShippingMap[vendorName], shippingCost);
                } else if (!vendorShippingMap[vendorName]) {
                    vendorShippingMap[vendorName] = 0;
                }
            }
        });

        initialTotalShipping = Object.values(vendorShippingMap).reduce((acc, shipping) => acc + shipping, 0);

        const currentPointStr = '[[${userDetailsDTO.userPoint}]]';
        const currentPoint = parseInt(currentPointStr, 10) || 0;
        const pointInput = document.querySelector('.discount_info input');
        const pointText = pointInput?.value.trim();
        let pointDiscount = 0;

        if (pointText !== '') {
            const pointValue = parseInt(pointText, 10);
            if (!isNaN(pointValue) && pointValue >= 5000 && pointValue <= currentPoint) {
                pointDiscount = pointValue;
            }
        }

        const discountSubtotal = totalOriginal - totalDiscount;
        let effectiveShipping = initialTotalShipping;

        if (discountSubtotal >= 10000000) {
            effectiveShipping = 0;
        }

        let couponDiscount = 0;
        let finalShippingCost = effectiveShipping;

        window.selectedCouponData = selectedCouponData;

        if (window.selectedCouponData) {
            const benefit = typeof window.selectedCouponData.benefit === 'string' ? window.selectedCouponData.benefit.trim() : '';

            if (benefit.includes('원')) {
                couponDiscount = parseInt(benefit.replace(/[^0-9]/g, ''), 10) || 0;
                console.log(couponDiscount);
            } else if (benefit.includes('%')) {
                const rate = parseInt(benefit.replace(/[^0-9]/g, ''), 10) || 0;
                const baseAmount = discountSubtotal;
                if (rate > 0 && baseAmount > 0) {
                    couponDiscount = Math.floor(baseAmount * (rate / 100));
                    console.log(couponDiscount);
                } else {
                    couponDiscount = 0;
                }
            } else if (benefit.includes('무료배송')) {
                couponDiscount = 0;
                finalShippingCost = 0;
            } else {
                couponDiscount = 0;
            }
        } else {
            couponDiscount = 0;
        }

        const finalTotal = discountSubtotal - pointDiscount - couponDiscount + finalShippingCost;

        document.getElementById('summary-count').innerText = totalCount.toLocaleString();
        document.querySelector('input[name="quantity"]').value = totalCount;

        document.getElementById('summary-amount').innerText = totalOriginal.toLocaleString();
        document.querySelector('input[name="originalTotalPrice"]').value = totalOriginal;

        document.getElementById('summary-discount').innerText = '-' + totalDiscount.toLocaleString();
        document.querySelector('input[name="totalDiscount"]').value = totalDiscount;

        const shippingText = finalShippingCost === 0 ? '무료' : finalShippingCost.toLocaleString();
        document.getElementById('summary-shipping').innerText = shippingText;
        document.querySelector('input[name="shippingFee"]').value = finalShippingCost;

        document.getElementById('summary-point-discount').innerText = '-' + pointDiscount.toLocaleString();
        document.querySelector('input[name="pointDiscount"]').value = pointDiscount;

        const couponDiscountText = '-' + couponDiscount.toLocaleString();
        document.getElementById('summary-coupon-discount').innerText = couponDiscountText;
        document.querySelector('input[name="couponDiscount"]').value = couponDiscount;

        document.getElementById('summary-total').innerText = finalTotal.toLocaleString();
        document.querySelector('input[name="orderTotalPrice"]').value = finalTotal;
    }



    // 포인트 버튼 클릭
    document.querySelector('.discount_info button')?.addEventListener('click', () => {
        const pointInput = document.querySelector('.discount_info input');
        const currentPoint = '[[${userDetailsDTO.userPoint}]]';

        const pointText = pointInput?.value.trim();

        if (currentPoint < 5000) {
            alert('포인트 5,000점 이상이면 현금처럼 사용가능');
            pointInput.disabled = true;
            return;
        }

        pointInput.disabled = false;

        if (pointText === '') {
            updateOrderSummary();
            return;
        }

        const pointValue = parseInt(pointText, 10);


        if (pointValue < 5000) {
            alert('포인트는 5,000점 이상부터 사용 가능합니다.');
            return;
        }

        if (pointValue > currentPoint) {
            alert('보유 포인트를 초과했습니다.');
            return;
        }

        updateOrderSummary();
    });


    // 배송 정보 자동 채움
    const infoDiv = document.getElementById('order_info');

    if (infoDiv) {
        const orderInfo = {
            name: infoDiv.getAttribute('attr-name') || '',
            hp: infoDiv.getAttribute('attr-hp') || '',
            zip: infoDiv.getAttribute('attr-zip') || '',
            addr1: infoDiv.getAttribute('attr-addr1') || '',
            addr2: infoDiv.getAttribute('attr-addr2') || ''
        };


        const checkbox = document.getElementById('sameAsOrderCheckbox');
        const recipientNameInput = document.getElementById('recipientName');
        const recipientHpInput = document.getElementById('recipientHp');
        const recipientZipInput = document.getElementById('recipientZip');
        const recipientAddr1Input = document.getElementById('recipientAddr1');
        const recipientAddr2Input = document.getElementById('recipientAddr2');

        if (checkbox) {
            checkbox.addEventListener('change', function () {
                if (this.checked) {
                    recipientNameInput.value = orderInfo.name;
                    recipientHpInput.value = orderInfo.hp;
                    recipientZipInput.value = orderInfo.zip;
                    recipientAddr1Input.value = orderInfo.addr1;
                    recipientAddr2Input.value = orderInfo.addr2;
                } else {
                    recipientNameInput.value = '';
                    recipientHpInput.value = '';
                    recipientZipInput.value = '';
                    recipientAddr1Input.value = '';
                    recipientAddr2Input.value = '';
                }
            });
        }
    }
    updateOrderSummary();


    // 로딩
    document.getElementById('orderForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const form = event.target;
        const formData = new FormData(form);

        document.getElementById('loading').style.display = 'flex';

        fetch('/order/submit', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                return response.json();
            })
            .then(data => {
                if(data.type === '일반'){
                    window.location.href = "/product/order_completed";
                }else{
                    window.location.href = data.next_redirect_pc_url;
                }
            })
            .catch(err => {
                alert('에러 발생: ' + err);
            })
            .finally(() => {
            });
    });
});