package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dao.TipoCuentaDao;
import daoImpl.TipoCuentaDaoImpl;
import dominio.TipoCuenta;

@WebServlet("/ServletAltaCuenta")
public class ServletAltaCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public ServletAltaCuenta() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClienteDao clienteDao = new ClienteDaoImpl();
        List<Cliente> listaClientes = clienteDao.Listar();

        request.setAttribute("listaClientes", listaClientes);
        
        TipoCuentaDao tipoCuentaDao = new TipoCuentaDaoImpl();
        List<TipoCuenta> listaTiposCuenta = tipoCuentaDao.listar();
        request.setAttribute("listaTiposCuenta", listaTiposCuenta);


        request.getRequestDispatcher("/jsp/admin/altaCuentas.jsp").forward(request, response);
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
