const API_URL = "http://localhost:8080/usuarios/login";

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
        
         localStorage.setItem("usuarioLogado", JSON.stringify({
            nome: usuarioSalvo.usuario,
            alertas: 3,
            sonolencia: 2,
            tempo: "4h 25m",
            seguranca: "95%",
            eventos: [
                "Login realizado com sucesso"
            ],
            grafico: [1, 2, 1, 3, 2, 1, 0]
        }));

        window.location.href = "resumo.html";
    } else {
        erroMsg.textContent = "Usuário ou senha incorretos.";
    }
});