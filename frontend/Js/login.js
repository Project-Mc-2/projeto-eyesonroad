const API_URL = "http://localhost:8080/usuarios/login";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", async function(event) {
    event.preventDefault();
    erroMsg.textContent = "";

    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();

    if (!usuario || !senha) {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    // Monta o JSON que o seu Java espera receber no método de login
    const dadosLogin = {
        login: usuario, 
        senha: senha
    };

    try {
        // Fazendo a requisição POST para o Spring Boot
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dadosLogin)
        });

        // Se o Java retornar 200 OK (Senha bateu com a criptografia do banco)
        if (response.ok) {
            
            // Tenta pegar os dados reais do usuário que o Java devolver (opcional)
            let usuarioAutenticado = {};
            try {
                usuarioAutenticado = await response.json();
            } catch (e) {
                console.log("Login OK, mas não retornou JSON.");
            }

            alert("Login realizado com sucesso no Banco de Dados!");
            
            // Salvando os dados no LocalStorage APENAS para alimentar os gráficos do resumo.html
            localStorage.setItem("usuarioLogado", JSON.stringify({
                nome: usuarioAutenticado.nome || usuario, // Usa o nome real do banco se tiver
                alertas: 3,
                sonolencia: 2,
                tempo: "4h 25m",
                seguranca: "95%",
                eventos: [
                    "Login realizado com sucesso via Banco de Dados"
                ],
                grafico: [1, 2, 1, 3, 2, 1, 0]
            }));

            // Redireciona para o Dashboard
            window.location.href = "resumo.html";
            
        } else {
            // Se o Java retornar 401 ou 403 (Senha errada ou usuário não existe)
            erroMsg.textContent = "Usuário ou senha incorretos.";
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        erroMsg.textContent = "Não foi possível conectar ao servidor back-end.";
    }
});