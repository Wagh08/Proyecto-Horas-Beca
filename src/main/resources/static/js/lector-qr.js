
document.addEventListener("DOMContentLoaded", () => {

    const boton = document.getElementById("activar-camara");
    const video = document.getElementById("video-qr");
    const codigo = document.getElementById("codigo-qr");
    const estado = document.getElementById("estado-lector");

    if (!boton || !video || !codigo || !estado) {
        return;
    }

    let controles = null;
    let detectado = false;

    // ACTIVAR CAMARA
    boton.addEventListener("click", async () => {

        if (!window.isSecureContext ||
            !navigator.mediaDevices?.getUserMedia) {

            estado.textContent =
                "La cámara requiere HTTPS o localhost.";

            return;
        }

        if (!window.ZXingBrowser) {

            estado.textContent =
                "No se pudo cargar el lector QR. " +
                "Utiliza el código de respaldo.";

            return;
        }

        boton.disabled = true;
        detectado = false;

        estado.textContent = "Abriendo cámara...";

        try {

            const lector =
                new ZXingBrowser.BrowserQRCodeReader();

            controles = await lector.decodeFromVideoDevice(
                undefined,
                video,
                (resultado, error, controlActual) => {

                    if (!resultado || detectado) {
                        return;
                    }

                    detectado = true;

                    codigo.value =
                        resultado.getText().trim();

                    controlActual.stop();

                    estado.textContent =
                        "QR leído correctamente. " +
                        "Pulsa Validar código.";

                }
            );

        } catch (excepcion) {

            boton.disabled = false;

            estado.textContent =
                excepcion.name === "NotAllowedError"
                    ? "Permite el acceso a la cámara " +
                      "o utiliza el código de respaldo."
                    : "No se pudo abrir la cámara. " +
                      "Utiliza el código de respaldo.";
        }
    });

    // DETENER CAMARA AL SALIR
    window.addEventListener("pagehide", () => {

        if (controles) {
            controles.stop();
        }

    });
});
