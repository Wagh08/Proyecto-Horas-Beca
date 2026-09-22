package modelo;

public class Organizador extends Usuario {

    private String departamento;
    private Actividad[] historialActividades;
    private int cantidadActividades;

    public Organizador() {
        departamento = "";
        historialActividades = new Actividad[100];
        cantidadActividades = 0;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Actividad[] getHistorialActividades() {
        return historialActividades;
    }

    public Actividad crearActividad(
            String nombre,
            String descripcion,
            String fecha,
            String horaInicio,
            String horaFin,
            int horasOtorgadas,
            boolean horasDobles) {

        Actividad actividad = new Actividad();

        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setFecha(fecha);
        actividad.setHoraInicio(horaInicio);
        actividad.setHoraFin(horaFin);
        actividad.setCantidadHoras(horasOtorgadas);
        actividad.setHorasDobles(horasDobles);
        actividad.setOrganizador(this);

        guardarActividad(actividad);

        return actividad;
    }

    public void guardarActividad(Actividad actividad) {
        if (cantidadActividades < historialActividades.length) {
            historialActividades[cantidadActividades] = actividad;
            cantidadActividades++;
        }
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion()
                + "\nDepartamento: " + departamento
                + "\nActividades creadas: " + cantidadActividades;
    }

    
}
