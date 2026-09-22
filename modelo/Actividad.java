package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Actividad {
    private String nombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String descripcion;
    private String ubicacion;
    private int cantidadHoras;
    private int cupoMaximo;
    private int cuposRestantes;
    private boolean horasDobles;
    private Organizador organizador;
    private Estudiante[] estudiantesInscritos;
    private String estado;

    public Actividad() {
        nombre = "";
        descripcion = "";
        ubicacion = "";
        cantidadHoras = 0;
        cupoMaximo = 0;
        cuposRestantes = 0;
        horasDobles = false;
        estudiantesInscritos = new Estudiante[0];
        estado = "Abierta";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        if (fecha == null) {
            return "";
        }
        return fecha.toString();
    }

    public void setFecha(String fecha) {
        this.fecha = LocalDate.parse(fecha);
    }

    public String getHoraInicio() {
        if (horaInicio == null) {
            return "";
        }
        return horaInicio.toString();
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = LocalTime.parse(horaInicio);
    }

    public String getHoraFin() {
        if (horaFin == null) {
            return "";
        }
        return horaFin.toString();
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = LocalTime.parse(horaFin);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCantidadHoras() {
        return cantidadHoras;
    }

    public void setCantidadHoras(int cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public boolean agregarEstudiante(Estudiante estudiante) {
        if (estudiante == null || !verificarDisponibilidad()) {
            return false;
        }

        for (int i = 0; i < estudiantesInscritos.length; i++) {
            if (estudiantesInscritos[i] == estudiante) {
                return false;
            }
        }

        for (int i = 0; i < estudiantesInscritos.length; i++) {
            if (estudiantesInscritos[i] == null) {
                estudiantesInscritos[i] = estudiante;
                calcularCuposRestantes();
                return true;
            }
        }
        return false;
    }

    public void eliminarEstudiante(Estudiante estudiante) {
        for (int i = 0; i < estudiantesInscritos.length; i++) {
            if (estudiantesInscritos[i] == estudiante) {
                estudiantesInscritos[i] = null;
                calcularCuposRestantes();
            }
        }
    }

    public boolean verificarDisponibilidad() {
        return cuposRestantes > 0 && "Abierta".equals(estado);
    }

    public int calcularHorasOtorgadas() {
        if (horaInicio == null || horaFin == null) {
            return 0;
        }

        int minutosInicio = horaInicio.getHour() * 60
                + horaInicio.getMinute();
        int minutosFin = horaFin.getHour() * 60
                + horaFin.getMinute();
        int diferencia = minutosFin - minutosInicio;

        if (diferencia <= 0) {
            cantidadHoras = 0;
            return cantidadHoras;
        }

        cantidadHoras = (diferencia + 59) / 60;
        if (horasDobles) {
            cantidadHoras = cantidadHoras * 2;
        }
        return cantidadHoras;
    }

    public void cerrarConvocatoria() {
        estado = "Cerrada";
    }

    public void cancelarActividad() {
        estado = "Cancelada";
    }

    public String mostrarInformacion() {
        return "Nombre: " + nombre
                + "\nFecha: " + fecha
                + "\nHorario: " + horaInicio + " - " + horaFin
                + "\nDescripción: " + descripcion
                + "\nUbicación: " + ubicacion
                + "\nCantidad de horas: " + cantidadHoras
                + "\nCupos restantes: " + cuposRestantes
                + "\nEstado: " + estado;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        if (cupoMaximo < 0) {
            return;
        }

        Estudiante[] nuevoArreglo = new Estudiante[cupoMaximo];
        int limite = estudiantesInscritos.length;

        if (cupoMaximo < limite) {
            limite = cupoMaximo;
        }

        for (int i = 0; i < limite; i++) {
            nuevoArreglo[i] = estudiantesInscritos[i];
        }

        this.cupoMaximo = cupoMaximo;
        estudiantesInscritos = nuevoArreglo;
        calcularCuposRestantes();
    }

    public int getCuposRestantes() {
        return cuposRestantes;
    }

    public void setCuposRestantes(int cuposRestantes) {
        this.cuposRestantes = cuposRestantes;
    }

    public boolean sonHorasDobles() {
        return horasDobles;
    }

    public void setHorasDobles(boolean horasDobles) {
        this.horasDobles = horasDobles;
    }

    public Organizador getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Organizador organizador) {
        this.organizador = organizador;
    }

    public Estudiante[] getEstudiantesInscritos() {
        return estudiantesInscritos;
    }

    public void setEstudiantesInscritos(Estudiante[] estudiantes) {
        if (estudiantes == null) {
            estudiantesInscritos = new Estudiante[cupoMaximo];
        } else {
            estudiantesInscritos = estudiantes;
            cupoMaximo = estudiantes.length;
        }
        calcularCuposRestantes();
    }

    public int calcularCuposRestantes() {
        int cantidadInscritos = 0;
        for (int i = 0; i < estudiantesInscritos.length; i++) {
            if (estudiantesInscritos[i] != null) {
                cantidadInscritos++;
            }
        }
        cuposRestantes = cupoMaximo - cantidadInscritos;
        return cuposRestantes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}