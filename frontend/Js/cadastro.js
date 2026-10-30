
const API_URL = "http://localhost:8080/usuarios";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");
const botao = form.querySelector('button[type="submit"]');

form.addEventListener("submit", async function(event) {
    event.preventDefault();

    erroMsg.textContent = "";
    erroMsg.style.color = "#ff8d96";

    const usuarioCompleto = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();
    const email = document.getElementById("email").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const telefone = document.getElementById("telefone").value.trim();
    const dataNascimento = document.getElementById("dataNascimento").value.trim();

    const placa = document.getElementById("placa").value
        .replace(/[^a-zA-Z0-9]/g, "")
        .toUpperCase();

    if (!usuarioCompleto || !senha || !email || !cpf || !telefone || !dataNascimento || !placa) {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    const cpfLimpo = cpf.replace(/\D/g, "");

    if (cpfLimpo.length !== 11) {
        erroMsg.textContent = "CPF inválido. Certifique-se de digitar os 11 dígitos.";
        return;
    }

    const usuarioCadastro = {
        nome: usuarioCompleto,
        login: email,
        senha: senha,
        email: email,
        telefone: telefone,
        dataNascimento: dataNascimento,
        cpf: cpfLimpo,
        tipo: "MOTORISTA",
        carro: {
            marca: "Não informada",
            modelo: "Não informado",
            placa: placa,
            ano: 2026
        }
    };

    botao.disabled = true;
    botao.textContent = "CADASTRANDO...";

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuarioCadastro)
        });

        if (response.ok) {
            erroMsg.style.color = "#1f9a52";
            erroMsg.textContent = "Cadastro realizado com sucesso!";

            form.reset();

            setTimeout(() => {
                window.location.href = "../pages/logarConta.html";
            }, 900);

            return;
        }

        const textoErro = await response.text();

        const matchDuplicado = textoErro.match(/Duplicate entry '([^']+)'/);

        if (matchDuplicado) {
            const valorDuplicado = matchDuplicado[1];

            if (valorDuplicado === cpfLimpo) {
                erroMsg.textContent = "Este CPF já está cadastrado.";
            } else if (valorDuplicado === email) {
                erroMsg.textContent = "Este e-mail já está cadastrado.";
            } else if (valorDuplicado === usuarioCompleto) {
                erroMsg.textContent = "Este nome de usuário já está cadastrado.";
            } else if (valorDuplicado === telefone) {
                erroMsg.textContent = "Este telefone já está cadastrado.";
            } else if (valorDuplicado === placa) {
                erroMsg.textContent = "Esta placa já está cadastrada.";
            } else {
                erroMsg.textContent = "Um dos dados informados já está cadastrado.";
            }

            return;
        }

        if (textoErro.includes("Já existe usuário com este login")) {
            erroMsg.textContent = "Este login já está cadastrado.";
            return;
        }

        erroMsg.textContent = "Erro ao realizar cadastro. Tente novamente.";

    } catch (error) {
        erroMsg.textContent = "Erro ao realizar cadastro. Tente novamente.";

    } finally {
        botao.disabled = false;
        botao.textContent = "CADASTRAR";
    }
});
