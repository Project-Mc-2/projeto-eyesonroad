const API_URL = "http://localhost:8080/usuarios/login";

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const senhaInput = document.getElementById('senha');
    const erroMsg = document.getElementById('erroMsg');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const emailDigitado = emailInput.value.trim();
        const senhaDigitada = senhaInput.value.trim();

        erroMsg.textContent = '';
        erroMsg.style.color = '#ff4d4d';

        if (!emailDigitado || !senhaDigitada) {
            erroMsg.textContent = 'Por favor, preencha todos os campos.';
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

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(dadosLogin)
            });

            if (response.ok) {
                try {
                    const dadosUsuario = await response.json();
                    localStorage.setItem('usuarioLogado', JSON.stringify(dadosUsuario));
                } catch (e) {
                    console.log("Resposta não continha JSON.");
                }

                erroMsg.style.color = '#2ecc71'; 
                erroMsg.textContent = 'Login efetuado com sucesso! Redirecionando...';

                setTimeout(() => {
                    window.location.href = '../pages/resumo.html'; 
                }, 1500);

            } else {
                erroMsg.textContent = 'Usuário ou senha incorretos.';
                senhaInput.value = '';
                senhaInput.focus();
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            erroMsg.textContent = "Não foi possível conectar ao servidor.";
        }
    });
});