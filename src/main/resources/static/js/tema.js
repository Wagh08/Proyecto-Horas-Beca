const claveTema = "horas-beca-tema";

function aplicarTema(tema) {
    const temaElegido = tema === "dark" ? "dark" : "light";
    document.documentElement.dataset.theme = temaElegido;

    const boton = document.getElementById("boton-tema");

    if (boton) {
        boton.textContent = temaElegido === "dark" ? "Usar tema claro" : "Usar tema oscuro";
        boton.setAttribute("aria-pressed", String(temaElegido === "dark"));
    }
}

let temaGuardado = "light";

try {
    temaGuardado = localStorage.getItem(claveTema) || "light";
} catch (error) {
    temaGuardado = "light";
}

aplicarTema(temaGuardado);

document.addEventListener("DOMContentLoaded", () => {
    const boton = document.getElementById("boton-tema");

    if (!boton) {
        return;
    }

    aplicarTema(document.documentElement.dataset.theme);

    boton.addEventListener("click", () => {
        const nuevoTema = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
        aplicarTema(nuevoTema);

        try {
            localStorage.setItem(claveTema, nuevoTema);
        } catch (error) {
            aplicarTema(nuevoTema);
        }
    });
});