document.addEventListener('DOMContentLoaded', function(){
    const modal = document.querySelector('.modal');
    const modalClose = document.querySelectorAll('.modalClose');


    const purhase_confirm_modal = document.querySelector('.purhase_confirm_modal');
    const product_review_modal = document.querySelector('.product_review_modal');
    const return_request_modal = document.querySelector('.return_request_modal');
    const exchange_request_modal = document.querySelector('.exchange_request_modal');
    const seller_info_modal = document.querySelector('.seller_info_modal');
    const order_detail_modal = document.querySelector('.order_detail_modal');

    const purhase_confirm_btn = document.querySelector('.purhase_confirm_btn');
    const product_review_btn = document.querySelector('.product_review_btn');
    const return_request_btn = document.querySelector('.return_request_btn');
    const exchange_request_btn = document.querySelector('.exchange_request_btn');
    const seller_info_a = document.querySelector('.seller_info_a');
    const order_detail_a = document.querySelector('.order_detail_a');

    
    // 구매확정 모달
    purhase_confirm_btn.addEventListener('click', function(){
        purhase_confirm_modal.style.display = 'block';
        
    });

    // 상품평 모달
    product_review_btn.addEventListener('click', function(){
        product_review_modal.style.display = 'block';
        
    });
    
    // 반품신청 모달
    return_request_btn.addEventListener('click', function(){
        return_request_modal.style.display = 'block';
        
    });

    // 교환신청 모달
    exchange_request_btn.addEventListener('click', function(){
        exchange_request_modal.style.display = 'block';
        
    });

    // 주문상세 모달
    order_detail_a.addEventListener('click', function(){
        order_detail_modal.style.display = 'block';
    })

    // 판매자정보 모달
    seller_info_a.addEventListener('click', function(){
        seller_info_modal.style.display = 'block';
    })


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