const iconos = {
    inicio: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="m3 10 9-7 9 7v10a1 1 0 0 1-1 1h-5v-7H9v7H4a1 1 0 0 1-1-1z"/></svg>',
    actividades: '<svg viewBox="0 0 24 24" aria-hidden="true"><rect x="3" y="5" width="18" height="16" rx="2"/><path d="M7 3v4M17 3v4M3 10h18M8 14h3M8 17h3"/></svg>',
    progreso: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 20V12M10 20V8M16 20V4M22 20H2"/></svg>',
    gestion: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 5h12M4 11h12M4 17h8M19 14v7M15.5 17.5h7"/></svg>',
    perfil: '<svg viewBox="0 0 24 24" aria-hidden="true"><circle cx="12" cy="8" r="4"/><path d="M4 21a8 8 0 0 1 16 0"/></svg>'
};

const paginas = {
    estudiante: [
        { id: "inicio", nombre: "Inicio", archivo: "inicio-estudiante.html" },
        { id: "actividades", nombre: "Actividades", archivo: "actividades.html" },
        { id: "progreso", nombre: "Progreso", archivo: "progreso.html" },
        { id: "perfil", nombre: "Perfil", archivo: "perfil-estudiante.html" }
    ],
    organizador: [
        { id: "inicio", nombre: "Inicio", archivo: "inicio-organizador.html" },
        { id: "gestion", nombre: "Gestión", archivo: "gestion.html" },
        { id: "perfil", nombre: "Perfil", archivo: "perfil-organizador.html" }
    ]
};

const rol = document.body.dataset.rol;
const paginaActual = document.body.dataset.pagina;
const secciones = paginas[rol];
const seccionActual = secciones.find(seccion => seccion.id === paginaActual);

document.getElementById("titulo-pagina").textContent = seccionActual.nombre;
document.title = `${seccionActual.nombre} | Horas Beca`;

document.querySelector(".navegacion-inferior").innerHTML = secciones.map(seccion => `
    <a
        href="${seccion.archivo}"
        ${seccion.id === paginaActual ? 'aria-current="page"' : ""}
    >
        ${iconos[seccion.id]}
        <span>${seccion.nombre}</span>
    </a>
`).join("");