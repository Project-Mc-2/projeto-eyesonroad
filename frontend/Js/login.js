function logar() {
    let cpf = document.getElementById("cpf").value;
    let placa = document.getElementById("placa").value;
    let senha = document.getElementById("senha").value;

    // validação
    if (cpf === "" || placa === "" || senha === "") {
        alert("Preencha todos os campos!");
        return;
    }

    alert("Login realizado com sucesso!");

    // COLOQUE A PRÓXIMA PÁGINA AQUI
    window.location.href = "home.html";
}

function voltar() {
    window.location.href = "cadastro.html";
}