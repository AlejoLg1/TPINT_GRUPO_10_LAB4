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


import negocioImpl.ClienteNegocioImpl;
import negocio.ClienteNegocio;

import negocio.ProvinciaNegocio;
import negocio.LocalidadNegocio;

import negocioImpl.ProvinciaNegocioImpl;
import negocioImpl.LocalidadNegocioImpl;

import dominio.Cliente;
import dominio.Direccion;
import dominio.Localidad;
import dominio.Provincia;
import dominio.Usuario;



@WebServlet("/ServletAltaCliente")
public class ServletAltaCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        cargarDatosProvinciaYLocalidad(request);
        reenviarDatos(request, response);
        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if (usuarioSesion == null || (!auth.validarRolAdmin(usuarioSesion) && !auth.validarRolBancario(usuarioSesion))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        Cliente cliente = mapearCliente(request);
        Usuario usuario = mapearUsuario(request);
        Direccion direccion = mapearDireccion(request);
        String passRepetida = request.getParameter("passRepetida");

        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        boolean status = false;
        String msg = "";

        try {
            status = clienteNegocio.registrarCliente(cliente, usuario, direccion, passRepetida);
            if (status) {
                response.sendRedirect(request.getContextPath() + "/ServletListarCliente");
                return;
            }
        } catch (Exception e) {
            msg = "❌ " + e.getMessage();
        }

        request.setAttribute("estado", status);
        request.setAttribute("mensaje", msg);
        reenviarDatos(request, response);
        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
    }

    private Cliente mapearCliente(HttpServletRequest request) {
        Cliente c = new Cliente();
        c.setNombre(request.getParameter("nombre"));
        c.setApellido(request.getParameter("apellido"));
        c.setDni(request.getParameter("dni"));
        c.setCuil(request.getParameter("cuil"));
        c.setCorreo(request.getParameter("email"));
        c.setTelefono(request.getParameter("telefono"));
        c.setNacionalidad(request.getParameter("nacionalidad"));
        c.setFechaNacimiento(LocalDate.parse(
            request.getParameter("fechanac") != null ? request.getParameter("fechanac") : LocalDate.now().toString()
        ));
        c.setSexo(convertirSexo(request.getParameter("sexo")));
        c.setEstado(true);

        int idProvincia = Integer.parseInt(request.getParameter("idProvincia"));
        int idLocalidad = Integer.parseInt(request.getParameter("idLocalidad"));
        c.setProvincia(new Provincia(idProvincia, request.getParameter("provincia")));
        c.setLocalidad(new Localidad(idLocalidad, request.getParameter("localidad")));
        return c;
    }

    private Usuario mapearUsuario(HttpServletRequest request) {
        Usuario u = new Usuario();
        u.setNombreUsuario(request.getParameter("username"));
        u.setClave(request.getParameter("pass"));
        u.setTipo("Cliente");
        u.setEstado(true);
        u.setIsAdmin(false);
        return u;
    }

    private Direccion mapearDireccion(HttpServletRequest request) {
        Direccion d = new Direccion();
        d.setCalle(request.getParameter("direccion"));
        d.setNumero(request.getParameter("numero"));
        return d;
    }

    private String convertirSexo(String sexoCompleto) {
        return switch (sexoCompleto) {
            case "Masculino" -> "M";
            case "Femenino" -> "F";
            case "Otro" -> "X";
            default -> "X";
        };
    }

    private void reenviarDatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Setear los valores ingresados
        request.setAttribute("nombre", request.getParameter("nombre"));
        request.setAttribute("apellido", request.getParameter("apellido"));
        request.setAttribute("dni", request.getParameter("dni"));
        request.setAttribute("cuil", request.getParameter("cuil"));
        request.setAttribute("nacionalidad", request.getParameter("nacionalidad"));
        request.setAttribute("fechanac", request.getParameter("fechanac"));
        request.setAttribute("direccion", request.getParameter("direccion"));
        request.setAttribute("numero", request.getParameter("numero"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("telefono", request.getParameter("telefono"));
        request.setAttribute("sexo", request.getParameter("sexo"));
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("pass", request.getParameter("pass"));
        request.setAttribute("passRepetida", request.getParameter("passRepetida"));

      
        ProvinciaNegocio provinciaNegocio = new ProvinciaNegocioImpl();
        LocalidadNegocio localidadNegocio = new LocalidadNegocioImpl();

        List<Provincia> provincias = provinciaNegocio.obtenerTodas();
        request.setAttribute("provincias", provincias);

        String strIdProvincia = request.getParameter("idProvincia");
        if (strIdProvincia != null && !strIdProvincia.isEmpty()) {
            try {
                int idProvincia = Integer.parseInt(strIdProvincia);
                request.setAttribute("idProvincia", idProvincia);

                List<Localidad> localidades = localidadNegocio.obtenerPorProvincia(idProvincia);
                request.setAttribute("localidades", localidades);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String strIdLocalidad = request.getParameter("idLocalidad");
        if (strIdLocalidad != null && !strIdLocalidad.isEmpty()) {
            try {
                int idLocalidad = Integer.parseInt(strIdLocalidad);
                request.setAttribute("idLocalidad", idLocalidad);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }


    private void cargarDatosProvinciaYLocalidad(HttpServletRequest request) {
        ProvinciaNegocio provinciaNegocio = new ProvinciaNegocioImpl();
        LocalidadNegocio localidadNegocio = new LocalidadNegocioImpl();

        List<Provincia> provincias = provinciaNegocio.obtenerTodas();
        request.setAttribute("provincias", provincias);

        String strIdProvincia = request.getParameter("idProvincia");
        if (strIdProvincia != null && !strIdProvincia.isEmpty()) {
            try {
                int idProvincia = Integer.parseInt(strIdProvincia);
                List<Localidad> localidades = localidadNegocio.obtenerPorProvincia(idProvincia);
                request.setAttribute("localidades", localidades);
                request.setAttribute("idProvincia", idProvincia);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String strIdLocalidad = request.getParameter("idLocalidad");
        if (strIdLocalidad != null && !strIdLocalidad.isEmpty()) {
            try {
                int idLocalidad = Integer.parseInt(strIdLocalidad);
                request.setAttribute("idLocalidad", idLocalidad);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

}
