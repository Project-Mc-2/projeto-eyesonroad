// ===============================
// PEGAR USUÁRIO LOGADO
// ===============================
let dados = null;

try {
    dados = JSON.parse(localStorage.getItem("usuarioLogado"));
} catch (e) {
    dados = null;
}

// ===============================
// NOME DO MOTORISTA
// ===============================
const nomeMotorista = document.getElementById("nomeMotorista");

if (dados && dados.nome) {
    nomeMotorista.textContent = dados.nome;
} else {
    nomeMotorista.textContent = "Desconhecido";
}


// ===============================
// CARDS (VALORES DO USUÁRIO)
// ===============================
const cards = document.querySelectorAll(".card h1");

if (cards.length >= 4) {
    cards[0].textContent = dados?.alertas ?? 0;
    cards[1].textContent = dados?.sonolencia ?? 0;
    cards[2].textContent = dados?.tempo ?? "0h 00m";
    cards[3].textContent = dados?.seguranca ?? "0%";
}


// ===============================
// EVENTOS
// ===============================
const eventosContainer = document.querySelector(".eventos");

const eventos = dados?.eventos;

eventosContainer.innerHTML = "<h3>Eventos Recentes</h3>";

if (eventos && eventos.length > 0) {
    eventos.forEach(ev => {
        const div = document.createElement("div");
        div.classList.add("evento");
        div.textContent = ev;
        eventosContainer.appendChild(div);
    });
} else {
    const div = document.createElement("div");
    div.classList.add("evento");
    div.textContent = "Nenhum evento registrado";
    eventosContainer.appendChild(div);
}


// ===============================
// GRÁFICO (OPCIONAL)
// ===============================
const ctx = document.getElementById("graficoSemanal");

if (ctx) {
    new Chart(ctx, {
        type: "line",
        data: {
            labels: ["Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom"],
            datasets: [{
                label: "Alertas",
                data: dados?.grafico ?? [0, 0, 0, 0, 0, 0, 0],
                fill: false,
                tension: 0.3
            }]
        },
        options: {
            responsive: true
        }
    });
}