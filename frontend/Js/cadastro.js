

const form = document.getElementById("loginForm");
const erroMsg = document.getElementById("erroMsg");

form.addEventListener("submit", function(event) {
    event.preventDefault();

   
    const usuario = document.getElementById("usuario").value.trim();
    const senha = document.getElementById("senha").value.trim();
    const email = document.getElementById("email").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const placa = document.getElementById("placa").value.trim();

    
    erroMsg.textContent = "";

    
    if (
        usuario === "" ||
        senha === "" ||
        email === "" ||
        cpf === "" ||
        placa === ""
    ) {
        erroMsg.textContent = "Preencha todos os campos.";
        return;
    }

    
    if (!email.includes("@") || !email.includes(".")) {
    erroMsg.textContent = "Digite um email válido.";
    return;
}

    if (!emailValido.test(email)) {
        erroMsg.textContent = "Digite um email válido.";
        return;
    }

    
    let cpfLimpo = cpf;
    cpfLimpo = cpfLimpo.replaceAll(".", "");
    cpfLimpo = cpfLimpo.replaceAll("-", "");

    if (cpfLimpo.length !== 11) {
        erroMsg.textContent = "CPF inválido.";
        return;
    }


    if (placa.length < 7) {
    erroMsg.textContent = "Placa inválida.";
    return;
}

    if (!placaValida.test(placa)) {
        erroMsg.textContent = "Placa inválida.";
        return;
    }

   
    alert("Cadastro realizado com sucesso!");

});