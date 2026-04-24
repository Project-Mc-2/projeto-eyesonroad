
function irLogin() {
    window.location.href = "login.html"; 
}

function cadastrar() {
    let nome = document.getElementById("nome").value;
    let email = document.getElementById("email").value;
    let cpf = document.getElementById("cpfCadastro").value;
    let placa = document.getElementById("placaCadastro").value;
    let senha = document.getElementById("senhaCadastro").value;

    if (!nome || !email || !cpf || !placa || !senha) {
        alert("Preencha todos os campos!");
        return false;
    }

    alert("Conta cadastrada com sucesso!");

    return false;
}