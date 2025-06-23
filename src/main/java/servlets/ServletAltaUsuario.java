package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;
import excepciones.NombreUsuarioExistenteException;
import excepciones.ContrasenasNoCoincidenException;
import excepciones.TodosLosCamposObligatorios;

import java.io.IOException;

@WebServlet("/ServletAltaUsuario")
public class ServletAltaUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletAltaUsuario() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/jsp/admin/altaUsuario.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("btnUsuario") != null) {
            boolean status = false;
            String mensajeError = null;

            try {
                String tipoUser = request.getParameter("tipoUser");
                String username = request.getParameter("username");
                String pass = request.getParameter("pass");
                String passRepetida = request.getParameter("passRepetida");

                if (tipoUser == null || username == null || pass == null || passRepetida == null ||
                    tipoUser.isEmpty() || username.isEmpty() || pass.isEmpty() || passRepetida.isEmpty()) {
                    throw new TodosLosCamposObligatorios("Todos los campos son obligatorios.");
                }

                if (!pass.equals(passRepetida)) {
                    throw new ContrasenasNoCoincidenException("Las contraseñas no coinciden.");
                }

                UsuarioDao dao = new UsuarioDaoImpl();
                if (dao.ExisteNombreUsuario(username)) {
                    throw new NombreUsuarioExistenteException("El nombre de usuario ya está en uso.");
                }

                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombreUsuario(username);
                nuevoUsuario.setClave(pass);
                nuevoUsuario.setTipo("BANCARIO");
                nuevoUsuario.setIsAdmin(tipoUser.equals("Admin"));
                nuevoUsuario.setEstado(true);

                status = dao.Agregar(nuevoUsuario);

            } catch (NombreUsuarioExistenteException e) {
                mensajeError = e.getMessage();
            } catch (TodosLosCamposObligatorios e) {
                mensajeError = e.getMessage();
	        } catch (ContrasenasNoCoincidenException e) {
	            request.setAttribute("mensajeError", e.getMessage());
	            RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
	            rd.forward(request, response);
	            return;
	        }
            catch (Exception e) {
                mensajeError = "Error inesperado al procesar la solicitud.";
                e.printStackTrace();
            }

            request.setAttribute("estado", status);
            request.setAttribute("mensajeError", mensajeError);
            RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
            rd.forward(request, response);
            return;
        }

        doGet(request, response);
    }
}
