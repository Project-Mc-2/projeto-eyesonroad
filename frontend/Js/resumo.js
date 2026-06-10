const ctx = document.getElementById('graficoSemanal');

new Chart(ctx, {
    type: 'bar',

    data: {
        labels: ['Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb', 'Dom'],

        datasets: [{
            label: 'Alertas Detectados',
            data: [2, 4, 6, 3, 10, 7, 1],

            backgroundColor: [
                '#0a1c78',
                '#0a1c78',
                '#0a1c78',
                '#0a1c78',
                '#ff4d4d',
                '#0a1c78',
                '#0a1c78'
            ],

            borderRadius: 10,
            borderSkipped: false
        }]
    },

    options: {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

            legend: {
                display: true
            },

            tooltip: {
                backgroundColor: '#020B4F'
            }
        },

        animation: {
            duration: 2000
        },

        scales: {

            y: {

                beginAtZero: true,

                title: {
                    display: true,
                    text: 'Quantidade de Alertas'
                }
            },

            x: {

                title: {
                    display: true,
                    text: 'Dias da Semana'
                }
            }
        }
    }
});