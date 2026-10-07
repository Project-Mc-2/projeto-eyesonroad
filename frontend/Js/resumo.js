const nomeMotorista = document.getElementById("nomeMotorista");

const nomeSalvo = localStorage.getItem("nomeMotorista");

if (nomeSalvo) {
    nomeMotorista.textContent = nomeSalvo;
} else {
    nomeMotorista.textContent = "Motorista";
}


let dadosAlertas = [0, 0, 0, 0, 0, 0, 0];

let totalAlertas = 0;
let totalSonolencia = 0;
let tempoMonitorado = 0;


const cardAlertas = document.getElementById("totalAlertas");
const cardSonolencia = document.getElementById("totalSonolencia");
const cardTempo = document.getElementById("tempoMonitorado");
const cardSeguranca = document.getElementById("indiceSeguranca");

const listaEventos = document.getElementById("listaEventos");

const btnAtualizar = document.getElementById("btnAtualizar");


const canvas = document.getElementById("graficoSemanal");


const graficoSemanal = new Chart(canvas, {

    type: "line",

    data: {

        labels: [
            "Seg",
            "Ter",
            "Qua",
            "Qui",
            "Sex",
            "Sáb",
            "Dom"
        ],

        datasets: [

            {
                label: "Alertas",

                data: dadosAlertas,

                borderColor: "#4d8dff",

                backgroundColor: "rgba(77, 141, 255, 0.15)",

                borderWidth: 3,

                fill: true,

                tension: 0.4,

                pointRadius: 5,

                pointHoverRadius: 7,

                pointBackgroundColor: "#4d8dff",

                pointBorderColor: "#ffffff",

                pointBorderWidth: 2
            }

        ]

    },

    options: {

        responsive: true,

        maintainAspectRatio: false,

        animation: {
            duration: 500
        },

        scales: {

            y: {

                beginAtZero: true,

                ticks: {

                    color: "#aebbd0",

                    stepSize: 1

                },

                grid: {

                    color: "rgba(255,255,255,0.08)"

                }

            },

            x: {

                ticks: {

                    color: "#aebbd0"

                },

                grid: {

                    display: false

                }

            }

        },

        plugins: {

            legend: {

                labels: {

                    color: "#ffffff"

                }

            }

        }

    }

});


function atualizarCards() {

    cardAlertas.textContent = totalAlertas;

    cardSonolencia.textContent = totalSonolencia;

    cardTempo.textContent = tempoMonitorado + "h";


    let seguranca = 100 - (totalAlertas * 2);


    if (seguranca < 0) {
        seguranca = 0;
    }


    cardSeguranca.textContent = seguranca + "%";
}


function registrarAlerta() {

    totalAlertas++;


    const data = new Date();

    const diaSemana = data.getDay();

    let indice;


    if (diaSemana === 0) {
        indice = 6;
    } else {
        indice = diaSemana - 1;
    }


    dadosAlertas[indice]++;


    graficoSemanal.data.datasets[0].data = dadosAlertas;

    graficoSemanal.update();


    adicionarEvento(
        "Alerta de atenção detectado",
        data
    );


    atualizarCards();
}


function registrarSonolencia() {

    totalSonolencia++;

    totalAlertas++;


    const data = new Date();

    const diaSemana = data.getDay();

    let indice;


    if (diaSemana === 0) {
        indice = 6;
    } else {
        indice = diaSemana - 1;
    }


    dadosAlertas[indice]++;


    graficoSemanal.data.datasets[0].data = dadosAlertas;

    graficoSemanal.update();


    adicionarEvento(
        "Episódio de sonolência detectado",
        data
    );


    atualizarCards();
}


function adicionarEvento(mensagem, data) {

    const hora = data.toLocaleTimeString(
        "pt-BR",
        {
            hour: "2-digit",
            minute: "2-digit"
        }
    );


    const novoEvento = document.createElement("div");

    novoEvento.classList.add("evento");


    novoEvento.innerHTML = `

        <i class="fa-solid fa-triangle-exclamation"></i>

        <span>

            ${mensagem}

            <small>
                ${hora}
            </small>

        </span>

    `;


    if (
        listaEventos.children.length === 1 &&
        listaEventos.children[0].textContent.includes("Nenhum alerta")
    ) {

        listaEventos.innerHTML = "";

    }


    listaEventos.prepend(novoEvento);


    if (listaEventos.children.length > 5) {

        listaEventos.removeChild(
            listaEventos.lastElementChild
        );

    }

}


function atualizarTempo() {

    tempoMonitorado += 1;

    cardTempo.textContent = tempoMonitorado + "h";

}


btnAtualizar.addEventListener("click", function () {

    graficoSemanal.update();

    atualizarCards();

});


atualizarCards();