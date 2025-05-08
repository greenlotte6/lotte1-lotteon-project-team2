document.addEventListener('DOMContentLoaded', function(){

    const modal = document.querySelectorAll('.modal');
    const modalClose = document.querySelectorAll('.modalClose');

    const purchase_confirm_modal = document.querySelector('.purchase_confirm_modal');
    const product_review_modal = document.querySelector('.product_review_modal');
    const return_request_modal = document.querySelector('.return_request_modal');
    const exchange_request_modal = document.querySelector('.exchange_request_modal');
    const seller_info_modal = document.querySelector('.seller_info_modal');
    const order_details_modal = document.querySelector('.order_details_modal');
    const inquiry_modal = document.querySelector('.inquiry_modal');


    const purchase_confirm_btn = document.querySelectorAll('.purchase_confirm_btn');
    const product_review_btn = document.querySelectorAll('.product_review_btn');
    const return_request_btn = document.querySelectorAll('.return_request_btn');
    const exchange_request_btn = document.querySelectorAll('.exchange_request_btn');
    const seller_info_a = document.querySelectorAll('.seller_info_a');
    const order_details_a = document.querySelectorAll('.order_details_a');
    const inquiry_btn = document.querySelectorAll('.inquiry_btn');


    // 숫자 포맷 함수
    function formatNumber(num) {
        return Number(num).toLocaleString();
    }

    purchase_confirm_btn.forEach(function(btn){
        btn.addEventListener('click', function(){

            const orderStatus  = btn.getAttribute('data-orderstatus');

            if(orderStatus === "구매확정"){
                alert("이미 구매확정입니다.");
                return;
            }

            purchase_confirm_modal.style.display ='block';
        });
    });

    product_review_btn.forEach(function(btn){
        btn.addEventListener('click', function(){
            const prodName = btn.getAttribute('data-prod-name');

            document.getElementById('modalProdName').textContent = prodName;

            product_review_modal.style.display ='block';
        });
    });
    
    return_request_btn.forEach(function(btn){
        btn.addEventListener('click', function(){

            const orderDate    = btn.getAttribute('data-orderdate');
            const itemNo       = btn.getAttribute('data-itemno');
            const prodName     = btn.getAttribute('data-prodname');
            const company      = btn.getAttribute('data-company');
            const itemCount    = btn.getAttribute('data-itemcount');
            const itemPrice    = btn.getAttribute('data-itemprice');
            const productImage = btn.getAttribute('data-productimage');
            const itemDiscount = btn.getAttribute('data-itemdiscount');
            const orderStatus  = btn.getAttribute('data-orderstatus');

            const totalPrice = itemPrice - itemDiscount;

            // 모달에 데이터 넣기
            document.getElementById('returnModalOrderDate').textContent = orderDate;
            document.getElementById('returnModalProductImage').src = productImage;
            document.getElementById('returnModalOrderNo').textContent = '주문번호 : ' + itemNo;
            document.getElementById('returnModalCompany').textContent = company;
            document.getElementById('returnModalProdName').textContent = prodName;
            document.getElementById('returnModalItemCount').textContent = '수량 : ' + itemCount + '개';
            document.getElementById('returnModalSellPrice').textContent = '판매가 : ' + formatNumber(itemPrice) + '원';
            document.getElementById('returnModalDiscount').textContent = '할인 : -' + formatNumber(itemDiscount) + '원';
            document.getElementById('returnModalTotalPrice').textContent = '결제금액 : ' + formatNumber(totalPrice) + '원';
            document.getElementById('returnModalOrderStatus').textContent = orderStatus;


            if(orderStatus === "반품신청"){
                alert("이미 반품 신청이 접수되어 있습니다.")
            }else if(orderStatus === "구매확정"){
                alert("구매 확정 후에는 반품 신청이 불가능합니다. 문의사항은 고객센터로 연락해 주세요.")
            }else{
                return_request_modal.style.display ='block';
            }
            
        });
    });

    exchange_request_btn.forEach(function(btn){
        btn.addEventListener('click', function(){


            const orderDate    = btn.getAttribute('data-orderdate');
            const itemNo       = btn.getAttribute('data-itemno');
            const prodName     = btn.getAttribute('data-prodname');
            const company      = btn.getAttribute('data-company');
            const itemCount    = btn.getAttribute('data-itemcount');
            const itemPrice    = btn.getAttribute('data-itemprice');
            const productImage = btn.getAttribute('data-productimage');
            const itemDiscount = btn.getAttribute('data-itemdiscount');
            const orderStatus  = btn.getAttribute('data-orderstatus');

            // 계산
            const totalPrice = itemPrice - itemDiscount;

            // 모달에 데이터 넣기
            document.getElementById('exchangeModalOrderDate').textContent = orderDate;
            document.getElementById('modalProductImage').src = productImage;
            document.getElementById('exchangeModalOrderNo').textContent = '주문번호 : ' + itemNo;
            document.getElementById('exchangeModalCompany').textContent = company;
            document.getElementById('exchangeModalProdName').textContent = prodName;
            document.getElementById('exchangeModalItemCount').textContent = '수량 : ' + itemCount + '개';
            document.getElementById('exchangeModalSellPrice').textContent = '판매가 : ' + formatNumber(itemPrice) + '원';
            document.getElementById('exchangeModalDiscount').textContent = '할인 : -' + formatNumber(itemDiscount) + '원';
            document.getElementById('exchangeModalTotalPrice').textContent = '결제금액 : ' + formatNumber(totalPrice) + '원';
            document.getElementById('exchangeModalOrderStatus').textContent = orderStatus;

            if(orderStatus === "교환신청"){
                alert("이미 교환 신청이 접수되어 있습니다.")
            }else if(orderStatus === "구매확정"){
                alert("구매 확정 후에는 교환 신청이 불가능합니다. 문의사항은 고객센터로 연락해 주세요.")
            }else{
                exchange_request_modal.style.display ='block';
            }


        });
    });

    seller_info_a.forEach(function(btn){
        btn.addEventListener('click', function(){
            seller_info_modal.style.display ='block';
        });
    });

    order_details_a.forEach(function(btn){
        btn.addEventListener('click', function(){
            order_details_modal.style.display ='block';
        });
    });

    inquiry_btn.forEach(function(btn){
        btn.addEventListener('click', function(){
            inquiry_modal.style.display ='block';
            seller_info_modal.style.display = 'none';
        });
    });


    // 모달 닫기 버튼
    modalClose.forEach(function(btn) {
        btn.addEventListener('click', function() {
            // 버튼의 가장 가까운 .modal 클래스를 가진 부모 찾기
            const modal = btn.closest('.modal');
            
            if(modal){
                modal.style.display = 'none';
            }
        });
    });
    
    
});