const API_URL = "http://localhost:8080/usuarios/login";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", async function(event) {
    event.preventDefault();

    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();

    erroMsg.textContent = "";

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                usuario: usuario,
                senha: senha
            })
        });

        // se login falhar
        if (!response.ok) {
            const erroTexto = await response.text();
            erroMsg.textContent = erroTexto || "Usuário ou senha incorretos.";
            return;
        }

        // se login der certo, backend deve retornar usuário
        const usuarioLogado = await response.json();

        alert("Login realizado com sucesso!");

        // salva usuário vindo do BACKEND (não mais localStorage de cadastro)
        localStorage.setItem("usuarioLogado", JSON.stringify({
            nome: usuarioLogado.usuario || usuario,
            alertas: usuarioLogado.alertas ?? 3,
            sonolencia: usuarioLogado.sonolencia ?? 2,
            tempo: usuarioLogado.tempo ?? "4h 25m",
            seguranca: usuarioLogado.seguranca ?? "95%",
            eventos: usuarioLogado.eventos ?? ["Login realizado com sucesso"],
            grafico: usuarioLogado.grafico ?? [1, 2, 1, 3, 2, 1, 0]
        }));

        window.location.href = "resumo.html";

    } catch (error) {
        console.error("Erro ao conectar com o backend:", error);
        erroMsg.textContent = "Erro de conexão com o servidor.";
    }
});