package vista;

import java.util.ArrayList;
import java.util.Scanner;
import controlador.Controlador;
import modelo.Actividad;
import modelo.Estudiante;
import modelo.Organizador;
import modelo.Sistema;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

public class Vista {

    private final Scanner scanner;
    private final Controlador controlador;
    private int opcionSeleccionada;

    public Vista() {
        this(new Controlador());
    }

    public Vista(Controlador controlador) {
        this.scanner = new Scanner(System.in);
        this.controlador = controlador;
        this.controlador.setVista(this);
    }

    public void iniciar() {
        boolean programaActivo = true;

        while (programaActivo) {
            mostrarMenuPrincipal();
            opcionSeleccionada = leerEntero("Seleccione una opción: ");

            switch (opcionSeleccionada) {
                case 1:
                    solicitarCredenciales();
                    break;
                case 2:
                    mostrarActividades(controlador.consultarActividades());
                    break;
                case 3:
                    solicitarRegistro();
                    break;
                case 0:
                    programaActivo = false;
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    public void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE HORAS BECA ===");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Ver oportunidades disponibles");
        System.out.println("3. Registrarse");
        System.out.println("0. Salir");
    }

    public void mostrarMenuEstudiante() {
        boolean sesionActiva = true;

        while (sesionActiva) {
            System.out.println("\n=== MENÚ DE ESTUDIANTE ===");
            System.out.println("1. Ver oportunidades disponibles");
            System.out.println("2. Aplicar a una oportunidad");
            System.out.println("3. Mostrar progreso anual");
            System.out.println("4. Mostrar historial");
            System.out.println("5. Mostrar código QR");
            System.out.println("6. Información del director de carrera");
            System.out.println("7. Información de ayudas financieras");
            System.out.println("0. Cerrar sesión");

            opcionSeleccionada = leerEntero("Seleccione una opción: ");

            switch (opcionSeleccionada) {
                case 1:
                    mostrarActividades(controlador.consultarActividades());
                    break;
                case 2:
                    solicitarAplicacion();
                    break;
                case 3:
                    mostrarProgreso(controlador.consultarProgreso());
                    break;
                case 4:
                    mostrarHistorial(controlador.consultarHistorial());
                    break;
                case 5:
                    mostrarCodigoQR(controlador.generarCodigoQR());
                    break;
                case 6:
                    mostrarInformacionDirectorCarrera();
                    break;
                case 7:
                    mostrarInformacionAyudasFinancieras();
                    break;
                case 0:
                    controlador.cerrarSesion();
                    sesionActiva = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    public void mostrarMenuOrganizador() {
        boolean sesionActiva = true;

        while (sesionActiva) {
            System.out.println("\n=== MENÚ DE ORGANIZADOR ===");
            System.out.println("1. Ver oportunidades disponibles");
            System.out.println("2. Crear oportunidad");
            System.out.println("3. Registrar asistencia con QR");
            System.out.println("4. Información del director de carrera");
            System.out.println("5. Información de ayudas financieras");
            System.out.println("0. Cerrar sesión");

            opcionSeleccionada = leerEntero("Seleccione una opción: ");

            switch (opcionSeleccionada) {
                case 1:
                    mostrarActividades(controlador.consultarActividades());
                    break;
                case 2:
                    solicitarDatosActividad();
                    break;
                case 3:
                    solicitarEscaneoQR();
                    break;
                case 4:
                    mostrarInformacionDirectorCarrera();
                    break;
                case 5:
                    mostrarInformacionAyudasFinancieras();
                    break;
                case 0:
                    controlador.cerrarSesion();
                    sesionActiva = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    public void solicitarCredenciales() {
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        if (!controlador.iniciarSesion(correo, contrasena)) {
            System.out.println("Correo o contraseña incorrectos.");
            return;
        }

        Usuario usuario = controlador.getUsuarioActual();
        System.out.println("Bienvenido, " + usuario.getNombre() + ".");

        if (usuario instanceof Estudiante) {
            mostrarMenuEstudiante();
        } else if (usuario instanceof Organizador) {
            mostrarMenuOrganizador();
        }
    }

    public void solicitarRegistro() {
        System.out.println("\n=== REGISTRO DE USUARIO ===");
        System.out.println("1. Estudiante");
        System.out.println("2. Organizador");

        int tipoUsuario = leerEntero("Seleccione el tipo de usuario: ");

        if (tipoUsuario != 1 && tipoUsuario != 2) {
            System.out.println("Tipo de usuario inválido.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        int telefono = leerEntero("Teléfono: ");
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        if (tipoUsuario == 1) {
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre(nombre);
            estudiante.setCorreo(correo);
            estudiante.setTelefono(telefono);
            estudiante.setContraseña(contrasena);

            System.out.print("Carrera: ");
            estudiante.setCarreraOCargo(scanner.nextLine());
            estudiante.setCarne(leerEntero("Carné: "));
            estudiante.setHorasRequeridas(
                    leerEntero("Horas requeridas al año: "));
            System.out.print("Ciclo actual: ");
            estudiante.setCicloProgreso(scanner.nextLine());
            estudiante.generarQR();

            controlador.registrarUsuario(estudiante);
        } else {
            Organizador organizador = new Organizador();
            organizador.setNombre(nombre);
            organizador.setCorreo(correo);
            organizador.setTelefono(telefono);
            organizador.setContraseña(contrasena);

            System.out.print("Cargo: ");
            organizador.setCarreraOCargo(scanner.nextLine());
            System.out.print("Departamento: ");
            organizador.setDepartamento(scanner.nextLine());

            controlador.registrarUsuario(organizador);
        }

        System.out.println("Usuario registrado correctamente.");
        System.out.println("Ahora puede iniciar sesión.");
    }

    public void mostrarActividades(ArrayList<Actividad> actividades) {
        System.out.println("\n=== OPORTUNIDADES DISPONIBLES ===");

        if (actividades == null || actividades.isEmpty()) {
            System.out.println("No hay oportunidades disponibles.");
            return;
        }

        for (int i = 0; i < actividades.size(); i++) {
            Actividad actividad = actividades.get(i);
            System.out.println("\n" + (i + 1) + ". " + actividad.getNombre());
            System.out.println(actividad.mostrarInformacion());
        }
    }

    public void mostrarProgreso(double progreso) {
        System.out.printf("Progreso anual: %.2f%%%n", progreso);
    }

    public void mostrarHistorial(Actividad[] actividades) {
        System.out.println("\n=== HISTORIAL DE ACTIVIDADES ===");
        boolean existeActividad = false;

        for (Actividad actividad : actividades) {
            if (actividad != null) {
                existeActividad = true;
                System.out.println("- " + actividad.getNombre()
                        + " (" + actividad.calcularHorasOtorgadas() + " horas)");
            }
        }

        if (!existeActividad) {
            System.out.println("No hay actividades registradas.");
        }
    }

    public void mostrarInformacionDirectorCarrera() {
        System.out.println(controlador.consultarInformacionDirectorCarrera());
    }

    public void mostrarInformacionAyudasFinancieras() {
        System.out.println(controlador.consultarInformacionAyudasFinancieras());
    }

    public void mostrarCodigoQR(String codigoQR) {
        System.out.println("Código QR del estudiante: " + codigoQR);
    }

    public void solicitarEscaneoQR() {
        ArrayList<Actividad> actividades = controlador.consultarActividades();
        Actividad actividad = seleccionarActividad(actividades);

        if (actividad == null) {
            return;
        }

        System.out.print("Ingrese el código QR del estudiante: ");
        String codigoQR = scanner.nextLine();

        if (controlador.escanearCodigoQR(codigoQR, actividad)) {
            System.out.println("Asistencia y horas registradas correctamente.");
        } else {
            System.out.println("No fue posible registrar la asistencia.");
        }
    }

    public void mostrarMensajeAplicacionesCerradas() {
        System.out.println("La actividad ya no acepta aplicaciones.");
    }

    private void solicitarAplicacion() {
        ArrayList<Actividad> actividades = controlador.consultarActividades();
        Actividad actividad = seleccionarActividad(actividades);

        if (actividad == null) {
            return;
        }

        if (controlador.aplicarActividad(actividad)) {
            System.out.println("Aplicación registrada correctamente.");
        } else {
            mostrarMensajeAplicacionesCerradas();
        }
    }

    private void solicitarDatosActividad() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Fecha: ");
        String fecha = scanner.nextLine();
        System.out.print("Hora de inicio: ");
        String horaInicio = scanner.nextLine();
        System.out.print("Hora de finalización: ");
        String horaFin = scanner.nextLine();
        System.out.print("Ubicación: ");
        String ubicacion = scanner.nextLine();
        int cantidadHoras = leerEntero("Cantidad de horas: ");
        int cupoMaximo = leerEntero("Cupo máximo: ");
        boolean horasDobles = leerSiNo("¿Ofrece horas dobles? (s/n): ");

        Actividad actividad = controlador.crearActividad(
                nombre,
                descripcion,
                fecha,
                horaInicio,
                horaFin,
                ubicacion,
                cantidadHoras,
                cupoMaximo,
                horasDobles);

        if (actividad != null) {
            System.out.println("Actividad creada correctamente.");
        } else {
            System.out.println("No fue posible crear la actividad.");
        }
    }

    private Actividad seleccionarActividad(ArrayList<Actividad> actividades) {
        mostrarActividades(actividades);

        if (actividades == null || actividades.isEmpty()) {
            return null;
        }

        int posicion = leerEntero("Seleccione una actividad: ") - 1;

        if (posicion < 0 || posicion >= actividades.size()) {
            System.out.println("Actividad inválida.");
            return null;
        }

        return actividades.get(posicion);
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException excepcion) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = scanner.nextLine().trim();

            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }

            System.out.println("Responda con s o n.");
        }
    }
}
