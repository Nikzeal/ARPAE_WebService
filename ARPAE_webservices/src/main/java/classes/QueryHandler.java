package classes;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class QueryHandler {
	
	private static String db_url = "jdbc:mysql://localhost:3306/utenti";
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
			//per controllare se l'username esiste basta vedere il risultato di next(), sarà false se non esistono righe
			boolean check = res.next();
			
			conn.close();
			return check ? 1 : 0; //se check true returna 1 altrimenti 0
		
		}catch(SQLException e){
			
			System.out.println(e.getLocalizedMessage());
			return -1;
		
		}
		
	}
	
	public int hasEmail(String email) {
		
		establishConnection();
		String prepared_query = "SELECT * FROM utenti WHERE UT_email = ?";
		
		try(
			java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
			){
			
			pr.setString(1, email);
			ResultSet res = pr.executeQuery();
			//per controllare se l'email esiste basta vedere il risultato di next(), sarà false se non esistono righe
			boolean check = res.next();
			
			conn.close();
			return check ? 1 : 0; //se check true returna 1 altrimenti 0
		
		}catch(SQLException e){
			
			System.out.println(e.getLocalizedMessage());
			return -1;
		
		}
	}
	
	
	public int checkPass(int user_id, String password) {
		
		establishConnection();
		String prepared_query = "SELECT UT_password FROM utenti WHERE UT_id = ?";
		
		try(
				
				java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
				
				){

			 
				pr.setInt(1, user_id);
				ResultSet res = pr.executeQuery();
				if(res.next()) {
					
					String pass = res.getString("UT_password");
					conn.close();
					
					if(password.equals(pass)){
						
						return 1;
						
					}else {
						return 0;
					}
					
				}else {
					conn.close();
					return -1;
				}
				
				
				

			}catch(SQLException e){
				
				System.out.println(e.getLocalizedMessage());
				return -1;
			
			}
	}
	
	public int getUserId(String username) {
		
		establishConnection();
		String prepared_query = "SELECT UT_id FROM utenti WHERE UT_username = ?";
		
		try(
			java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
			){
			
			pr.setString(1, username);
			ResultSet res = pr.executeQuery();
			if(res.next()) {
				int user_id = res.getInt("UT_id");
				conn.close();
				return user_id;
			}else {
				conn.close();
				return -1;
			}
			
			
			
		}catch(SQLException e){
			
			System.out.println(e.getLocalizedMessage());
			return -1;
		
		}
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
		String prepared_query = "UPDATE utenti SET UT_verification = ? WHERE  UT_id = ?";
		
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
	
	public int isVerified(int user_id) {
		
		establishConnection();
		String prepared_query = "SELECT UT_verification FROM utenti WHERE  UT_id = ?";
		
		try(
				java.sql.PreparedStatement pr = conn.prepareStatement(prepared_query);
				){
				
				
				pr.setInt(1, user_id);
				
				ResultSet res = pr.executeQuery();
				
				if(res.next()) {
					short verification = res.getShort("UT_verification");
					conn.close();
					return verification;
				}else {
					conn.close();
					return -1;
				}
				
			
			}catch(SQLException e){
				
				System.out.println(e.getLocalizedMessage());
				return -1;
			
			}
		
		
	}
		
		
	
	

}
