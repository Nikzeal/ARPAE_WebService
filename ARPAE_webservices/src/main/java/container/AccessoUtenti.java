package container;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import classes.QueryHandler;

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
    public boolean isValidUsername() {
    	
    	if(username == null || username.contains(" ")) {
    		return false;
    	}
    	
    	return true;
    }
    
    //Password check
    public boolean isValidPassword() {
 
    	if(password.length() < 8) {
    		return false;
    	}
    	
    	return true;
    }
    
    //Empty input check
    public boolean isNotBlank() {
    	
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
		
			PrintWriter out = response.getWriter(); 
			//input reader
			BufferedReader in_body = request.getReader();
			//stringBuilder per costruire una stringa dal messaggio in formato json
			StringBuilder sb = new StringBuilder();
			String line;
			String body;
			
			while((line = in_body.readLine()) != null) {
				sb.append(line);
			}
			
			body = sb.toString();
			
			Gson g = new Gson();
			JsonObject user = g.fromJson(body, JsonObject.class);
			
			username = user.get("Username").getAsString();
			password = user.get("Password").getAsString();
		
		/* i controlli andranno organizzati cosi:
			 * se i seguenti controlli passano
			 * metodo controlloValiditaPassword()
			 * metodo controlloValiditaUsername()
			 * metodo controlloCampiVuoti()
			 * allora si potra passare al codice successivo
			 * poi verra controllata la verifica della email: metodo isVerified()
			 */
			
			if(isNotBlank() && isValidUsername() && isValidPassword()) {
				
				
				QueryHandler queryForThis = new QueryHandler();
				
				int hasUsername = queryForThis.hasUsername(username);
				
				int user_id = queryForThis.getUserId(username);
				
				switch(hasUsername) {
				
				case 1:
					
					int checkPass = queryForThis.checkPass(user_id, password);
					
					if(checkPass == 1) {
						
						int checkVerified = queryForThis.isVerified(user_id);
						
						if(checkVerified == 1) {
							
							risposta = "password corretta";
							
						}else if(checkVerified == 0) {
							
							risposta = "email non verificata";
							
						}else {
							risposta = "errore con il database (controllo verifica email)";
						}
						
						
					}else if (checkPass == 0){
						
						risposta = "password errata";
						
					}else {
						risposta = "errore con il database (controllo password)";
					}
					break;
					
				case 0:
					risposta = "utente inesistente";
					break;
					
				default:
					risposta = "errore del database (presenza username)";
					break;
				}
				
				
			}else {
				risposta = "errore nell'input";
			}
			
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "PUT,POST");
			//da trasformare in formato json
			/*
			 * le risposte in formato json conterranno:
			 * stati (verifica dell'email, andatura della richiesta, correttezza password, controlli sugli input...)
			 * descrizione
			 * eventuali dati
			 */
			out.println(risposta);
		
	}

	
	
	
	
	
	
	
	
}
