document.addEventListener('DOMContentLoaded', function() {
    var questions = document.querySelectorAll('.faq-question');
    questions.forEach(function(q) {
        q.addEventListener('click', function() {
            var item = q.parentElement;
            item.classList.toggle('active');
        });
    });
});