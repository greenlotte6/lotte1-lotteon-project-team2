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




});
