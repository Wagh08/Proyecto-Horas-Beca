const formulario = document.getElementById("formularioLogin");
const mensaje = document.getElementById("mensaje");

formulario.addEventListener("submit", function(evento) {

    evento.preventDefault();

    const correo = document.getElementById("correo").value;
    const contraseña = document.getElementById("contraseña").value;

    if (correo === "" || contraseña === "") {
        mensaje.textContent = "Complete todos los campos.";
        mensaje.style.color = "red";
    } else {
        mensaje.textContent =
            "Datos ingresados correctamente.";

        mensaje.style.color = "green";
    }
});