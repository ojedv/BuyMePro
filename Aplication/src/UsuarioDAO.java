import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Registra un nuevo usuario en la base de datos
     * @param usuario El usuario a registrar
     * @return El ID asignado al usuario o -1 en caso de error
     */
    public int registrarUsuario(Usuario usuario) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            conexion = ConexionBD.obtenerConexion();

            // Verificar si el nickname ya existe
            if (existeNickname(usuario.getNickname(), conexion)) {
                System.out.println("El nickname ya está en uso");
                return -1;
            }

            // Verificar si el correo ya existe
            if (existeCorreo(usuario.getCorreo(), conexion)) {
                System.out.println("El correo ya está registrado");
                return -1;
            }

            String sql = "INSERT INTO usuario (nickname, contraseña, nombre, apellidos, telefono, correo) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, usuario.getNickname());
            ps.setString(2, usuario.getContraseña());
            ps.setString(3, usuario.getNombre());
            ps.setString(4, usuario.getApellidos());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getCorreo());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                    usuario.setId_usuario(idGenerado);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }

        return idGenerado;
    }

    /**
     * Verifica si un nickname ya existe en la base de datos
     * @param nickname El nickname a verificar
     * @param conexion La conexión a la base de datos
     * @return true si existe, false en caso contrario
     */
    private boolean existeNickname(String nickname, Connection conexion) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM usuario WHERE nickname = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, nickname);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return false;
    }

    /**
     * Verifica si un correo ya existe en la base de datos
     * @param correo El correo a verificar
     * @param conexion La conexión a la base de datos
     * @return true si existe, false en caso contrario
     */
    private boolean existeCorreo(String correo, Connection conexion) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
            ps = conexion.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }

        return false;
    }

    /**
     * Realiza el inicio de sesión verificando nickname y contraseña
     * @param nickname El nickname del usuario
     * @param contraseña La contraseña del usuario
     * @return El objeto Usuario si las credenciales son correctas, null en caso contrario
     */
    public Usuario iniciarSesion(String nickname, String contraseña) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            String sql = "SELECT * FROM usuario WHERE nickname = ? AND contraseña = ?";
            ps = conexion.prepareStatement(sql);

            ps.setString(1, nickname);
            ps.setString(2, contraseña);

            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");

                usuario = new Usuario(nickname, contraseña, nombre, apellidos, telefono, correo);
                usuario.setId_usuario(id);
            }

        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }

        return usuario;
    }

    /**
     * Obtiene un usuario por su ID
     * @param idUsuario El ID del usuario a buscar
     * @return El objeto Usuario o null si no se encuentra
     */
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
            ps = conexion.prepareStatement(sql);

            ps.setInt(1, idUsuario);

            rs = ps.executeQuery();

            if (rs.next()) {
                String nickname = rs.getString("nickname");
                String contraseña = rs.getString("contraseña");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");

                usuario = new Usuario(nickname, contraseña, nombre, apellidos, telefono, correo);
                usuario.setId_usuario(idUsuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }

        return usuario;
    }

    /**
     * Lista todos los usuarios registrados
     * @return Lista de usuarios
     */
    public List<Usuario> listarUsuarios() {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            conexion = ConexionBD.obtenerConexion();
            String sql = "SELECT * FROM usuario";
            ps = conexion.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nickname = rs.getString("nickname");
                String contraseña = rs.getString("contraseña");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");

                Usuario usuario = new Usuario(nickname, contraseña, nombre, apellidos, telefono, correo);
                usuario.setId_usuario(id);

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }

        return usuarios;
    }

    /**
     * Cierra los recursos de la base de datos
     */
    private void cerrarRecursos(Connection conexion, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }

        ConexionBD.cerrarConexion();
    }
}
