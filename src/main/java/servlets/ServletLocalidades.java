package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import dominio.Localidad;
import negocioImpl.LocalidadNegocioImpl;

@WebServlet("/ServletLocalidades")
public class ServletLocalidades extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletLocalidades() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idProvinciaStr = request.getParameter("idProvincia");
        LocalidadNegocioImpl localidadNegocio = new LocalidadNegocioImpl();

        try {
            int idProvincia = Integer.parseInt(idProvinciaStr);
            List<Localidad> localidades = localidadNegocio.obtenerPorProvincia(idProvincia);

            // Convertir lista a JSON
            Gson gson = new Gson();
            String json = gson.toJson(localidades);

            // Configurar respuesta como JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de provincia inválido.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener localidades.");
        }
    }
}
