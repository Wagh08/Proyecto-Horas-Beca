const campoUbicacion = document.querySelector("#filtro-ubicacion");
const listaUbicaciones = document.querySelector("#sugerencias-ubicacion");

if (campoUbicacion && listaUbicaciones) {
    const contenedorUbicacion = campoUbicacion.closest(".campo-ubicacion");
    let solicitudActual;
    let temporizador;

    function ocultarSugerencias() {
        listaUbicaciones.hidden = true;
        listaUbicaciones.replaceChildren();
        contenedorUbicacion.classList.remove("tiene-sugerencias");
        campoUbicacion.setAttribute("aria-expanded", "false");
    }

    function mostrarSugerencias(ubicaciones) {
        ocultarSugerencias();

        ubicaciones.forEach(ubicacion => {
            const opcion = document.createElement("button");
            opcion.type = "button";
            opcion.className = "sugerencia-ubicacion";
            opcion.setAttribute("role", "option");
            opcion.textContent = ubicacion;

            opcion.addEventListener("click", event => {
                campoUbicacion.value = ubicacion;
                ocultarSugerencias();

                if (event.detail === 0) {
                    campoUbicacion.focus();
                }
            });

            listaUbicaciones.append(opcion);
        });

        if (ubicaciones.length > 0) {
            listaUbicaciones.hidden = false;
            contenedorUbicacion.classList.add("tiene-sugerencias");
            campoUbicacion.setAttribute("aria-expanded", "true");
        }
    }

    campoUbicacion.addEventListener("input", () => {
        clearTimeout(temporizador);
        solicitudActual?.abort();
        ocultarSugerencias();

        const texto = campoUbicacion.value.trim();

        if (texto.length < 2) {
            return;
        }

        temporizador = setTimeout(async () => {
            const controlador = new AbortController();
            solicitudActual = controlador;

            try {
                const respuesta = await fetch(`/api/ubicaciones?q=${encodeURIComponent(texto)}`, { signal: controlador.signal });

                if (!respuesta.ok) {
                    return;
                }

                const ubicaciones = await respuesta.json();

                if (campoUbicacion.value.trim() === texto && solicitudActual === controlador) {
                    mostrarSugerencias(ubicaciones);
                }
            } catch (error) {
                if (error.name !== "AbortError") {
                    ocultarSugerencias();
                }
            }
        }, 200);
    });

    campoUbicacion.addEventListener("keydown", event => {
        if (event.key === "Escape") {
            ocultarSugerencias();
        }

        if (event.key === "ArrowDown" && !listaUbicaciones.hidden) {
            event.preventDefault();
            listaUbicaciones.querySelector("button")?.focus();
        }
    });

    listaUbicaciones.addEventListener("keydown", event => {
        const opciones = [...listaUbicaciones.querySelectorAll("button")];
        const indice = opciones.indexOf(document.activeElement);

        if (event.key === "Escape") {
            ocultarSugerencias();
            campoUbicacion.focus();
        } else if (event.key === "ArrowDown") {
            event.preventDefault();
            opciones[Math.min(indice + 1, opciones.length - 1)]?.focus();
        } else if (event.key === "ArrowUp") {
            event.preventDefault();

            if (indice <= 0) {
                campoUbicacion.focus();
            } else {
                opciones[indice - 1].focus();
            }
        }
    });

    document.addEventListener("pointerdown", event => {
        if (!contenedorUbicacion.contains(event.target)) {
            ocultarSugerencias();
        }
    });
}