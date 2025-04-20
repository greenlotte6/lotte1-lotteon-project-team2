//문의내역 상태 글자색
document.addEventListener('DOMContentLoaded', function(){
    document.querySelectorAll('.inquiry-status').forEach(function(el) {
        if (el.textContent.trim() === '답변완료') {
            el.style.color = '#00b050';
        }else if(el.textContent.trim() === '검토중'){
            el.style.color = 'red';
        }
    });
});