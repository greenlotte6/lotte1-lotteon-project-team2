document.getElementById('signup-toggle').addEventListener('click', function() {
    const signupItems = document.querySelector('#signup-section .more');
    const toggleButton = document.getElementById('signup-toggle');

    // height 값을 동적으로 변경하여 슬라이드 애니메이션 적용
    if (signupItems.style.height === '0px' || signupItems.style.height === '') {
        signupItems.style.height = `${signupItems.scrollHeight}px`;  // 전체 컨텐츠의 높이를 설정
        toggleButton.textContent = '간단히 보기';
    } else {
        signupItems.style.height = '0px'; // 다시 0으로 설정하여 숨김
        toggleButton.textContent = '더보기';
    }
});

document.getElementById('withdrawal-toggle').addEventListener('click', function() {
    const withdrawalItems = document.querySelector('#withdrawal-section .more');
    const toggleButton = document.getElementById('withdrawal-toggle');

    // height 값을 동적으로 변경하여 슬라이드 애니메이션 적용
    if (withdrawalItems.style.height === '0px' || withdrawalItems.style.height === '') {
        withdrawalItems.style.height = `${withdrawalItems.scrollHeight}px`;  // 전체 컨텐츠의 높이를 설정
        toggleButton.textContent = '간단히 보기';
    } else {
        withdrawalItems.style.height = '0px'; // 다시 0으로 설정하여 숨김
        toggleButton.textContent = '더보기';
    }
});