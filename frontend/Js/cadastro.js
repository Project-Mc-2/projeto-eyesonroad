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
    const placa = document.getElementById("placa").value.trim().toUpperCase();

<<<<<<< HEAD
    erroMsg.textContent = "";

    // validações frontend (mantidas)
    if (!usuario || !senha || !email || !cpf || !placa) {
=======
    if (!usuarioCompleto || !senha || !email || !cpf || !telefone || !dataNascimento || !placa) {
>>>>>>> 1af1fde1cef3e4293c5c8c9d49e4337141f5545d
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    const cpfLimpo = cpf.replace(/\D/g, "");
    if (cpfLimpo.length !== 11) {
        erroMsg.textContent = "CPF inválido. Certifique-se de digitar os 11 dígitos.";
        return;
    }

    const usuarioCadastro = {
<<<<<<< HEAD
        usuario,
        senha,
        email,
        cpf: cpfLimpo,
        placa
=======
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
>>>>>>> 1af1fde1cef3e4293c5c8c9d49e4337141f5545d
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuarioCadastro)
        });

<<<<<<< HEAD
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
=======
        if (response.ok) {
            alert("Cadastro realizado com sucesso!");
            window.location.href = "../pages/logarConta.html";
        } else {
            const textoErro = await response.text();
            try {
                const dadosErro = JSON.parse(textoErro);
                erroMsg.textContent = dadosErro.mensagem || "Erro ao realizar cadastro.";
            } catch (e) {
                erroMsg.textContent = "Erro no servidor.";
            }
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        erroMsg.textContent = "Não foi possível conectar ao servidor.";
    }
});
>>>>>>> 1af1fde1cef3e4293c5c8c9d49e4337141f5545d
