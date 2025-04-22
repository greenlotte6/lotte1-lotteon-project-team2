// /js/common/daumPostcode.js
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById("postcode").value = data.zonecode;
            document.getElementById("address").value = data.roadAddress || data.jibunAddress;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}