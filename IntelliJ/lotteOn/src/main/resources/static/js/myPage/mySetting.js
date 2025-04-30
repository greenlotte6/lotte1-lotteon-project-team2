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

            const inputs = this.parentElement.querySelectorAll('.phone-input');
            const emailInputs = this.parentElement.querySelectorAll('.email-input');
            const isEdit = this.textContent === '수정';

            inputs.forEach(input => {
                input.readOnly = !isEdit;

                if (isEdit) {
                    input.classList.remove('readonly-style'); // 기본 스타일 제거
                    input.classList.add('editing');           // 수정 스타일 추가
                } else {
                    input.classList.remove('editing');
                    input.classList.add('readonly-style');    // 다시 기본 스타일 추가
                }
            });

            emailInputs.forEach(input => {
                input.readOnly = !isEdit;

                if (isEdit) {
                    input.classList.remove('readonly-style');
                    input.classList.add('editing');
                } else {
                    input.classList.remove('editing');
                    input.classList.add('readonly-style');
                }
            });

            this.textContent = isEdit ? '저장' : '수정';
        });
    });

    // 탈퇴 버튼
    function confirmWithdrawal() {
        return confirm('정말로 탈퇴하시겠습니까? 탈퇴 시 계정이 비활성화됩니다.');
    }


});
