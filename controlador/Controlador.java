package controlador;

import java.util.ArrayList;
import modelo.Actividad;
import modelo.Estudiante;
import modelo.Organizador;
import modelo.Sistema;
import modelo.Usuario;
import vista.Vista;

import java.util.ArrayList;


public class Controlador {

    private final Sistema sistema;
    private Vista vista;
    private Usuario usuarioActual;

    public Controlador() {
        this(new Sistema());
    }

    public Controlador(Sistema sistema) {
        this.sistema = sistema;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void registrarUsuario(Usuario usuario) {
        sistema.registrarUsuario(usuario);
    }

    public boolean iniciarSesion(String correo, String contrasena) {
        boolean sesionIniciada = sistema.iniciarSesion(correo, contrasena);

        if (sesionIniciada) {
            usuarioActual = sistema.getUsuarioActivo();
        }

        return sesionIniciada;
    }

    public void cerrarSesion() {
        sistema.cerrarSesion();
        usuarioActual = null;
    }

    public ArrayList<Actividad> consultarActividades() {
        return sistema.obtenerActividadesDisponibles();
    }

    public boolean aplicarActividad(Actividad actividad) {
        if (!(usuarioActual instanceof Estudiante)) {
            return false;
        }

        return sistema.aplicarEstudiante((Estudiante) usuarioActual, actividad);
    }

    public Actividad crearActividad(
            String nombre,
            String descripcion,
            String fecha,
            String horaInicio,
            String horaFin,
            String ubicacion,
            int cantidadHoras,
            int cupoMaximo,
            boolean horasDobles) {

        if (!(usuarioActual instanceof Organizador)) {
            return null;
        }

        Organizador organizador = (Organizador) usuarioActual;
        Actividad actividad = organizador.crearActividad(
                nombre,
                descripcion,
                fecha,
                horaInicio,
                horaFin,
                cantidadHoras,
                horasDobles);

        actividad.setUbicacion(ubicacion);
        actividad.setCupoMaximo(cupoMaximo);
        actividad.setCuposRestantes(cupoMaximo);
        actividad.setEstado("Abierta");
        sistema.agregarActividad(actividad);

        return actividad;
    }

    public boolean registrarAsistencia(String codigoQR, Actividad actividad) {
        if (!(usuarioActual instanceof Organizador)) {
            return false;
        }

        return sistema.registrarAsistenciaQR(codigoQR, actividad);
    }

    public double consultarProgreso() {
        if (!(usuarioActual instanceof Estudiante)) {
            return 0.0;
        }

        return sistema.consultarProgresoEstudiante((Estudiante) usuarioActual);
    }

    public Actividad[] consultarHistorial() {
        if (!(usuarioActual instanceof Estudiante)) {
            return new Actividad[0];
        }

        return ((Estudiante) usuarioActual).getHistorialActividades();
    }

    public String consultarInformacionDirectorCarrera() {
        return sistema.obtenerInformacionDirectorCarrera();
    }

    public String consultarInformacionAyudasFinancieras() {
        return sistema.obtenerInformacionAyudasFinancieras();
    }

    public String generarCodigoQR() {
        if (!(usuarioActual instanceof Estudiante)) {
            return "";
        }

        return ((Estudiante) usuarioActual).generarQR();
    }

    public boolean escanearCodigoQR(String codigoQR, Actividad actividad) {
        return registrarAsistencia(codigoQR, actividad);
    }
}
