document.getElementById('convert-btn').addEventListener('click', function() {
    let amount = document.getElementById('amount').value;
    let fromCurrency = document.getElementById('from-currency').value;
    let toCurrency = document.getElementById('to-currency').value;

    fetch(`/convert?amount=${amount}&fromCurrency=${fromCurrency}&toCurrency=${toCurrency}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById('result').innerHTML = data;
        });
});

document.getElementById('login-btn').addEventListener('click', function() {
    alert('Login functionality to be implemented.');
});

// Currency Chart (Using Dummy Data)
document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('currencyChart').getContext('2d');

    const currencyChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ["Feb 20", "Feb 21", "Feb 22", "Feb 23", "Feb 24"],
            datasets: [{
                label: 'BGN to USD',
                data: [0.55, 0.56, 0.54, 0.55, 0.56],
                borderColor: 'blue',
                borderWidth: 2,
                fill: false
            },
                {
                    label: 'BGN to EUR',
                    data: [0.51, 0.51, 0.51, 0.51, 0.51],
                    borderColor: 'green',
                    borderWidth: 2,
                    fill: false
                }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: { display: true, text: 'Date' }
                },
                y: {
                    title: { display: true, text: 'Exchange Rate' }
                }
            }
        }
    });
});
