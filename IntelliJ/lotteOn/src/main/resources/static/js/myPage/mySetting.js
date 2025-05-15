document.addEventListener('DOMContentLoaded', function(){
    // 이메일 입력
    const email2nd = document.getElementById('email-2nd');
    const emailSelect = document.getElementById('email-select');

    emailSelect.addEventListener('change', function() {
      if (this.value === 'custom') {
        email2nd.value = '';
      } else {
        email2nd.value = this.value;
      }
    });

    // 여러 개의 수정 버튼을 지원하려면 class 사용 권장
    document.querySelectorAll('.editBtn').forEach(btn => {
        btn.addEventListener('click', function () {
            // 버튼이 속한 <td> 내에서만 input을 찾음
            const parentTd = this.parentElement;
            const phoneInputs = parentTd.querySelectorAll('.phone-input');
            const emailInputs = parentTd.querySelectorAll('.email-input');
            const nameInputs = parentTd.querySelectorAll('.name-input');
            const isEdit = this.textContent === '수정';

            // 각각의 input에 대해 수정 가능/불가능 토글
            phoneInputs.forEach(input => {
                input.readOnly = !isEdit;
                input.classList.toggle('editing', isEdit);
                input.classList.toggle('readonly-style', !isEdit);
            });

            emailInputs.forEach(input => {
                input.readOnly = !isEdit;
                input.classList.toggle('editing', isEdit);
                input.classList.toggle('readonly-style', !isEdit);
            });

            nameInputs.forEach(input => {
                input.readOnly = !isEdit;
                input.classList.toggle('editing', isEdit);
                input.classList.toggle('readonly-style', !isEdit);
            });

            // 버튼 숨기기!
            this.style.display = 'none';
        });
    });

    // 탈퇴 버튼
    function confirmWithdrawal() {
        return confirm('정말로 탈퇴하시겠습니까? 탈퇴 시 계정이 비활성화됩니다.');
    }


});
