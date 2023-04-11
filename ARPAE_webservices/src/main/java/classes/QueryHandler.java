package classes;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class QueryHandler {
	
	private static String db_url = "jdbc:mysql://localhost:3306/passaporti-vaccinali";
    private static String db_driver = "com.mysql.jdbc.Driver";
    private static String db_user = "root";
    private static String db_password = "";
    private Connection conn;
  

	public QueryHandler() {
		
		try {
			Class.forName(db_driver).newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void establishConnection() {
		
		try{
			conn = (Connection) DriverManager.getConnection(db_url, db_user, db_password); 
		}catch(SQLException e){
			System.err.println(e.getLocalizedMessage());
		}
		
	}
	
	public int hasUsername(String username) {
		
		establishConnection();
		String prepared_query = "SELECT * FROM utenti WHERE UT_username = ?";
		
		try(
			java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
			){
			
			pr.setString(1, username);
			ResultSet res = pr.executeQuery();
			//per controllare se un utente esiste basta vedere il risultato di next(), sarà false se non esistono righe
			boolean check = res.next();
			
			conn.close();
			return check ? 1 : 0; //se check true returna 1 altrimenti 0
		
		}catch(SQLException e){
			
			System.out.println(e.getLocalizedMessage());
			return -1;
		
		}
		
		
		
	}
	
	public int hasEmail(String email) {
		return -1;
	}
	
	public int inserisciUtente(String nome, String cognome, String username, String email, String password) {
		
		establishConnection();
		String prepared_query = "INSERT INTO soggetti (UT_nome, UT_cognome, UT_username, UT_email, UT_password) VALUES (?, ?, ?, ?, ?)";
		
		try(
				java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
				){
				
				pr.setString(1, nome);
				pr.setString(2, cognome);
				pr.setString(3, username);
				pr.setString(4, email);
				pr.setString(5, password);
				
				//executeUpdate returna o 1 se è andato a buonfine o 0 se non è andato a buonfine
				int check = pr.executeUpdate();
				
				conn.close();
				
				return check;
			
			}catch(SQLException e){
				
				System.out.println(e.getLocalizedMessage());
				return -1;
			
			}
		
	}
	
	public int updateVerification(short verification, String user_id) {
		
		establishConnection();
		String prepared_query = "UPDATE soggetti SET UT_verification = ? WHERE  UT_id = ?";
		
		try(
				java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
				){
				
				pr.setShort(1, verification);
				pr.setString(2, user_id);
				
				int check = pr.executeUpdate();
				
				conn.close();
				
				return check;
			
			}catch(SQLException e){
				
				System.out.println(e.getLocalizedMessage());
				return -1;
			
			}
		
		
		
	}
		
		
	
	

}
