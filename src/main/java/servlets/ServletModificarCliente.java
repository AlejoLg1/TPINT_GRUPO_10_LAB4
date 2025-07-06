package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import negocio.ProvinciaNegocio;
import negocio.ClienteNegocio;
import negocioImpl.ClienteNegocioImpl;
import negocioImpl.ProvinciaNegocioImpl;
import dominio.Cliente;
import dominio.Direccion;
import dominio.Localidad;
import dominio.Provincia;
import dominio.Usuario;



@WebServlet("/ServletModificarCliente")
public class ServletModificarCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        int idCliente = Integer.parseInt(request.getParameter("id"));
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        ProvinciaNegocio provinciaNegocio = new ProvinciaNegocioImpl();

        Cliente cliente = clienteNegocio.obtenerClientePorId(idCliente);
        List<Provincia> provincias = provinciaNegocio.obtenerTodas();

        request.setAttribute("provincias", provincias);
        request.setAttribute("clienteMod", cliente);

        String jspDestino = (cliente != null)
            ? "/jsp/admin/altaCliente.jsp"
            : "/jsp/admin/clientes.jsp";

        request.getRequestDispatcher(jspDestino).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
        if (usuarioSesion == null || (!auth.validarRolAdmin(usuarioSesion) && !auth.validarRolBancario(usuarioSesion))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        String msg = "✅ Modificación realizada exitosamente";
        boolean status = false;

        try {
            // Mapear objetos
            Cliente cliente = mapearCliente(request);
            Usuario usuario = mapearUsuario(request);
            Direccion direccion = mapearDireccion(request);

            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            status = clienteNegocio.modificarCliente(cliente, usuario, direccion);

        } catch (Exception e) {
            status = false;
            msg = "❌ " + e.getMessage();
            e.printStackTrace();
        }

        // Recargar datos
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        ProvinciaNegocio provinciaNegocio = new ProvinciaNegocioImpl();

        Cliente clienteActualizado = clienteNegocio.obtenerClientePorId(Integer.parseInt(request.getParameter("idCliente")));
        List<Provincia> provincias = provinciaNegocio.obtenerTodas();

        request.setAttribute("estado", status);
        request.setAttribute("mensaje", msg);
        request.setAttribute("clienteMod", clienteActualizado);
        request.setAttribute("provincias", provincias);

        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
    }

    // Métodos de mapeo
    private Cliente mapearCliente(HttpServletRequest request) {
        Cliente c = new Cliente();
        c.setIdCliente(Integer.parseInt(request.getParameter("idCliente")));
        c.setNombre(request.getParameter("nombre"));
        c.setApellido(request.getParameter("apellido"));
        c.setDni(request.getParameter("dni"));
        c.setCuil(request.getParameter("cuil"));
        c.setNacionalidad(request.getParameter("nacionalidad"));
        c.setFechaNacimiento(LocalDate.parse(request.getParameter("fechanac")));
        c.setCorreo(request.getParameter("email"));
        c.setTelefono(request.getParameter("telefono"));
        c.setSexo(obtenerSexo(request.getParameter("sexo")));
        c.setEstado(true);

        Provincia provincia = new Provincia();
        provincia.setId(Integer.parseInt(request.getParameter("idProvincia")));
        c.setProvincia(provincia);

        Localidad localidad = new Localidad();
        localidad.setId(Integer.parseInt(request.getParameter("idLocalidad")));
        c.setLocalidad(localidad);

        Direccion direccion = new Direccion();
        direccion.setId(Integer.parseInt(request.getParameter("idDireccion")));
        direccion.setCalle(request.getParameter("direccion"));
        direccion.setNumero(request.getParameter("numero"));
        c.setDireccion(direccion);

        return c;
    }

    private Usuario mapearUsuario(HttpServletRequest request) {
        Usuario u = new Usuario();
        u.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
        u.setNombreUsuario(request.getParameter("username"));
        u.setClave(request.getParameter("pass"));
        u.setTipo("Cliente");
        u.setEstado(true);

        String pass = request.getParameter("pass");
        String passRepetida = request.getParameter("passRepetida");

        if (!pass.equals(passRepetida)) {
            throw new RuntimeException("Las contraseñas no coinciden.");
        }

        return u;
    }

    private Direccion mapearDireccion(HttpServletRequest request) {
        Direccion d = new Direccion();
        d.setId(Integer.parseInt(request.getParameter("idDireccion")));
        d.setCalle(request.getParameter("direccion"));
        d.setNumero(request.getParameter("numero"));
        return d;
    }

    private String obtenerSexo(String sexoCompleto) {
        switch (sexoCompleto) {
            case "Masculino": return "M";
            case "Femenino": return "F";
            case "Otro": return "X";
            default: return "X";
        }
    }
}
