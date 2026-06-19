const API_URL = "http://localhost:8080/usuarios/cadastro";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", async function(event) {
    event.preventDefault();

    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();
    const email = document.getElementById("email").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const placa = document.getElementById("placa").value.trim();

    erroMsg.textContent = "";

    // validações frontend (mantidas)
    if (!usuario || !senha || !email || !cpf || !placa) {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    if (!email.includes("@") || !email.includes(".")) {
        erroMsg.textContent = "Digite um e-mail válido.";
        return;
    }

    const cpfLimpo = cpf.replace(/\D/g, "");

    if (cpfLimpo.length !== 11) {
        erroMsg.textContent = "CPF inválido.";
        return;
    }

    if (placa.length < 7) {
        erroMsg.textContent = "Placa inválida.";
        return;
    }

    const usuarioCadastro = {
        usuario,
        senha,
        email,
        cpf: cpfLimpo,
        placa
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuarioCadastro)
        });

        if (!response.ok) {
            const erro = await response.text();
            erroMsg.textContent = erro || "Erro ao cadastrar usuário.";
            return;
        }

        const resultado = await response.json();

        alert("Cadastro realizado com sucesso!");

        // salva retorno do backend (opcional)
        localStorage.setItem("usuarioCadastro", JSON.stringify(resultado));

        window.location.href = "../pages/logarConta.html";

    } catch (error) {
        console.error("Erro ao conectar com backend:", error);
        erroMsg.textContent = "Erro de conexão com o servidor.";
    }
});