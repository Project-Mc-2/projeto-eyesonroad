const ctx = document.getElementById('graficoSemanal');

const grafico = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab', 'Dom'],
        datasets: [{
            label: 'Nível de Sonolência',
            data: [2, 3, 5, 2, 8, 6, 1],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                display: false
            }
        },
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
 
});