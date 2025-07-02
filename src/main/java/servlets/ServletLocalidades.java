package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import dao.LocalidadDao;
import daoImpl.LocalidadDaoImpl;
import dominio.Localidad;
import dominio.Usuario;


@WebServlet("/ServletLocalidades")
public class ServletLocalidades extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ServletLocalidades() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idProvinciaStr = request.getParameter("idProvincia");
        int idProvincia = Integer.parseInt(idProvinciaStr);
        
        LocalidadDao localidadDao = new LocalidadDaoImpl();
        List<Localidad> localidades = localidadDao.obtenerPorProvincia(idProvincia);

        // Convertir lista a JSON
        Gson gson = new Gson();
        String json = gson.toJson(localidades);

        // Configurar respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
