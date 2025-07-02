package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import negocioImpl.AutenticacionNegocioImpl;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;
import excepciones.NombreUsuarioExistenteException;
import excepciones.AutenticacionException;
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
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
        response.sendRedirect(request.getContextPath() + "/jsp/admin/altaUsuario.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
        if (request.getParameter("btnUsuario") != null) {
            boolean status = false;
            String msg = "✅ Usuario registrado correctamente ";

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
                
                if (username.contains(" "))
			        throw new AutenticacionException("El nombre de usuario no puede contener espacios.");
			    
			    if (pass.contains(" "))
			        throw new ContrasenasNoCoincidenException("La contraseña no puede contener espacios.");
			    
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
            	status = false;
                msg = "❌ " + e.getMessage();
            } catch (TodosLosCamposObligatorios e) {
            	status = false;
                msg = "❌ " + e.getMessage();
	        } catch (ContrasenasNoCoincidenException e) {
	            status = false;
	            msg = "❌ " + e.getMessage();
	        }
            catch (AutenticacionException e) {
				status = false;
				msg = "❌ " + e.getMessage();
            }
            catch (Exception e) {
            	status = false;
                msg = "❌ Error inesperado al procesar la solicitud.";
                e.printStackTrace();
            }

            request.setAttribute("estado", status);
            request.setAttribute("mensaje", msg);
            RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
            rd.forward(request, response);
            return;
        }

        doGet(request, response);
    }
}
