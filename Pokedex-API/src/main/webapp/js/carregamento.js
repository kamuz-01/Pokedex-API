// Exibe o SweetAlert de carregamento assim que a página começa a carregar
window.onload = function() {
    Swal.fire({
        text: 'Por favor, aguarde enquanto carregamos as informações.',
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });

    setTimeout(function() {
        Swal.close(); // Fecha o SweetAlert após o tempo
    }, 2000);  // Tempo em milissegundos (2 segundos)
};