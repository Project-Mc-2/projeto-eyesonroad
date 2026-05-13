// login.js

// Pegando os elementos do HTML
const form = document.getElementById("loginForm");
const usuarioInput = document.getElementById("usuario");
const senhaInput = document.getElementById("senha");
const erroMsg = document.getElementById("erroMsg");

// Evento ao enviar o formulário
form.addEventListener("submit", function(event) {

    // Impede o recarregamento da página
    event.preventDefault();

    // Pegando os valores digitados
    const usuario = usuarioInput.value.trim();
    const senha = senhaInput.value.trim();

    // Limpando mensagem de erro anterior
    erroMsg.textContent = "";

    // Verifica se os campos estão vazios
    if (usuario === "" || senha === "") {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    // Exemplo de login fixo
    const usuarioCorreto = "admin";
    const senhaCorreta = "1234";

    // Verificação do login
    if (usuario === usuarioCorreto && senha === senhaCorreta) {

        // Mensagem de sucesso
        alert("Login realizado com sucesso!");

        // Redirecionamento
        // Troque pelo link da sua página
        window.location.href = "/frontend/pages/home.html";

    } else {

        // Mensagem de erro
        erroMsg.textContent = "Usuário ou senha incorretos.";

    }

});