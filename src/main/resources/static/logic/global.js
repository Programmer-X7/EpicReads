// Search Bar Functionality

document.addEventListener('DOMContentLoaded', function () {
        const searchBtn = document.getElementById('search-btn');
        const searchSection = document.getElementById('search-section');

        // Toggle search section visibility
        searchBtn.addEventListener('click', function () {
            searchSection.classList.toggle('show');
        });

        // Hide search section when clicking outside of it
        document.addEventListener('click', function (event) {
            if (!searchSection.contains(event.target) && event.target !== searchBtn) {
                searchSection.classList.remove('show');
            }
        });
});

// CashFree
const payNowButton = document.getElementById('payNow');
const cfSessionId = document.getElementById('cfPaymentSessionId').value;

console.log(cfSessionId);

const payNow = () => {
    const cashfree = Cashfree({
      mode: "sandbox",
    });

    let checkoutOptions = {
      paymentSessionId: cfSessionId,
      redirectTarget: "_self",
    };

    cashfree.checkout(checkoutOptions);
  };

  payNowButton.addEventListener('click', payNow);
