const API_URL = "http://localhost:8080/usuarios/login";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", async function(event) {
    event.preventDefault();
    erroMsg.textContent = "";

    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value.trim();

    if (!email || !senha) {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    const dadosLogin = {
        login: email, 
        senha: senha
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosLogin)
        });

        if (response.ok) {
            
            let usuarioAutenticado = {};
            try {
                usuarioAutenticado = await response.json();
            } catch (e) {
                console.log("Login OK, mas não retornou JSON.");
            }

            alert("Login realizado com sucesso no Banco de Dados!");
            
            localStorage.setItem("usuarioLogado", JSON.stringify({
                nome: usuarioAutenticado.nome || usuario, 
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
            erroMsg.textContent = "Email ou senha incorretos.";
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        erroMsg.textContent = "Não foi possível conectar ao servidor back-end.";
    }
});