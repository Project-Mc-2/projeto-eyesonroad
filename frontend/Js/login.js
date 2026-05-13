
const form = document.getElementById("loginForm");
const usuarioInput = document.getElementById("usuario");
const senhaInput = document.getElementById("senha");
const erroMsg = document.getElementById("erroMsg");


form.addEventListener("submit", function(event) {

    
    event.preventDefault();

    
    const usuario = usuarioInput.value.trim();
    const senha = senhaInput.value.trim();

    erroMsg.textContent = "";

   
    if (usuario === "" || senha === "") {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    
    const usuarioCorreto = "admin";
    const senhaCorreta = "1234";

 
    if (usuario === usuarioCorreto && senha === senhaCorreta) {

       
        alert("Login realizado com sucesso!");

        
        window.location.href = "/frontend/pages/resumo.html";

    } else {

        erroMsg.textContent = "Usuário ou senha incorretos.";

    }

});