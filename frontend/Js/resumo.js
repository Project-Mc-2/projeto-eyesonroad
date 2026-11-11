
const API_URL = "http://localhost:8080/api/alertas";

const nomeMotoristaElemento = document.getElementById("nomeMotorista");
const totalAlertasElemento = document.getElementById("totalAlertas");
const totalSonolenciaElemento = document.getElementById("totalSonolencia");
const indiceSegurancaElemento = document.getElementById("indiceSeguranca");

const ultimoAlertaElemento = document.getElementById("ultimoAlerta");
const horarioUltimoAlertaElemento = document.getElementById("horarioUltimoAlerta");
const estadoAtencaoElemento = document.getElementById("estadoAtencao");
const situacaoCardElemento = document.getElementById("situacaoCard");
const indicadorCameraElemento = document.getElementById("indicadorCamera");

let totalAlertas = 0;
let totalSonolencia = 0;
let idsAlertasRecebidos = new Set();

/* IDENTIFICAR O MOTORISTA LOGADO */

function carregarNomeMotorista() {
    const usuarioSalvo = localStorage.getItem("usuarioLogado");

    if (!usuarioSalvo) {
        nomeMotoristaElemento.textContent = "Não identificado";
        return;
    }

    try {
        const usuario = JSON.parse(usuarioSalvo);

        if (typeof usuario === "object" && usuario !== null) {
            nomeMotoristaElemento.textContent =
                usuario.nome ||
                usuario.nomeMotorista ||
                usuario.usuario ||
                usuario.login ||
                "Não identificado";
        } else {
            nomeMotoristaElemento.textContent = usuario;
        }

    } catch (erro) {
        nomeMotoristaElemento.textContent = usuarioSalvo;
    }
}

/* FORMATAR HORÁRIO */

function formatarHorario(data) {
    const dataConvertida = new Date(data);

    if (isNaN(dataConvertida.getTime())) {
        return "--:--";
    }

    return dataConvertida.toLocaleTimeString("pt-BR", {
        hour: "2-digit",
        minute: "2-digit"
    });
}

/* ATUALIZAR ÍNDICE DE SEGURANÇA */

function atualizarIndiceSeguranca() {
    const indice = Math.max(0, 100 - totalAlertas * 2);

    indiceSegurancaElemento.textContent = indice + "%";
}

/* ATUALIZAR CONTADORES */

function atualizarContadores() {
    totalAlertasElemento.textContent = totalAlertas;
    totalSonolenciaElemento.textContent = totalSonolencia;

    atualizarIndiceSeguranca();
}

/* ATUALIZAR SITUAÇÃO DO MOTORISTA */

function atualizarSituacao(sonolencia) {
    if (sonolencia) {
        estadoAtencaoElemento.textContent = "Sonolência detectada";

        situacaoCardElemento.innerHTML = `
            <i class="fa-solid fa-triangle-exclamation"></i>
            <div>
                <strong>Atenção: sonolência detectada</strong>
                <p>O sistema identificou sinais de sonolência no motorista.</p>
            </div>
        `;

        situacaoCardElemento.style.background =
            "rgba(255, 170, 60, 0.08)";

        situacaoCardElemento.style.borderColor =
            "rgba(255, 170, 60, 0.3)";

        situacaoCardElemento.querySelector("i").style.color =
            "#ffb454";

    } else {
        estadoAtencaoElemento.textContent = "Atenção necessária";

        situacaoCardElemento.innerHTML = `
            <i class="fa-solid fa-shield-heart"></i>
            <div>
                <strong>Alerta registrado</strong>
                <p>O sistema registrou uma ocorrência durante o monitoramento.</p>
            </div>
        `;

        situacaoCardElemento.style.background =
            "rgba(104, 165, 255, 0.08)";

        situacaoCardElemento.style.borderColor =
            "rgba(104, 165, 255, 0.2)";

        situacaoCardElemento.querySelector("i").style.color =
            "#68a5ff";
    }
}

/* ATUALIZAR ÚLTIMO ALERTA */

function atualizarUltimoAlerta(alerta) {
    const tipo = String(alerta.tipo || "").toLowerCase();

    const mensagem =
        alerta.mensagem || "Alerta identificado";

    const data =
        alerta.data || alerta.timestamp || new Date().toISOString();

    ultimoAlertaElemento.textContent = mensagem;

    horarioUltimoAlertaElemento.textContent =
        formatarHorario(data);

    atualizarSituacao(
        tipo.includes("sonolencia") ||
        tipo.includes("sonolência")
    );
}

/* REGISTRAR ALERTA RECEBIDO */

function registrarAlerta(alerta) {
    const identificador =
        alerta.id ??
        `${alerta.tipo}-${alerta.data || alerta.timestamp}-${alerta.mensagem}`;

    if (idsAlertasRecebidos.has(identificador)) {
        return;
    }

    idsAlertasRecebidos.add(identificador);

    totalAlertas++;

    const tipo = String(alerta.tipo || "").toLowerCase();

    if (
        tipo.includes("sonolencia") ||
        tipo.includes("sonolência")
    ) {
        totalSonolencia++;
    }

    atualizarContadores();
}

/* PROCESSAR RESPOSTA DA API */

function processarAlertas(dados) {
    let alertas = [];

    if (Array.isArray(dados)) {
        alertas = dados;

    } else if (dados && Array.isArray(dados.alertas)) {
        alertas = dados.alertas;
    }

    if (
        dados &&
        !Array.isArray(dados) &&
        !Array.isArray(dados.alertas)
    ) {
        totalAlertas = Number(dados.totalAlertas) || 0;
        totalSonolencia = Number(dados.totalSonolencia) || 0;

        atualizarContadores();
    }

    alertas.forEach(alerta => {
        registrarAlerta(alerta);
    });

    if (alertas.length > 0) {
        const ultimo = alertas[alertas.length - 1];

        atualizarUltimoAlerta(ultimo);
    }
}

/* INDICADOR DE CONEXÃO COM A API */

function atualizarIndicadorAPI(conectado) {
    if (conectado) {
        indicadorCameraElemento.innerHTML = `
            <i class="fa-solid fa-circle"></i>
            <span>API conectada</span>
        `;

        indicadorCameraElemento.style.borderColor =
            "rgba(80, 200, 120, 0.35)";

        indicadorCameraElemento.querySelector("i").style.color =
            "#50c878";

    } else {
        indicadorCameraElemento.innerHTML = `
            <i class="fa-solid fa-circle"></i>
            <span>API indisponível</span>
        `;

        indicadorCameraElemento.style.borderColor =
            "rgba(255, 100, 100, 0.35)";

        indicadorCameraElemento.querySelector("i").style.color =
            "#ff6464";
    }
}

/* BUSCAR ALERTAS NO BACKEND */

async function buscarAlertas() {
    try {
        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error("Não foi possível buscar os alertas.");
        }

        const dados = await resposta.json();

        atualizarIndicadorAPI(true);
        processarAlertas(dados);

    } catch (erro) {
        console.error("Erro ao conectar com a API:", erro);

        atualizarIndicadorAPI(false);
    }
}

/* INICIALIZAÇÃO */

carregarNomeMotorista();
atualizarContadores();
buscarAlertas();

setInterval(buscarAlertas, 3000);
