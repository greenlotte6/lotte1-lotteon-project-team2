document.addEventListener("DOMContentLoaded", function () {

    // 상품 수량 조절 기능
    const plusBtns = document.querySelectorAll('.btn-plus');
    const minusBtns = document.querySelectorAll('.btn-minus');
    const quantityEls = document.querySelectorAll('#quantity');
    const quantityInputs = document.querySelectorAll('.quantity-input');  // input 요소
    const priceTotals = document.querySelectorAll('.price_total');

    const unitPrices = Array.from(priceTotals).map((el, i) => {
        const priceText = el.textContent.replace(/[^0-9]/g, '');
        const initialCount = parseInt(quantityEls[i].textContent);
        return parseInt(priceText) / initialCount;
    });

    // 상품 수량 조절 기능 (+ 버튼)
    plusBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            let count = parseInt(quantityEls[index].textContent);
            count++;
            quantityEls[index].textContent = count;
            quantityInputs[index].value = count;

            const totalPrice = unitPrices[index] * count;
            priceTotals[index].textContent = totalPrice.toLocaleString() + '원';
        });
    });

    // 상품 수량 조절 기능 (- 버튼)
    minusBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            let count = parseInt(quantityEls[index].textContent);
            if (count > 1) {
                count--;
                quantityEls[index].textContent = count;
                quantityInputs[index].value = count;

                const totalPrice = unitPrices[index] * count;
                priceTotals[index].textContent = totalPrice.toLocaleString() + '원';
            }
        });
    });

    // input 필드에서 수량 변경 시, span 값도 업데이트
    quantityInputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            let count = parseInt(input.value);
            if (count > 0) {
                quantityEls[index].textContent = count;  // span 값 업데이트
                const totalPrice = unitPrices[index] * count;
                priceTotals[index].textContent = totalPrice.toLocaleString() + '원';
            }
        });
    });



    // 탭 메뉴 기능
    const tabs = document.querySelectorAll('.tab-menu li');
    const panes = document.querySelectorAll('.tab-pane');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            panes.forEach(p => p.classList.remove('active'));

            tab.classList.add('active');
            const target = tab.getAttribute('data-tab');
            const targetPane = document.getElementById(target);
            document.getElementById(target).classList.add('active');

            targetPane.classList.add('active');

            targetPane.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        });
    });

    const tabMenu = document.querySelector('.tab-menu');
    const offsetTop = tabMenu.offsetTop;

    window.addEventListener('scroll', () => {
        if(window.scrollY >= offsetTop) {
            tabMenu.classList.add('fixed');
        } else {
            tabMenu.classList.remove('fixed');
        }
    });



    // 도착 예정일 표시 기능
    const arrivalSpan = document.getElementById("arrivalDate");
    const today = new Date();
    today.setDate(today.getDate() + 3);
    const weekdays = ['일', '월', '화', '수', '목', '금', '토'];
    const formatted = `${today.getMonth() + 1}/${today.getDate()}(${weekdays[today.getDay()]})`;
    arrivalSpan.textContent = formatted;



    // 리뷰 목록 AJAX 처리 및 UI 업데이트 기능
    const reviewsContainer = document.querySelector('.reviews');
    let currentSortType = null; // 현재 정렬 방식을 저장하는 변수
    function updateReviewListUI(reviewData) {
        const existingReviewsContent = reviewsContainer.querySelectorAll('.reviews_content');
        existingReviewsContent.forEach(review => review.remove());
        reviewData.dtoList.forEach(review => {
            const reviewContent = document.createElement('div');
            reviewContent.classList.add('reviews_content');
            let starsHTML = '';
            for (let i = 0; i < Math.floor(review.rating); i++) {
                starsHTML += '<span>⭐</span>';
            }
            reviewContent.innerHTML = `
                    <p>${review.uid.substring(0, 4)}***</p>
                    <div class="stars">
                        ${starsHTML}
                        <p>${review.rating}</p>
                    </div>
                    <p>판매자 : ${review?.product?.company || '정보 없음'}</p>
                    <p>${review.content.replace(/\n/g, '<br>')}</p>
                     ${review.snameImage1 ? `<img src="/upload/review/${review.snameImage1}" alt="리뷰 이미지 1">` : ''}
                     ${review.snameImage2 ? `<img src="/upload/review/${review.snameImage2}" alt="리뷰 이미지 2">` : ''}
                `;
            reviewsContainer.appendChild(reviewContent);
        });
        let reviewsPage = reviewsContainer.querySelector('.reviews_page');
        if (!reviewsPage) {
            reviewsPage = document.createElement('div');
            reviewsPage.classList.add('reviews_page');
        }
        reviewsPage.innerHTML = '';
        let paginationHTML = '';
        if (reviewData.prev) {
            paginationHTML += `<a href="#" class="prev" data-page="${reviewData.start - 1}">&lt;</a>`;
        }
        for (let i = reviewData.start; i <= reviewData.end; i++) {
            const currentClass = i === reviewData.pg ? 'current' : 'num';
            paginationHTML += `<a href="#" class="${currentClass}" data-page="${i}">${i}</a>`;
        }
        if (reviewData.next) {
            paginationHTML += `<a href="#" class="next" data-page="${reviewData.end + 1}">&gt;</a>`;
        }
        reviewsPage.innerHTML = paginationHTML;
        reviewsContainer.appendChild(reviewsPage);

        console.log("AJAX Response Data:", reviewData);
    }
    function fetchReviews(page, prodNo, sortType) {
        let requestUrl = `/product/reviewList?pg=${page}&prodNo=${prodNo}`;
        if (sortType) {
            requestUrl += `&sortType=${sortType}`;
        }
        fetch(requestUrl)
            .then(response => response.json())
            .then(data => {
                updateReviewListUI(data);
            })
            .catch(error => {
            });
    }
    function handlePaginationClick(event) {
        if (event.target.matches('.reviews_page a')) {
            event.preventDefault();
            const pageNumber = parseInt(event.target.getAttribute('data-page'));
            if (!isNaN(pageNumber) && pageNumber > 0) {
                fetchReviews(pageNumber, document.getElementById('prodNo').value, currentSortType); // 현재 정렬 방식 유지
            }
        }
    }
    function handleSortClick(event) {
        if (event.target.matches('.sort-link')) {
            event.preventDefault();
            const sortType = event.target.getAttribute('data-sort-type');
            currentSortType = sortType; // 현재 정렬 방식 업데이트
            fetchReviews(1, document.getElementById('prodNo').value, currentSortType); // 정렬 후 첫 페이지 로드
        }
    }
    fetchReviews(1, document.getElementById('prodNo').value, currentSortType);
    reviewsContainer.addEventListener('click', handlePaginationClick);
    reviewsContainer.addEventListener('click', handleSortClick);


    // coupon 모달 창
    const couponButton = document.querySelector('.coupon');
    const couponModal = document.getElementById('couponModal');
    const couponListDiv = document.getElementById('couponList');
    const companySpan = document.querySelector('.department');
    const selectedCoupons = new Map(); // 선택된 쿠폰 저장 (couponId: element)
    const applyCouponButton = couponModal ? couponModal.querySelector('.modal-buttons button') : null;

    if (couponButton && couponModal && couponListDiv && companySpan && applyCouponButton) {
        couponButton.addEventListener('click', function () {
            const companyName = companySpan.textContent.trim();
            const couponUrl = `/product/coupon?company=${companyName}`;

            fetch(couponUrl, {
                method: 'GET',
                credentials: 'include'
            })
                .then(response => {
                    if (response.redirected) {
                        const currentUrl = window.location.href;
                        const prod = currentUrl.split("=")[1];
                        window.location.href = "/product/ViewLoginCheck?prodNo=" + prod;
                        return;
                    }
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(coupons => {
                    if (!coupons) return;
                    couponListDiv.innerHTML = '';
                    const currentDate = new Date();
                    const oneDay = 24 * 60 * 60 * 1000;

                    const handleCouponClick = function (couponData, element) {
                        const couponId = couponData.cno;
                        const validTo = couponData.validTo;
                        if (selectedCoupons.has(couponId)) {
                            element.style.border = '';
                            selectedCoupons.delete(couponId);
                            console.log('Coupon Deselected:', { cno: couponId, validTo: validTo });
                        } else {
                            element.style.border = '2px solid red';
                            selectedCoupons.set(couponId, element);
                            console.log('Coupon Selected:', { cno: couponId, validTo: validTo });
                        }
                    };
                    const renderCouponItem = (coupon) => {
                        const couponItem = document.createElement('div');
                        couponItem.classList.add('coupon-item');
                        couponItem.innerHTML = `
                        <p>${coupon.benefit}</p>
                        <p>${coupon.couponName}</p>
                        <p>쿠폰 종류: ${coupon.couponType} (${coupon.minPrice} 이상 구매 시 사용 가능)</p>
                        <input type="hidden" class="coupon-id" value="${coupon.cno}">
                    `;
                        couponItem.dataset.cno = coupon.cno;
                        couponItem.dataset.validto = coupon.validTo;
                        const validToDate = new Date(coupon.validTo);
                        const timeDiff = validToDate.getTime() - currentDate.getTime();
                        const daysLeft = Math.ceil(timeDiff / oneDay);
                        if (daysLeft < 7 && daysLeft >= 0) {
                            const tooltip = document.createElement('span');
                            tooltip.classList.add('coupon-expiry');
                            tooltip.textContent = `${daysLeft}일 남음`;
                            couponItem.appendChild(tooltip);
                        }
                        couponItem.addEventListener('click', function () {
                            handleCouponClick(coupon, this);
                        });
                        couponListDiv.appendChild(couponItem);
                    };
                    if (!coupons || (Array.isArray(coupons) && coupons.length === 0) || (typeof coupons === 'object' && !coupons.cno && !Array.isArray(coupons)) ) {
                        couponListDiv.innerHTML = '<p>현재 적용 가능한 쿠폰이 없습니다.</p>';
                    } else if (Array.isArray(coupons)) {
                        coupons.forEach(renderCouponItem);
                    } else if (coupons && typeof coupons === 'object' && coupons.cno) {
                        renderCouponItem(coupons);
                    }
                    couponModal.style.display = 'block';
                    selectedCoupons.clear();
                })
                .catch(error => {
                    console.error('Error fetching or processing coupons:', error);
                    couponListDiv.innerHTML = '<p>쿠폰 정보를 불러오는 중 오류가 발생했습니다.</p>';
                    couponModal.style.display = 'block';
                });
        });

        const couponCloseButton = couponModal.querySelector('.close-button');
        if (couponCloseButton) {
            couponCloseButton.addEventListener('click', function () {
                couponModal.style.display = 'none';
            });
        }

        window.addEventListener('click', function (event) {
            if (event.target == couponModal) {
                couponModal.style.display = 'none';
            }
        });

        if (applyCouponButton) {
            applyCouponButton.addEventListener('click', function () {
                if (selectedCoupons.size > 0) {
                    applyCouponButton.disabled = true;
                    applyCouponButton.textContent = '요청 중...';
                    const requests = [];
                    selectedCoupons.forEach((couponElement) => {
                        const couponCno = couponElement.dataset.cno;
                        console.log("couponCno: " + couponCno);

                        const formData = new URLSearchParams();
                        formData.append("cno", couponCno);

                        const request = fetch('/product/couponIssue', {
                            method: 'POST',
                            credentials: 'include',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            body: formData.toString()
                        })
                            .then(response => response.text())
                            .then(message => {
                                console.log(`[응답 메시지] ${couponCno}: ${message}`);
                                return message;
                            })
                            .catch(error => {
                                console.error(`Coupon CNO: ${couponCno} - Error:`, error);
                                return `쿠폰 ${couponCno} 발급 실패`;
                            });

                        requests.push(request);
                    });
                    Promise.all(requests).then(results => {
                        const successCount = results.filter(msg => msg.includes('발급되었습니다')).length;
                        if (successCount > 0) {
                            alert(`쿠폰 ${successCount}개가 발급되었습니다.`);
                        }
                        applyCouponButton.disabled = false;
                        applyCouponButton.textContent = '할인 혜택 받기';
                        couponModal.style.display = 'none';
                        selectedCoupons.clear();
                        couponListDiv.querySelectorAll('.coupon-item').forEach(item => item.style.border = '');
                    });

                } else {
                    alert('적용할 쿠폰을 선택해주세요.');
                }
            });
        }
    } else {
        console.warn('Coupon modal functionality requires .coupon button, #couponModal, #couponList, .department span, and apply button inside modal.');
    }



    // QnA ajax 처리
    const prodNoElement = document.getElementById('prodNo');
    const qnaEl = document.querySelector('.qna_content');
    const qnaContainer = qnaEl ? qnaEl.parentNode : null;
    const qnaPagination = document.querySelector('.qna_page');
    function updateQnaListUI(qnaData) {
        if (qnaData && Array.isArray(qnaData.dtoList)) {
            const existingQnaContents = qnaContainer.querySelectorAll('.qna_content');
            existingQnaContents.forEach(content => content.remove());
            qnaData.dtoList.forEach(inquiry => {
                const qnaContent = document.createElement('div');
                qnaContent.classList.add('qna_content');
                qnaContent.innerHTML = `
            <p>${inquiry.cateV1} > ${inquiry.cateV2}</p>
            <p>${inquiry.content}</p>
            <div class="answer">${inquiry.answer ? `<p>${inquiry.answer}</p><p>롯데쇼핑(주)본점<span>판매자</span></p>` : '<p>답변 대기 중입니다.</p>'}</div>
            <div>
                <a href="#">답변달기</a>
                <p>${new Date(inquiry.wdate).toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' })}</p>
            </div>
        `;
                qnaContainer.appendChild(qnaContent);
            });
            qnaPagination.innerHTML = '';
            if (qnaData.prev) {
                const prevLink = document.createElement('a');
                prevLink.href = '#';
                prevLink.textContent = '<';
                prevLink.setAttribute('data-page', qnaData.start - 1);
                qnaPagination.appendChild(prevLink);
            }
            for (let i = qnaData.start; i <= qnaData.end; i++) {
                const pageLink = document.createElement('a');
                pageLink.href = '#';
                pageLink.textContent = i;
                pageLink.setAttribute('data-page', i);
                if (i === qnaData.pg) {
                    pageLink.classList.add('current');
                } else {
                    pageLink.classList.add('num');
                }
                qnaPagination.appendChild(pageLink);
            }
            if (qnaData.next) {
                const nextLink = document.createElement('a');
                nextLink.href = '#';
                nextLink.textContent = '>';
                nextLink.setAttribute('data-page', qnaData.end + 1);
                qnaPagination.appendChild(nextLink);
            }
            qnaContainer.appendChild(qnaPagination);
        } else {
        }
    }
    function fetchQnaList(page, prodNo) {
        const url = `/product/qnaList?pg=${page}&prodNo=${prodNo}`;
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                updateQnaListUI(data);
            })
            .catch(error => {
            });
    }
    if (prodNoElement) {
        fetchQnaList(1, prodNoElement.value);
    }
    qnaPagination.addEventListener('click', function (event) {
        if (event.target.tagName === 'A' && event.target.hasAttribute('data-page') && prodNoElement) {
            event.preventDefault();
            const pageNumber = parseInt(event.target.getAttribute('data-page'));
            const prodNoValue = prodNoElement.value;
            if (!isNaN(pageNumber) && pageNumber > 0 && prodNoValue) {
                fetchQnaList(pageNumber, prodNoValue);
            }
        }
    });


    // Qna 모달 창 (이벤트 위임 방식)
    document.addEventListener('click', function(event) {
        if (event.target.classList.contains('qna_content')) {

            var qnaModal = document.getElementById("qnaModal");
            var modalInquiryContent = document.getElementById("modalInquiryContent");
            var modalAnswerContent = document.getElementById("modalAnswerContent");
            var qnaContentElement = event.target;
            var inquiryTextElement = qnaContentElement.querySelector("p:nth-child(2)");
            var answerDiv = qnaContentElement.querySelector(".answer");
            var answerText = "";

            if (answerDiv) {
                answerText = answerDiv.innerHTML;
            } else {
                answerText = "<p>답변 대기 중입니다.</p>";
            }
            if (inquiryTextElement) {
                modalInquiryContent.textContent = inquiryTextElement.textContent;
            }
            modalAnswerContent.innerHTML = answerText;
            if (qnaModal) {
                qnaModal.style.display = "block";
                console.log('모달 표시 시도 (이벤트 위임)!');
            } else {
                console.log('qnaModal 요소가 없습니다 (이벤트 위임)!');
            }
        }
    });
    var closeBtn = document.querySelector("#qnaModal .close-button");
    if (closeBtn) {
        closeBtn.onclick = function() {
            var qnaModal = document.getElementById("qnaModal");
            if (qnaModal) {
                qnaModal.style.display = "none";
            }
        };
    }
    window.onclick = function(event) {
        var qnaModal = document.getElementById("qnaModal");
        if (event.target == qnaModal) {
            qnaModal.style.display = "none";
        }
    };



    // 장바구니
    const cartButton = document.querySelector('form table button.cart');

    if (cartButton) {
        cartButton.addEventListener('click', (event) => {
            event.preventDefault();

            const productNoInput = document.getElementById('productNo');
            const productNo = productNoInput ? productNoInput.value : null;

            const quantityElement = document.getElementById('quantity');
            const quantity = quantityElement ? parseInt(quantityElement.textContent, 10) : 1;

            const optionData = {};
            const optionRows = document.querySelectorAll('form table tr.choice');

            optionRows.forEach((row, index) => {
                const optionTypeElement = row.querySelector('td:first-child');
                const selectElement = row.querySelector('td:nth-child(2) select');

                if (optionTypeElement && selectElement && selectElement.value) {
                    const optKey = `opt${index + 1}`;
                    const optContKey = `opt${index + 1}cont`;
                    optionData[optKey] = optionTypeElement.textContent.trim();
                    optionData[optContKey] = selectElement.value;
                }
            });

            const cartItemData = {
                prodNo: productNo,
                quantity: quantity,
                options: optionData
            };


            fetch('/product/addCart', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(cartItemData)
            })
                .then(response => {
                    if (response.redirected) {
                        console.log('서버로부터 리다이렉트 응답 받음. 로그인 페이지로 이동합니다.');
                        let loginRedirectUrl = '/product/ViewLoginCheck';
                        if (productNo) {
                            loginRedirectUrl += '?prodNo=' + productNo;
                        }
                        window.location.href = loginRedirectUrl;
                        return null;
                    }
                    if (!response.ok) {
                        return response.json()
                            .catch(() => {
                                throw new Error(`서버 응답 오류: ${response.status}`);
                            })
                            .then(errorBody => {
                                const message = errorBody?.message || `서버 응답 오류: ${response.status}`;
                                throw new Error(message);
                            });
                    }
                    return response.json();
                })
                .then(result => {
                    if (result === null) {
                        return;
                    }
                    console.log('장바구니 추가 처리 결과:', result);
                    if (result === 1) {
                        if (confirm('장바구니로 이동하시겠습니까?')) {
                            window.location.href = '/product/cart';
                        } else {
                            console.log('장바구니 이동 취소');
                        }
                    } else {
                        alert('장바구니 요청이 처리되었으나, 추가되지 않았을 수 있습니다. (결과: ' + result + ')');
                        console.warn('장바구니 추가 결과가 1이 아님:', result);
                    }
                })
                .catch(error => {
                    console.error('장바구니 추가 실패:', error);
                    alert(`오류가 발생했습니다: ${error.message}`);
                });
        });
    } else {
        console.error('장바구니 버튼(form table button.cart)을 찾을 수 없습니다.');
    }

});