package container;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import classes.QueryHandler;

/**
 * Servlet implementation class RegistrazioneUtenti
 */
@WebServlet("/RegistrazioneUtenti")
public class RegistrazioneUtenti extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	String nome;
	String cognome;
	String nascita;
	String username;
	String email;
	String password;
	String confirm_password;		//Daniele: aggiunto il campo conferma password
	String user_key;
	short verification;
	private String key;
	String risposta;
	static final String seed = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?!#&$";
	static SecureRandom rnd = new SecureRandom();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrazioneUtenti() {
        super();
        this.key = randomString(16);
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
               updateKey();
            }
        };

        // schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0l, 1000*60*60);
        // TODO Auto-generated constructor stub
    }
    
    public void updateKey(){
    	this.key = randomString(16);
    }
    
    public String randomString(int len) {
    	
    	StringBuilder sb = new StringBuilder(len);
    	
    	  for(int i = 0; i < len; i++) {
    	     sb.append(seed.charAt(rnd.nextInt(seed.length())));
    	   }
    	   return sb.toString();
    }
    //METODI CONTROLLO CAMPI
    //Username check
    public boolean isValidUsername(String username) {
    	
    	if((username == null) || username.contains(" ")) {
    		return false;
    	}else
    		return true;
    }
    //Email check
   public boolean isValidEmail(String email) {
    	
    	String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
    	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    	
    	if((email == null) || (email.matches(regexPattern) == false))
    		return false;
    	else
    		return true;
    }
    //Confirm password check
   public boolean isValidConfirmPassword() {
	   if(password != confirm_password)
		   return false;
	   else
		   return true;
   }
   //Empty input check
   public boolean isNotEmpty() {
	   if((nome == "" ) || (cognome == "" ) || (nascita == "" ) || (username == "" ) || (email == "" ) || (password == "" ) || (confirm_password == "" ) || (user_key == "" ))
		   return false;
	   else
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
		
		//output writer
		PrintWriter out = response.getWriter(); 
		//input reader
		BufferedReader in_body = request.getReader();
		//stringBuilder per costruire una stringa dal messaggio in formato json
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
		
		//Daniele: aggiunta la conferma password
		confirm_password = persona.get("Confirm_Password").getAsString();
		
		//pass_encr = password criptata;
		user_key = persona.get("User_key").getAsString();
		/*if(persona.get("Verification").getAsBoolean()) {
			verification = 1;
					
		}else {
			verification = 0 ;
		}*/
		/* 
		 * i controlli andranno organizzati cosi:
		 * se i seguenti controlli passano
		 * metodo controlloValiditaPassword()
		 * metodo controlloValiditaUsername()
		 * metodo ControlloValiditaEmail()
		 * metodo controlloCampiVuoti()
		 * allora si potra passare al codice successivo
		 */
		if(user_key == this.key){
			
			QueryHandler queryForThis = new QueryHandler();
			
			int hasUsername = queryForThis.hasUsername(username);
			int hasEmail = queryForThis.hasEmail(email); //da fare
			
			switch(hasUsername) {
			
				case 1:
					risposta = "username gi� esistente";
					break;
				case 0:
					//da trovare un metodo migliore
					if(hasEmail != -1) {
						if(hasEmail == 0) {
						
							int inserted = queryForThis.inserisciUtente(nome, cognome, username, email, password);
							
							if(inserted != -1) {
								risposta = "utente registrato";
							}else {
								risposta = "errore del database (inserimento utente)";
							}
						}else {
							risposta = "email gi� esistente";
						}
					}else {
						risposta = "errore del database (presenza email)";
					}
					break;
					
				default:
					risposta = "errore del database (presenza username)";
					break;
			}
		}else {
			risposta = "Non puo accedere al servizio di registrazione con questa chiave!";
		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "PUT,POST");
		//da trasformare in formato json
		out.println(risposta);
		
	}

}
