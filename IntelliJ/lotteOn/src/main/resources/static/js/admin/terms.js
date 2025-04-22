document.addEventListener("DOMContentLoaded", function (){
    const termsBtn = document.getElementById("termsBtn");
    const taxBtn = document.getElementById("taxBtn");
    const financesBtn = document.getElementById("financesBtn");
    const privacyBtn = document.getElementById("privacyBtn");
    const locationBtn = document.getElementById("locationBtn");

    const terms = document.getElementById("terms");
    const tax = document.getElementById("tax");
    const finance = document.getElementById("finance");
    const privacy = document.getElementById("privacy");
    const location = document.getElementById("location");

    let cate;
    let content;

    // 구매 약관
    termsBtn.addEventListener("click", function () {

        cate = "terms"
        content = terms.querySelector('textarea[name="terms"]').value;

        fetch("/admin/terms/modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json" // 또는 form으로 보낼 경우 다른 방식도 가능
            },
            body: JSON.stringify({ cate: cate, content : content }) // 전송 데이터
        })
            .then(response => {
                if (!response.ok) throw new Error("서버 오류");
                alert("약관을 변경했습니다.");
            })
            .catch(err => {
                console.log(err);
                alert("약관 변경에 실패했습니다.");
            });
    });

    // 판매약관
    taxBtn.addEventListener("click", function () {

        cate = "tax"
        content = tax.querySelector('textarea[name="tax"]').value;

        fetch("/admin/terms/modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json" // 또는 form으로 보낼 경우 다른 방식도 가능
            },
            body: JSON.stringify({ cate: cate, content : content }) // 전송 데이터
        })
            .then(response => {
                if (!response.ok) throw new Error("서버 오류");
                alert("약관을 변경했습니다.");
            })
            .catch(err => {
                console.log(err);
                alert("약관 변경에 실패했습니다.");
            });
    });

    // 전자금융거래
    financesBtn.addEventListener("click", function () {

        cate = "finance"
        content = finance.querySelector('textarea[name="finance"]').value;

        fetch("/admin/terms/modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json" // 또는 form으로 보낼 경우 다른 방식도 가능
            },
            body: JSON.stringify({ cate: cate, content : content }) // 전송 데이터
        })
            .then(response => {
                if (!response.ok) throw new Error("서버 오류");
                alert("약관을 변경했습니다.");
            })
            .catch(err => {
                console.log(err);
                alert("약관 변경에 실패했습니다.");
            });
    });

    privacyBtn.addEventListener("click", function () {

        cate = "privacy"
        content = privacy.querySelector('textarea[name="privacy"]').value;

        fetch("/admin/terms/modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json" // 또는 form으로 보낼 경우 다른 방식도 가능
            },
            body: JSON.stringify({ cate: cate, content : content }) // 전송 데이터
        })
            .then(response => {
                if (!response.ok) throw new Error("서버 오류");
                alert("약관을 변경했습니다.");
            })
            .catch(err => {
                console.log(err);
                alert("약관 변경에 실패했습니다.");
            });
    });

    locationBtn.addEventListener("click", function () {

        cate = "location"
        content = location.querySelector('textarea[name="location"]').value;

        fetch("/admin/terms/modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json" // 또는 form으로 보낼 경우 다른 방식도 가능
            },
            body: JSON.stringify({ cate: cate, content : content }) // 전송 데이터
        })
            .then(response => {
                if (!response.ok) throw new Error("서버 오류");
                alert("약관을 변경했습니다.");
            })
            .catch(err => {
                console.log(err);
                alert("약관 변경에 실패했습니다.");
            });
    });


});