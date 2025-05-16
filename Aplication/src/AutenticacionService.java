public class AutenticacionService {
    private final UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;

    public AutenticacionService() {
        this.usuarioDAO = new UsuarioDAO();
        this.usuarioActual = null;
    }

    /**
     * Registra un nuevo usuario en el sistema
     * @param nickname Nombre de usuario
     * @param contraseña Contraseña
     * @param nombre Nombre real del usuario
     * @param apellidos Apellidos del usuario
     * @param telefono Teléfono de contacto
     * @param correo Correo electrónico
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarUsuario(String nickname, String contraseña, String nombre, String apellidos, String telefono, String correo) {
        // Validar datos
        if (!validarDatos(nickname, contraseña, nombre, apellidos, telefono, correo)) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(nickname, contraseña, nombre, apellidos, telefono, correo);
        int resultado = usuarioDAO.registrarUsuario(nuevoUsuario);

        return resultado > 0;
    }

    /**
     * Inicia sesión con las credenciales proporcionadas
     * @param nickname Nombre de usuario
     * @param contraseña Contraseña
     * @return true si el inicio de sesión fue exitoso, false en caso contrario
     */
    public boolean iniciarSesion(String nickname, String contraseña) {
        if (nickname == null || nickname.trim().isEmpty() || contraseña == null || contraseña.trim().isEmpty()) {
            return false;
        }

        usuarioActual = usuarioDAO.iniciarSesion(nickname, contraseña);

        return usuarioActual != null;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        usuarioActual = null;
    }

    /**
     * Verifica si hay un usuario con sesión iniciada
     * @return true si hay un usuario con sesión, false en caso contrario
     */
    public boolean estaLogueado() {
        return usuarioActual != null;
    }

    /**
     * Obtiene el usuario con sesión actual
     * @return El usuario actual o null si no hay sesión
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Valida los datos de registro
     */
    private boolean validarDatos(String nickname, String contraseña, String nombre, String apellidos, String telefono, String correo) {
        // Verificar que ningún campo esté vacío
        if (nickname == null || nickname.trim().isEmpty() ||
                contraseña == null || contraseña.trim().isEmpty() ||
                nombre == null || nombre.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty() ||
                correo == null || correo.trim().isEmpty()) {
            return false;
        }

        // Verificar formato de correo (simple)
        if (!correo.contains("@") || !correo.contains(".")) {
            return false;
        }

        // Verificar longitud mínima de nickname y contraseña
        if (nickname.length() < 3 || contraseña.length() < 4) {
            return false;
        }

        return true;
    }
}