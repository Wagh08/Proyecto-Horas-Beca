public class Main {

    public static void main(String[] args) {
        Sistema sistema = new Sistema();

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Estudiante");
        estudiante.setCorreo("estudiante@correo.com");
        estudiante.setContraseña("1234");
        estudiante.setCarne(1001);
        estudiante.setHorasRequeridas(100);

        Organizador organizador = new Organizador();
        organizador.setNombre("Organizador");
        organizador.setCorreo("organizador@correo.com");
        organizador.setContraseña("1234");
        organizador.setDepartamento("Bienestar Estudiantil");

        sistema.registrarUsuario(estudiante);
        sistema.registrarUsuario(organizador);

        sistema.setNombreDirectorCarrera("Director de carrera");
        sistema.setCorreoDirectorCarrera("director@correo.com");
        sistema.setTelefonoDirectorCarrera("12345678");
        sistema.setUbicacionDirectorCarrera("Oficina de dirección");

        sistema.setCorreoAyudasFinancieras("ayudas@correo.com");
        sistema.setTelefonoAyudasFinancieras("87654321");
        sistema.setUbicacionAyudasFinancieras("Oficina de ayudas");

        Controlador controlador = new Controlador(sistema);
        Vista vista = new Vista(controlador);
        vista.iniciar();
    }
}