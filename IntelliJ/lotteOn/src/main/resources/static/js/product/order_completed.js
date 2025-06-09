// 롯데On 주문 페이지 전체 스크립트
document.addEventListener("DOMContentLoaded", function () {

    // 판매자별 테이블 접기
    const table = document.querySelector('.order_content'); // 원본 테이블
    const container = document.getElementById('tables-container');
    const rows = Array.from(table.querySelectorAll('tr')).slice(1); // 헤더 제외

    const vendorGroups = {};

    rows.forEach(row => {
        const vendor = row.querySelector('.vendor-name')?.innerText.trim();
        if (!vendor) return;
        if (!vendorGroups[vendor]) vendorGroups[vendor] = [];
        vendorGroups[vendor].push(row);
    });

    container.innerHTML = '';

    Object.entries(vendorGroups).forEach(([vendor, trList]) => {
        const section = document.createElement('div');
        section.className = 'vendor-section';

        const header = document.createElement('div');
        header.className = 'vendor-header';

        const titleSpan = document.createElement('span');
        titleSpan.className = 'vendor-title';
        titleSpan.innerText = vendor;

        const iconSpan = document.createElement('span');
        iconSpan.className = 'vendor-toggle-icon';
        iconSpan.innerText = '▼';

        header.appendChild(titleSpan);
        header.appendChild(iconSpan);

        const groupTable = document.createElement('table');
        groupTable.className = 'order_content';

        const headerRow = table.querySelector('tr').cloneNode(true);
        groupTable.appendChild(headerRow);

        const clonedTrs = [];

        trList.forEach((tr, i) => {
            const clonedRow = tr.cloneNode(true);
            if (i !== 0) clonedRow.style.display = 'none';
            clonedTrs.push(clonedRow);
            groupTable.appendChild(clonedRow);
        });

        let isOpen = false;
        header.addEventListener('click', () => {
            isOpen = !isOpen;
            clonedTrs.forEach((tr, i) => {
                if (i === 0) return; // 첫 번째는 항상 보이게
                tr.style.display = isOpen ? 'table-row' : 'none';
            });
            iconSpan.innerText = isOpen ? '▲' : '▼';
        });

        section.appendChild(header);
        section.appendChild(groupTable);
        container.appendChild(section);
    });

});