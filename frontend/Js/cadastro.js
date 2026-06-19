const API_URL = "http://localhost:8080/usuarios";

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", async function(event) {
    event.preventDefault();
    erroMsg.textContent = "";

    const usuarioCompleto = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();
    const email = document.getElementById("email").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const telefone = document.getElementById("telefone").value.trim();
    const dataNascimento = document.getElementById("dataNascimento").value.trim();
    
    const placa = document.getElementById("placa").value.replace(/[^a-zA-Z0-9]/g, "").toUpperCase();

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
        login: usuarioCompleto,      
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
            
        
            const matchDuplicado = textoErro.match(/Duplicate entry '([^']+)'/);

            if (matchDuplicado) {
                const valorDuplicado = matchDuplicado[1];

                if (valorDuplicado === cpfLimpo) {
                    erroMsg.textContent = "Este CPF já está cadastrado.";
                } else if (valorDuplicado === email) {
                    erroMsg.textContent = "Este E-mail já está cadastrado.";
                } else if (valorDuplicado === usuarioCompleto) {
                    erroMsg.textContent = "Este Nome de Usuário/Login já está cadastrado.";
                } else if (valorDuplicado === telefone) {
                    erroMsg.textContent = "Este Telefone já está cadastrado.";
                } else if (valorDuplicado === placa) {
                    erroMsg.textContent = "Esta Placa de veículo já está cadastrada.";
                } else {
                    erroMsg.textContent = `O dado '${valorDuplicado}' já está cadastrado no sistema.`;
                }
                return; 
            }

            if (textoErro.includes("Já existe usuário com este login")) {
                erroMsg.textContent = "Este Login já está cadastrado.";
                return;
            }

            try {
                const dadosErro = JSON.parse(textoErro);
                erroMsg.textContent = dadosErro.message || dadosErro.erro || dadosErro.mensagem || "Erro ao realizar cadastro.";
            } catch (e) {
                erroMsg.textContent = "Erro interno no servidor (500). Verifique os dados inseridos.";
            }
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        erroMsg.textContent = "Não foi possível conectar ao servidor.";
    }
});