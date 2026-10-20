package modelo;

public class Usuario {
    private String nombre;
    private String correo;
    private int telefono;
    private String contraseña;
    private String carreraOCargo;

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCarreraOCargo() {
        return carreraOCargo;
    }

    public void setCarreraOCargo(String carreraOCargo) {
        this.carreraOCargo = carreraOCargo;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contraseña.equals(contrasena);
    }

    public String mostrarInformacion() {
        return "Nombre: " + nombre
                + "\nCorreo: " + correo
                + "\nTeléfono: " + telefono
                + "\nCarrera o cargo: " + carreraOCargo;
    }
}