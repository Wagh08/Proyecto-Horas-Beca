package modelo;

public class Estudiante extends Usuario {

    private int horasRequeridas;
    private int horasRealizadas;
    private int carne;
    private String codigoQR;
    private Actividad[] actividadesAplicadas;
    private Actividad[] historialActividades;
    private String cicloProgreso;

    private int cantidadAplicadas;
    private int cantidadHistorial;

    public Estudiante() {
        horasRequeridas = 0;
        horasRealizadas = 0;
        carne = 0;
        codigoQR = "";
        cicloProgreso = "";

        actividadesAplicadas = new Actividad[100];
        historialActividades = new Actividad[100];

        cantidadAplicadas = 0;
        cantidadHistorial = 0;
    }

    public int getHorasRequeridas() {
        return horasRequeridas;
    }

    public void setHorasRequeridas(int horasRequeridas) {
        this.horasRequeridas = horasRequeridas;
    }

    public int getHorasRealizadas() {
        return horasRealizadas;
    }

    public void setHorasRealizadas(int horasRealizadas) {
        this.horasRealizadas = horasRealizadas;
    }

    public int getCarne() {
        return carne;
    }

    public void setCarne(int carne) {
        this.carne = carne;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public Actividad[] getActividadesAplicadas() {
        return actividadesAplicadas;
    }

    public void setActividadesAplicadas(
            Actividad[] actividadesAplicadas) {

        this.actividadesAplicadas = actividadesAplicadas;
    }

    public Actividad[] getHistorialActividades() {
        return historialActividades;
    }

    public void setHistorialActividades(
            Actividad[] historialActividades) {

        this.historialActividades = historialActividades;
    }

    public String getCicloProgreso() {
        int horasPorSemestre = horasRequeridas / 2;

        return cicloProgreso + ": " + horasPorSemestre + " horas requeridas";
    }

    public void setCicloProgreso(String cicloProgreso) {
        this.cicloProgreso = cicloProgreso;
    }

    public boolean aplicarActividad(Actividad actividad) {
        if (actividad == null
                || cantidadAplicadas >= actividadesAplicadas.length) {
            return false;
        }

        if (actividad.agregarEstudiante(this)) {
            actividadesAplicadas[cantidadAplicadas] = actividad;
            cantidadAplicadas++;
            return true;
        }

        return false;
    }

    public void registrarHoras(int horas) {
        horasRealizadas = horasRealizadas + horas;
    }

    public int calcularHorasPendientes() {
        int horasPendientes =
                horasRequeridas - horasRealizadas;

        if (horasPendientes < 0) {
            horasPendientes = 0;
        }

        return horasPendientes;
    }

    public double calcularProgreso() {
        if (horasRequeridas == 0) {
            return 0;
        }

        return (double) horasRealizadas
                / horasRequeridas * 100;
    }

    public void agregarAlHistorial(Actividad actividad) {
        if (cantidadHistorial < historialActividades.length) {
            historialActividades[cantidadHistorial] = actividad;
            cantidadHistorial++;
        }
    }

    public String generarQR() {
        codigoQR = "QR-" + carne;

        return codigoQR;
    }

    public double consultarProgresoAnual() {
        return calcularProgreso();
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion()
                + "\nCarné: " + carne
                + "\nHoras requeridas al año: " + horasRequeridas
                + "\nHoras requeridas por semestre: " + (horasRequeridas / 2)
                + "\nHoras realizadas: "  + horasRealizadas
                + "\nHoras pendientes: " + calcularHorasPendientes()
                + "\nProgreso anual: " + calcularProgreso() + "%"
                + "\nCiclo: " + getCicloProgreso();
    }
}
