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
 * Servlet implementation class RegistrazioneUtenti
 */
@WebServlet("/RegistrazioneUtenti")
public class RegistrazioneUtenti extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String nome;
	String cognome;
	String nascita;
	String username;
	String email;
	String password;
	String key;
	short verification;
	String risposta;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrazioneUtenti() {
        super();
        // TODO Auto-generated constructor stub
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
		
		//output writer
		PrintWriter out = response.getWriter(); 
		//input reader
		BufferedReader in_body = request.getReader();
		//stringBuilder per costruire una stringa facendo vari append
		StringBuilder sb = new StringBuilder();
		String line;
		String body;
		
		//acquisisco la stringa dal body
		while((line = in_body.readLine()) != null) {
			sb.append(line);
		}
		
		body = sb.toString();
		//trsformazione stringa in oggetto json
		Gson g = new Gson();
		JsonObject persona = g.fromJson(body, JsonObject.class);
		//acquisizione valore delle chiavi
		nome = persona.get("Nome").getAsString();
		cognome = persona.get("Cognome").getAsString();
		username = persona.get("Username").getAsString();
		email = persona.get("Email").getAsString();
		password = persona.get("Password").getAsString();
		//pass_encr = password criptata;
		key = persona.get("key").getAsString();
		/*if(persona.get("Verification").getAsBoolean()) {
			verification = 1;
					
		}else {
			verification = 0 ;
		}*/
		
		QueryHandler queryForThis = new QueryHandler();
		
		int hasUsername = queryForThis.hasUsername(username);
		int hasEmail = queryForThis.hasEmail(email); //da fare
		
		switch(hasUsername) {
		
			case 1:
				risposta = "username già esistente";
				
				break;
			case 0:
				//da trovare un metodo migliore
				if(hasEmail != -1) {
					if(hasEmail == 0) {
					
						int inserted = queryForThis.inserisciUtente(nome, cognome, username, email, password);
						
						if(inserted != -1) {
							risposta = "utente registrato";
						}else {
							risposta = "errore del database (registrazione utente)";
						}
						
					}else {
						risposta = "email già esistente";
					}
						
				}else {
					risposta = "errore del database (presenza email)";
				}
				
				break;
				
			default:
				risposta = "errore del database (presenza username)";
				
				break;
		}
		
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "PUT,POST");
		//da trasformare in formato json
		out.println(risposta);
		
	}

}
