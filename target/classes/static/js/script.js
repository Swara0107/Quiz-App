// ===== SAVE QUIZ =====
function saveQuiz() {
    fetch('/saveQuiz?quizId=' + new URLSearchParams(window.location.search).get('quizId'), {
        method: 'POST'
    }).then(response => {
        if (response.ok) {
            location.reload();
        }
    });
}

// ===== QUESTION TYPE HANDLING =====
document.addEventListener('DOMContentLoaded', function() {
    const typeSelect = document.getElementById('type');
    const optionsDiv = document.getElementById('options');
    const optionInputs = optionsDiv.querySelectorAll('input');

    function toggleOptions() {
        const selectedType = typeSelect.value;
        if (selectedType === 'MCQ') {
            optionsDiv.style.display = 'block';
            optionInputs.forEach(input => input.required = true);
        } else {
            optionsDiv.style.display = 'none';
            optionInputs.forEach(input => input.required = false);
        }
    }

    if (typeSelect) {
        typeSelect.addEventListener('change', toggleOptions);
        toggleOptions(); // Initial call
    }
});
