document.addEventListener('DOMContentLoaded', (event) => {
    const btnNumbers = document.querySelectorAll('.quantity-btn');
    btnNumbers.forEach(button => {
        button.addEventListener('click', function() {
            const form = this.closest('form');
            const input = form.querySelector('input[type="number"]');
            let currentValue = parseInt(input.value);
            const type = this.getAttribute('data-type');
            const actionInput = form.querySelector('input[name="action"]');

            if (type === 'plus') {
                input.value = currentValue + 1;
                actionInput.value = 'plus';
            } else if (type === 'minus' && currentValue > parseInt(input.min)) {
                input.value = currentValue - 1;
                actionInput.value = 'minus';
            }

            form.submit();
        });
    });
});
