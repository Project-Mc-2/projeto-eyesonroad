const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", function(event) {
    event.preventDefault();

    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();

    erroMsg.textContent = "";

    const usuarioSalvo = JSON.parse(localStorage.getItem("usuarioCadastro"));

    if (!usuarioSalvo) {
        erroMsg.textContent = "Nenhum usuário cadastrado.";
        return;
    }

    if (
        usuario === usuarioSalvo.usuario &&
        senha === usuarioSalvo.senha
    ) {
        alert("Login realizado com sucesso!");
        window.location.href = "resumo.html";
    } else {
        erroMsg.textContent = "Usuário ou senha incorretos.";
    }
});