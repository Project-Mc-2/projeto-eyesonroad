
const API_URL = "http://localhost:8080/usuarios/login";

document.addEventListener("DOMContentLoaded", () => {

    const loginForm = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const senhaInput = document.getElementById("senha");
    const erroMsg = document.getElementById("erroMsg");
    const botao = loginForm.querySelector('button[type="submit"]');

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const emailDigitado = emailInput.value.trim();
        const senhaDigitada = senhaInput.value.trim();

        erroMsg.textContent = "";
        erroMsg.style.color = "#ff8d96";

        if (!emailDigitado || !senhaDigitada) {
            erroMsg.textContent = "Preencha todos os campos.";
            return;
        }

        const dadosLogin = {
            nome: "Usuario Temporario",
            login: emailDigitado,
            senha: senhaDigitada,
            email: emailDigitado,
            telefone: "11999999999",
            dataNascimento: "2000-01-01",
            cpf: "00000000000",
            tipo: "MOTORISTA",
            carro: {
                marca: "N/A",
                modelo: "N/A",
                placa: "XXX0000",
                ano: 2026
            }
        };

        botao.disabled = true;
        botao.textContent = "ENTRANDO...";

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(dadosLogin)
            });

            if (response.ok) {

                const textoResposta = await response.text();

                let dadosUsuario = {};

                try {
                    dadosUsuario = textoResposta ? JSON.parse(textoResposta) : {};
                } catch (e) {
                    dadosUsuario = {};
                }

                localStorage.setItem(
                    "usuarioLogado",
                    JSON.stringify(dadosUsuario)
                );

                const nomeMotorista = dadosUsuario.nome || emailDigitado;

                localStorage.setItem("nomeMotorista", nomeMotorista);

                window.location.href = "../pages/resumo.html";

                return;
            }

            const textoErro = await response.text();

            if (
                response.status === 401 ||
                response.status === 403 ||
                /usuário ou senha incorretos|usuario ou senha incorretos|senha inválida|senha invalida|login inválido|login invalido/i.test(textoErro)
            ) {
                erroMsg.textContent = "E-mail ou senha incorretos.";
                senhaInput.value = "";
                senhaInput.focus();

            } else {
                erroMsg.textContent = "Erro ao fazer login. Tente novamente.";
            }

        } catch (error) {
            erroMsg.textContent = "Erro ao fazer login. Tente novamente.";

        } finally {
            botao.disabled = false;
            botao.textContent = "ENTRAR";
        }
    });
});
