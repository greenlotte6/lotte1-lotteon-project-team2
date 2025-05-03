document.addEventListener('DOMContentLoaded', () => {
    const $ = (select) => document.querySelectorAll(select);
    const draggables = $('.category_manage_div');
    const container = document.querySelector('#category_manage_div');

    draggables.forEach(el => {
        el.addEventListener('dragstart', () => {
            el.classList.add('dragging');
        });

        el.addEventListener('dragend', () => {
            el.classList.remove('dragging');
        });
    });

    function getDragAfterElement(container, y) {
        const draggableElements = [...container.querySelectorAll('.draggable:not(.dragging)')];

        return draggableElements.reduce((closest, child) => {
            const box = child.getBoundingClientRect();
            const offset = y - box.top - box.height / 2;
            if (offset < 0 && offset > closest.offset) {
                return { offset: offset, element: child };
            } else {
                return closest;
            }
        }, { offset: Number.NEGATIVE_INFINITY }).element;
    }

    container.addEventListener('dragover', e => {
        e.preventDefault();
        const afterElement = getDragAfterElement(container, e.clientY);
        const draggable = document.querySelector('.dragging');
        if (afterElement == null) {
            container.appendChild(draggable);
        } else {
            container.insertBefore(draggable, afterElement);
        }
    });



    const mainDivs = document.querySelectorAll(".Main_div > p");

    mainDivs.forEach(function(pTag) {
        pTag.addEventListener("click", function() {
            const subDiv = this.parentElement.querySelector(".Sub_div");
            if (subDiv) {
                subDiv.style.display = subDiv.style.display === "none" || subDiv.style.display === "" ? "block" : "none";
            }
        });
    });



    const plusBtn = document.querySelector('.category_plus');
    const inputBox = document.getElementById('category_input_box');
    const inputField = document.getElementById('new_main_category');
    const categoryContainer = document.getElementById('category_manage_div');
    const addButtonWrapper = categoryContainer.querySelector('.category_add').closest('.category_manage_div');

    // 버튼 클릭 시 입력창 열고/닫기
    plusBtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (inputBox.style.display === 'block') {
            inputBox.style.display = 'none';
            inputField.value = '';
        } else {
            inputBox.style.display = 'block';
            inputField.focus();
        }
    });

    // 입력창에서 Enter = 추가, ESC = 닫기
    inputField.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            const categoryName = inputField.value.trim();
            if (!categoryName) {
                alert('카테고리명을 입력해주세요.');
                return;
            }

            // 새 카테고리 DOM 추가
            const newDiv = document.createElement('div');
            newDiv.className = 'category_manage_div draggable';
            newDiv.innerHTML = `
                <div class="Main_div">
                    <input type="hidden" name="newCateNo" value="${categoryName}">
                    <p> ▼ ${categoryName}
                        <a href="#" class="category_delete">삭제</a>
                    </p>
                    <div class="Sub_div">
                                 <!-- sub input UI 추가 -->
                        <p class="sub_category_add">
                            <a href="#" class="sub_category_plus">+ 추가</a>
                        </p>
                        <div class="sub_input_box" style="display: none;">
                            <input type="text" class="new_sub_input" placeholder="2차 카테고리 입력">
                        </div>  
                    </div>
                </div>
            `;
            categoryContainer.insertBefore(newDiv, addButtonWrapper);

            // 입력창 초기화 및 닫기
            inputField.value = '';
            inputBox.style.display = 'none';
            
            // 새로 이벤트 달아주기
            const newP = newDiv.querySelector(".Main_div > p");
            newP.addEventListener("click", function () {
                const subDiv = newDiv.querySelector('.Sub_div');
                if (subDiv) {
                    subDiv.style.display = (subDiv.style.display === 'none') ? 'block' : 'none';
                }
            });

            newSubCategoryAddButtons();






        } else if (e.key === 'Escape') {
            inputField.value = '';
            inputBox.style.display = 'none';
        }
    });


    // 메인 카테고리 삭제
    categoryContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('category_delete')) {
            e.preventDefault();

            // 가장 가까운 Main_div 찾아서 hidden input에서 mainCateNo 추출
            const mainDiv = e.target.closest('.Main_div');
            const mainCateNoInput = mainDiv.querySelector('input[name="mainCateNo"]');
            if (!mainCateNoInput) {
                // 새로 추가된 항목일 경우 그냥 DOM에서만 삭제
                mainDiv.parentElement.remove();
                return;
            }

            const mainCateNo = mainCateNoInput.value;

            if (!confirm('정말 삭제하시겠습니까?')) return;

            // 비동기 요청
            fetch('/admin/config/category/delete/main', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `mainCateNo=${mainCateNo}`
            })
                .then(res => res.text())
                .then(result => {
                    if (result === 'ok') {
                        mainDiv.parentElement.remove(); // 상위 category_manage_div 제거
                    }else {
                        alert('삭제에 실패했습니다.');
                    }
                })
                .catch(() => alert('서버와의 통신에 실패했습니다.'));
        }
    });


    categoryContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('subCategory_delete')) {
            e.preventDefault();

            const subP = e.target.closest('p');
            const subCateNoInput = subP.querySelector('input[name="subCateNo"]');

            // subCateNo가 없으면 새로 추가된 항목으로 간주하고 DOM에서만 제거
            if (!subCateNoInput) {
                subP.remove();
                return;
            }

            const subCateNo = subCateNoInput.value;
            const subCategoryName = subP.textContent.replace('삭제', '').trim();

            if (!confirm(`'${subCategoryName}' 서브카테고리를 삭제하시겠습니까?`)) return;

            fetch('/admin/config/category/delete/sub', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `subCateNo=${subCateNo}`
            })
                .then(res => res.text())
                .then(result => {
                    if (result === 'ok') {
                        subP.remove();
                    } else {
                        alert('삭제에 실패했습니다.');
                    }
                })
                .catch(() => alert('서버와의 통신에 실패했습니다.'));
        }
    });

    // 서브카테고리
    function initSubCategoryDragAndDrop() {
        const subContainers = document.querySelectorAll('.Sub_div');

        subContainers.forEach(container => {
            const subItems = container.querySelectorAll('p');
            subItems.forEach(item => {
                item.setAttribute('draggable', true);

                item.addEventListener('dragstart', () => {
                    item.classList.add('dragging');
                });

                item.addEventListener('dragend', () => {
                    item.classList.remove('dragging');
                });
            });

            container.addEventListener('dragover', e => {
                e.preventDefault();
                const afterElement = getSubDragAfterElement(container, e.clientY);
                const dragging = container.querySelector('.dragging');
                if (afterElement == null) {
                    container.appendChild(dragging);
                } else {
                    container.insertBefore(dragging, afterElement);
                }
            });
        });
    }

    function getSubDragAfterElement(container, y) {
        const draggableElements = [...container.querySelectorAll('p:not(.dragging)')];
        return draggableElements.reduce((closest, child) => {
            const box = child.getBoundingClientRect();
            const offset = y - box.top - box.height / 2;
            if (offset < 0 && offset > closest.offset) {
                return { offset: offset, element: child };
            } else {
                return closest;
            }
        }, { offset: Number.NEGATIVE_INFINITY }).element;
    }

    initSubCategoryDragAndDrop();

    // 서브카테고리 추가 버튼
    function initSubCategoryAddButtons() {
        const addButtons = document.querySelectorAll('.sub_category_plus');

        addButtons.forEach(button => {
            button.addEventListener('click', function (e) {
                e.preventDefault();

                const parent = button.closest('.Main_div');
                const inputBox = parent.querySelector('.sub_input_box');
                const inputField = inputBox.querySelector('.new_sub_input');
                const subDiv = parent.querySelector('.Sub_div');

                if (inputBox.style.display === 'block') {
                    inputBox.style.display = 'none';
                    inputField.value = '';
                } else {
                    inputBox.style.display = 'block';
                    inputField.focus();
                }

                inputField.addEventListener('keydown', function handler(e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        const subName = inputField.value.trim();
                        if (!subName) {
                            alert('2차 카테고리명을 입력해주세요.');
                            return;
                        }

                        // 상위 Main_div에서 mainCateNo 추출
                        const mainCateNoInput = parent.querySelector('input[name="mainCateNo"]');
                        const mainCateNo = mainCateNoInput ? mainCateNoInput.value : '';

                        // 새 hidden input의 value = "mainCateNo:subName"
                        const combinedValue = `${mainCateNo}:${subName}`;

                        const newP = document.createElement('p');
                        newP.setAttribute('draggable', 'true');
                        newP.innerHTML = `
                        ${subName}
                        <input type="hidden" name="newSubCateNo" value="${combinedValue}">
                        <a href="#" class="subCategory_delete">삭제</a>
                    `;

                        const addButtonP = subDiv.querySelector('.sub_category_add');
                        subDiv.insertBefore(newP, addButtonP);

                        inputField.value = '';
                        inputBox.style.display = 'none';

                        inputField.removeEventListener('keydown', handler); // 중복 방지
                    } else if (e.key === 'Escape') {
                        inputField.value = '';
                        inputBox.style.display = 'none';
                        inputField.removeEventListener('keydown', handler);
                    }
                });
            });
        });
    }

    // 서브카테고리 추가 버튼
    function newSubCategoryAddButtons() {
        const addButtons = document.querySelectorAll('.sub_category_plus');

        addButtons.forEach(button => {
            button.addEventListener('click', function (e) {
                e.preventDefault();

                const parent = button.closest('.Main_div');
                const inputBox = parent.querySelector('.sub_input_box');
                const inputField = inputBox.querySelector('.new_sub_input');
                const subDiv = parent.querySelector('.Sub_div');

                if (inputBox.style.display === 'block') {
                    inputBox.style.display = 'none';
                    inputField.value = '';
                } else {
                    inputBox.style.display = 'block';
                    inputField.focus();
                }

                inputField.addEventListener('keydown', function handler(e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        const subName = inputField.value.trim();
                        if (!subName) {
                            alert('2차 카테고리명을 입력해주세요.');
                            return;
                        }

                        // 상위 Main_div에서 mainCateNo 추출
                        const mainCateNoInput = parent.querySelector('input[name="newCateNo"]');
                        const mainCateNo = mainCateNoInput ? mainCateNoInput.value : '';

                        // 새 hidden input의 value = "mainCateNo:subName"
                        const combinedValue = `${mainCateNo}:${subName}`;

                        const newP = document.createElement('p');
                        newP.setAttribute('draggable', 'true');
                        newP.innerHTML = `
                        ${subName}
                        <input type="hidden" name="newSubCateNoV2" value="${combinedValue}">
                        <a href="#" class="subCategory_delete">삭제</a>
                    `;

                        const addButtonP = subDiv.querySelector('.sub_category_add');
                        subDiv.insertBefore(newP, addButtonP);

                        inputField.value = '';
                        inputBox.style.display = 'none';

                        inputField.removeEventListener('keydown', handler); // 중복 방지
                    } else if (e.key === 'Escape') {
                        inputField.value = '';
                        inputBox.style.display = 'none';
                        inputField.removeEventListener('keydown', handler);
                    }
                });
            });
        });
    }

    initSubCategoryAddButtons();


});

