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


    purchase_confirm_btn.forEach(function(btn){
        btn.addEventListener('click', function(){
            purchase_confirm_modal.style.display ='block';
        });
    });

    product_review_btn.forEach(function(btn){
        btn.addEventListener('click', function(){
            product_review_modal.style.display ='block';
        });
    });
    
    return_request_btn.forEach(function(btn){
        btn.addEventListener('click', function(){
            return_request_modal.style.display ='block';
        });
    });

    exchange_request_btn.forEach(function(btn){
        btn.addEventListener('click', function(){
            exchange_request_modal.style.display ='block';
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