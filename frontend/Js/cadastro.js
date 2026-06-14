const API_URL = "http://localhost:8080/usuarios";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", async function(event) {
    event.preventDefault();
    erroMsg.textContent = "";

    // 1. Capturando os 5 campos reais que existem na sua tela HTML
    const usuarioCompleto = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();
    const email = document.getElementById("email").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const placa = document.getElementById("placa").value.trim().toUpperCase();

    // 2. Validação simples dos campos visíveis no front-end
    if (!usuarioCompleto || !senha || !email || !cpf || !placa) {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    const cpfLimpo = cpf.replace(/\D/g, "");
    if (cpfLimpo.length !== 11) {
        erroMsg.textContent = "CPF inválido. Certifique-se de digitar os 11 dígitos.";
        return;
    }

    // 3. Montando o Objeto casado exatamente com a sua NOVA classe Usuario.java
    const usuarioCadastro = {
        nome: usuarioCompleto,       // Repassa o valor para o campo obrigatório 'nome'
        login: usuarioCompleto,      // Repassa o valor para o campo obrigatório 'login'
        senha: senha,                // Aceita qualquer senha de 5 a 20 caracteres
        email: email,
        telefone: "15999999999",     // String numérica padrão para o campo 'telefone'
        dataNascimento: "2000-01-01",// Formato yyyy-MM-dd mapeado pelo @JsonFormat do Java
        cpf: cpfLimpo,
        tipo: "MOTORISTA",           // Valor do seu Enum TipoUsuario
        carro: {                     // Entidade Carro vinculada via @OneToOne no back
            marca: "Não informada",
            modelo: "Não informado",
            placa: placa,
            ano: 2026                
        }
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuarioCadastro)
        });

        if (response.ok) {
            alert("Cadastro realizado com sucesso!");
            window.location.href = "../pages/logarConta.html";
        } else {
            const textoErro = await response.text();
            console.error("Resposta detalhada do servidor:", textoErro);
            
            try {
                const dadosErro = JSON.parse(textoErro);
                if (dadosErro.errors && dadosErro.errors.length > 0) {
                    erroMsg.textContent = dadosErro.errors[0].defaultMessage;
                } else {
                    erroMsg.textContent = dadosErro.mensagem || dadosErro.message || "Erro de validação no servidor.";
                }
            } catch (e) {
                erroMsg.textContent = "Erro no servidor: " + textoErro.substring(0, 60);
            }
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        erroMsg.textContent = "Não foi possível conectar ao servidor back-end.";
    }
});