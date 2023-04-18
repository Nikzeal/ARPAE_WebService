package container;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class AccessoUtenti
 */
@WebServlet("/AccessoUtenti")
public class AccessoUtenti extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    String username;
    String password;
    String risposta;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccessoUtenti() {
        super();
        
    }

    
    //Username check
    public boolean isValidUsername(String username) {
    	
    	if(username == null || username.contains(" ")) {
    		return false;
    	}
    	
    	return true;
    }
    
    //Password check
    public boolean isValidPassword(String password) {
 
    	if(password.length() < 8) {
    		return false;
    	}
    	
    	return true;
    }
    
    //Empty input check
    public boolean isNotEmpty() {
    	
 	   if(username.isBlank() || password.isBlank()) {
 		   return false;
 	   }
 	   
 	   return true;	   
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*IMPLEMENTAZIONE METODO DO POST
		 * Output writer - input reader
		 * Acquisizione valore campi del form
		 * manipolazione stringa del messaggio
		 */
		
		/* i controlli andranno organizzati cosi:
			 * se i seguenti controlli passano
			 * metodo controlloValiditaPassword()
			 * metodo controlloValiditaUsername()
			 * metodo ControlloVerificaMail()
			 * metodo controlloCampiVuoti()
			 * allora si potra passare al codice successivo
			 */
		
		//Controlli con database
	}

}
