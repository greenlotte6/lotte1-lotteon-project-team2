document.addEventListener("DOMContentLoaded", function () {

    // 주문 버튼 클릭 시 체크된 상품 없으면 경고
    const orderForm = document.getElementById('order-form');
    const orderButton = document.getElementById('order-button');

    orderButton.addEventListener('click', function (e) {
        const checkedItems = orderForm.querySelectorAll('input[name="cartNo"]:checked');

        if (checkedItems.length === 0) {
            e.preventDefault(); // 폼 제출 막기
            alert('주문할 상품을 선택해주세요.');
        }
    });

    // 수량 +, - 버튼 및 체크박스들
    const plusBtns = document.querySelectorAll('.btn-plus');
    const minusBtns = document.querySelectorAll('.btn-minus');
    const checkAll = document.querySelector('#check-all');
    const itemCheckboxes = document.querySelectorAll('.cart_content input[type="checkbox"]');

    // 요약 정보 업데이트 함수
    function updateSummary() {
        let totalCount = 0;
        let originalTotalAmount = 0;
        let discountTotal = 0;
        let actualPayAmount = 0;
        const uniqueVendors = new Set();

        const deliveryFeePerVendor = 2500;
        const freeShippingLimit = 1000000;

        itemCheckboxes.forEach((cb) => {
            if (cb.checked) {
                const cartContent = cb.closest('.cart_content');
                const quantity = parseInt(cartContent.querySelector('.quantity').textContent);

                const priceOriginalEl = cartContent.querySelector('.price_original');
                const priceTotalEl = cartContent.querySelector('.price_total');

                const unitOriginal = parseInt(priceOriginalEl.dataset.unit);
                const unitTotal = parseInt(priceTotalEl.dataset.unit);

                const itemDiscount = (unitOriginal - unitTotal) * quantity;
                const itemOriginalPrice = unitOriginal * quantity;
                const itemTotalPrice = unitTotal * quantity;

                const vendorNameEl = cartContent.querySelector('.vendor-name');
                if (vendorNameEl) {
                    uniqueVendors.add(vendorNameEl.textContent.trim());
                }

                totalCount += quantity;
                originalTotalAmount += itemOriginalPrice;
                discountTotal += itemDiscount;
                actualPayAmount += itemTotalPrice;
            }
        });

        const orderAmount = originalTotalAmount - discountTotal;

        const vendorDeliveryMap = new Map();

        itemCheckboxes.forEach((cb) => {
            if (cb.checked) {
                const cartContent = cb.closest('.cart_content');
                const deliveryFeeEl = cartContent.querySelector('.price_delivery');
                const vendorNameEl = cartContent.querySelector('.vendor-name');
                const vendor = vendorNameEl ? vendorNameEl.textContent.trim() : '';

                const rawDeliveryFee = deliveryFeeEl?.dataset.deliveryFee;
                const deliveryFee = parseInt(rawDeliveryFee || '0');

                if (isNaN(deliveryFee)) {
                    console.error('배송비가 NaN입니다. 확인해주세요. Raw value:', rawDeliveryFee, 'Element:', deliveryFeeEl);
                }
                if (vendor) {
                    const currentMax = vendorDeliveryMap.get(vendor) || 0;
                    if (!isNaN(deliveryFee)) {
                        vendorDeliveryMap.set(vendor, Math.max(currentMax, deliveryFee));
                    } else {
                        vendorDeliveryMap.set(vendor, Math.max(currentMax, 0));
                        console.warn(`Vendor ${vendor}의 배송비를 0으로 처리합니다 (NaN 발생).`);
                    }
                }
            }
        });
        let shippingCost = 0;
        vendorDeliveryMap.forEach((fee) => {
            if (fee > 0) {
                shippingCost += deliveryFeePerVendor;
            }
        });
        if (totalCount === 0 || orderAmount >= freeShippingLimit) {
            shippingCost = 0;
        }
        const finalTotal = orderAmount + shippingCost;
        const shippingText =
            totalCount === 0 ? '0'
                : orderAmount >= freeShippingLimit ? '무료'
                    : shippingCost.toLocaleString() + '원';
        document.getElementById('summary-count').textContent = totalCount;
        const checkAllLabel = document.querySelector('label[for="check-all"]');
        if (checkAllLabel) {
            checkAllLabel.textContent = `전체선택(${totalCount})`;
        }

        document.getElementById('summary-count').textContent = totalCount;
        document.getElementById('summary-amount').textContent = originalTotalAmount.toLocaleString();
        document.getElementById('summary-discount').textContent = '-' + discountTotal.toLocaleString();
        document.getElementById('summary-shipping').textContent = shippingText;
        document.getElementById('summary-total').textContent = finalTotal.toLocaleString() + '원';
    }

    // 수량 조절 버튼
    plusBtns.forEach((btn) => {
        btn.addEventListener('click', () => {
            const cartContent = btn.closest('.cart_content');
            const quantityEl = cartContent.querySelector('.quantity');
            let count = parseInt(quantityEl.textContent);
            count++;
            quantityEl.textContent = count;

            const priceOriginalEl = cartContent.querySelector('.price_original');
            const priceTotalEl = cartContent.querySelector('.price_total');
            const unitOriginal = parseInt(priceOriginalEl.dataset.unit);
            const unitTotal = parseInt(priceTotalEl.dataset.unit);

            priceOriginalEl.textContent = (unitOriginal * count).toLocaleString() + '원';
            priceTotalEl.textContent = (unitTotal * count).toLocaleString() + '원';

            const cartNoInput = cartContent.querySelector('input[name="cartNo"]');
            const cartNoValue = cartNoInput.value;

            updateCartProductCount(cartNoValue, count);

            updateSummary();
        });
    });

    minusBtns.forEach((btn) => {
        btn.addEventListener('click', () => {
            const cartContent = btn.closest('.cart_content');
            const quantityEl = cartContent.querySelector('.quantity');
            let count = parseInt(quantityEl.textContent);
            if (count > 1) {
                count--;
                quantityEl.textContent = count;

                const priceOriginalEl = cartContent.querySelector('.price_original');
                const priceTotalEl = cartContent.querySelector('.price_total');
                const unitOriginal = parseInt(priceOriginalEl.dataset.unit);
                const unitTotal = parseInt(priceTotalEl.dataset.unit);

                priceOriginalEl.textContent = (unitOriginal * count).toLocaleString() + '원';
                priceTotalEl.textContent = (unitTotal * count).toLocaleString() + '원';

                const cartNoInput = cartContent.querySelector('input[name="cartNo"]');
                const cartNoValue = cartNoInput.value;

                updateCartProductCount(cartNoValue, count);

                updateSummary();
            }
        });
    });

    // 수량 변경 업데이트
    function updateCartProductCount(cartNo, newQuantity) {
        const formData = new URLSearchParams();
        formData.append("cartNo", cartNo);
        formData.append("newQuantity", newQuantity);

        fetch('/update/cartProdCount', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData.toString()
        })
            .then(response => response.json())
            .then(data => {
                console.log('Cart updated:', data);
            })
            .catch(error => {
                console.error('Error updating cart:', error);
            });
    }

    // 체크박스 변화 시 요약 업데이트
    itemCheckboxes.forEach(cb => cb.addEventListener('change', updateSummary));

    // 전체 선택 기능
    checkAll.addEventListener('change', function () {
        const isChecked = this.checked;
        itemCheckboxes.forEach(cb => cb.checked = isChecked);
        updateSummary();
    });

    // 초기화 시 전체 해제 및 요약 초기 계산
    itemCheckboxes.forEach(cb => cb.checked = false);
    updateSummary();

    // 판매자별로 상품 정렬
    const cartItems = Array.from(document.querySelectorAll('.cart_content'));
    const sellerGroups = {};
    cartItems.forEach(item => {
        const seller = item.querySelector('.vendor-name').textContent.trim();
        if (!sellerGroups[seller]) {
            sellerGroups[seller] = [];
        }
        sellerGroups[seller].push(item);
    });
    const sortedSellerKeys = Object.keys(sellerGroups).sort();
    const parent = cartItems[0]?.parentNode;
    if (parent) {
        sortedSellerKeys.forEach(seller => {
            const group = sellerGroups[seller];
            group.forEach(item => {
                parent.appendChild(item);
            });
        });
    }

    // 선택 항목 삭제
    const deleteSelectedBtn = document.querySelector('.cart_header button[type="button"]');

    if (deleteSelectedBtn) {

        function updateSummaryAfterDelete() {
            const allCheckboxes = document.querySelectorAll('.cart_content input[type="checkbox"]');
            const checkAllLabel = document.querySelector('label[for="check-all"]');
            const checkAll = document.getElementById('check-all');

            if (checkAllLabel) {
                checkAllLabel.textContent = `전체선택(${allCheckboxes.length})`;
            }

            if (checkAll) {
                checkAll.checked = false;
            }

            if (allCheckboxes.length === 0) {
                document.getElementById('summary-count').textContent = '0';
                document.getElementById('summary-amount').textContent = '0';
                document.getElementById('summary-discount').textContent = '0';
                document.getElementById('summary-shipping').textContent = '0';
                document.getElementById('summary-point').textContent = '0';
                document.getElementById('summary-total').textContent = '0원';
            }
        }

        deleteSelectedBtn.addEventListener('click', async () => {
            const selectedCheckboxes = document.querySelectorAll('.cart_content input[type="checkbox"]:checked');

            if (selectedCheckboxes.length === 0) {
                alert('삭제할 상품을 선택해주세요.');
                return;
            }

            if (!confirm(`선택된 상품을 삭제하시겠습니까?`)) {
                return;
            }

            const deletePromises = [];

            selectedCheckboxes.forEach(checkbox => {
                const cartNo = checkbox.value;
                const cartItemElement = checkbox.closest('.cart_content');
                const deleteUrl = `/product/removeItem?cartNo=${cartNo}`;

                const deletePromise = fetch(deleteUrl, {
                    method: 'GET'
                })
                    .then(response => {
                        if (response.ok) {
                            return response.text().then(text => ({ success: text === '1', element: cartItemElement }));
                        } else {
                            alert(`상품(번호: ${cartNo}) 삭제 중 서버 오류 발생 (Status: ${response.status})`);
                            return { success: false, element: cartItemElement };
                        }
                    })
                    .catch(error => {
                        console.error('삭제 요청 실패:', error);
                        alert(`상품(번호: ${cartNo}) 삭제 중 네트워크 오류 발생`);
                        return { success: false, element: cartItemElement };
                    });

                deletePromises.push(deletePromise);
            });

            try {
                const results = await Promise.all(deletePromises);

                let deletedCount = 0;
                results.forEach(result => {
                    if (result.success && result.element) {
                        result.element.remove();
                        deletedCount++;
                    } else if (!result.success && result.element) {
                        const failedCheckbox = result.element.querySelector('input[type="checkbox"]');
                        if (failedCheckbox) failedCheckbox.checked = false;
                    }
                });

                updateSummaryAfterDelete();

                if (deletedCount > 0) {
                    // 성공한 경우 메시지 없이 조용히 넘어감
                } else {
                    alert('상품 삭제에 실패했습니다. 다시 시도해주세요.');
                }

            } catch (error) {
                updateSummaryAfterDelete();
            }
        });
    }

    // 예상 도착일 표시 (3일 뒤)
    const arrivalSpans = document.querySelectorAll('.arrival-date');
    const arrivalDate = new Date();
    arrivalDate.setDate(arrivalDate.getDate() + 3);
    let formattedDate = arrivalDate.toLocaleDateString('ko-KR', {
        month: 'numeric',
        day: 'numeric',
        weekday: 'short'
    });
    formattedDate = formattedDate.replace('. ', '/');
    formattedDate = formattedDate.replace('. ', '');
    const arrivalText = `${formattedDate} 도착예정`;
    arrivalSpans.forEach(span => {
        span.textContent = arrivalText;
    });

});
