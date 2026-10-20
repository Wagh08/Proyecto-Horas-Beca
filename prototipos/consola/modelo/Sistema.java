package modelo;

import java.util.ArrayList;

public class Sistema {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Actividad> actividades;
    private ArrayList<String> asistencias;
    private Usuario usuarioActivo;
    private String nombreDirectorCarrera;
    private String correoDirectorCarrera;
    private String telefonoDirectorCarrera;
    private String ubicacionDirectorCarrera;
    private String correoAyudasFinancieras;
    private String telefonoAyudasFinancieras;
    private String ubicacionAyudasFinancieras;

    public Sistema() {
        usuarios = new ArrayList<Usuario>();
        actividades = new ArrayList<Actividad>();
        asistencias = new ArrayList<String>();
        usuarioActivo = null;
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            return;
        }

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCorreo().equals(usuario.getCorreo())) {
                return;
            }
        }

        usuarios.add(usuario);
    }

    public boolean iniciarSesion(String correo, String contrasena) {
        if (correo == null || contrasena == null) {
            return false;
        }

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);

            if (usuario.getCorreo().equals(correo)
                    && usuario.validarContrasena(contrasena)) {
                usuarioActivo = usuario;
                return true;
            }
        }

        return false;
    }

    public void cerrarSesion() {
        usuarioActivo = null;
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void agregarActividad(Actividad actividad) {
        if (actividad != null
                && buscarActividad(actividad.getNombre()) == null) {
            actividades.add(actividad);
        }
    }

    public Actividad buscarActividad(String nombreActividad) {
        if (nombreActividad == null) {
            return null;
        }

        for (int i = 0; i < actividades.size(); i++) {
            Actividad actividad = actividades.get(i);

            if (actividad.getNombre().equals(nombreActividad)) {
                return actividad;
            }
        }

        return null;
    }

    public ArrayList<Actividad> obtenerActividadesDisponibles() {
        ArrayList<Actividad> disponibles = new ArrayList<Actividad>();

        for (int i = 0; i < actividades.size(); i++) {
            if (actividades.get(i).verificarDisponibilidad()) {
                disponibles.add(actividades.get(i));
            }
        }

        return disponibles;
    }

    public boolean aplicarEstudiante(Estudiante estudiante,
                                     Actividad actividad) {
        if (estudiante == null || actividad == null) {
            return false;
        }
        return estudiante.aplicarActividad(actividad);
    }

    public boolean registrarAsistenciaQR(String codigoQR,
                                         Actividad actividad) {
        if (codigoQR == null || actividad == null) {
            return false;
        }

        Estudiante estudiante = buscarEstudiantePorQR(codigoQR);

        if (estudiante == null) {
            return false;
        }

        if (validarRegistroDuplicado(estudiante, actividad)) {
            return false;
        }

        asistencias.add(codigoQR + "-" + actividad.getNombre());
        acreditarHoras(estudiante, actividad);
        return true;
    }

    public void acreditarHoras(Estudiante estudiante,
                               Actividad actividad) {
        if (estudiante == null || actividad == null) {
            return;
        }

        int horas = actividad.calcularHorasOtorgadas();
        estudiante.registrarHoras(horas);
        estudiante.agregarAlHistorial(actividad);
    }

    public String getNombreDirectorCarrera() {
        return nombreDirectorCarrera;
    }

    public void setNombreDirectorCarrera(String nombreDirectorCarrera) {
        this.nombreDirectorCarrera = nombreDirectorCarrera;
    }

    public String getCorreoDirectorCarrera() {
        return correoDirectorCarrera;
    }

    public void setCorreoDirectorCarrera(String correoDirectorCarrera) {
        this.correoDirectorCarrera = correoDirectorCarrera;
    }

    public String getTelefonoDirectorCarrera() {
        return telefonoDirectorCarrera;
    }

    public void setTelefonoDirectorCarrera(String telefonoDirectorCarrera) {
        this.telefonoDirectorCarrera = telefonoDirectorCarrera;
    }

    public String getUbicacionDirectorCarrera() {
        return ubicacionDirectorCarrera;
    }

    public void setUbicacionDirectorCarrera(String ubicacionDirectorCarrera) {
        this.ubicacionDirectorCarrera = ubicacionDirectorCarrera;
    }

    public String getCorreoAyudasFinancieras() {
        return correoAyudasFinancieras;
    }

    public void setCorreoAyudasFinancieras(
            String correoAyudasFinancieras) {
        this.correoAyudasFinancieras = correoAyudasFinancieras;
    }

    public String getTelefonoAyudasFinancieras() {
        return telefonoAyudasFinancieras;
    }

    public void setTelefonoAyudasFinancieras(
            String telefonoAyudasFinancieras) {
        this.telefonoAyudasFinancieras = telefonoAyudasFinancieras;
    }

    public String getUbicacionAyudasFinancieras() {
        return ubicacionAyudasFinancieras;
    }

    public void setUbicacionAyudasFinancieras(
            String ubicacionAyudasFinancieras) {
        this.ubicacionAyudasFinancieras = ubicacionAyudasFinancieras;
    }

    public Estudiante buscarEstudiantePorQR(String codigoQR) {
        if (codigoQR == null) {
            return null;
        }

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i) instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) usuarios.get(i);

                if (estudiante.getCodigoQR().equals(codigoQR)) {
                    return estudiante;
                }
            }
        }

        return null;
    }

    public boolean validarRegistroDuplicado(Estudiante estudiante,
                                            Actividad actividad) {
        if (estudiante == null || actividad == null) {
            return false;
        }

        String registro = estudiante.getCodigoQR()
                + "-" + actividad.getNombre();

        return asistencias.contains(registro);
    }

    public String obtenerEstadisticasOrganizador(
            Organizador organizador) {
        if (organizador == null) {
            return "Organizador no encontrado";
        }

        Actividad[] historial = organizador.getHistorialActividades();
        int totalHoras = 0;
        int cantidadActividades = 0;

        if (historial == null) {
            return "Actividades creadas: 0\nTotal de horas: 0";
        }

        for (int i = 0; i < historial.length; i++) {
            if (historial[i] != null) {
                cantidadActividades++;
                totalHoras = totalHoras
                        + historial[i].getCantidadHoras();
            }
        }

        return "Actividades creadas: " + cantidadActividades
                + "\nTotal de horas: " + totalHoras;
    }

    public String obtenerInformacionDirectorCarrera() {
        return "Nombre: " + nombreDirectorCarrera
                + "\nCorreo: " + correoDirectorCarrera
                + "\nTeléfono: " + telefonoDirectorCarrera
                + "\nUbicación: " + ubicacionDirectorCarrera;
    }

    public String obtenerInformacionAyudasFinancieras() {
        return "Correo: " + correoAyudasFinancieras
                + "\nTeléfono: " + telefonoAyudasFinancieras
                + "\nUbicación: " + ubicacionAyudasFinancieras;
    }

    public ArrayList<Actividad> consultarHistorialEstudiante(
            Estudiante estudiante) {
        ArrayList<Actividad> historial = new ArrayList<Actividad>();

        if (estudiante == null) {
            return historial;
        }

        Actividad[] actividadesEstudiante =
                estudiante.getHistorialActividades();

        if (actividadesEstudiante == null) {
            return historial;
        }

        for (int i = 0; i < actividadesEstudiante.length; i++) {
            if (actividadesEstudiante[i] != null) {
                historial.add(actividadesEstudiante[i]);
            }
        }

        return historial;
    }

    public double consultarProgresoEstudiante(
            Estudiante estudiante) {
        if (estudiante == null) {
            return 0;
        }
        return estudiante.calcularProgreso();
    }
} 