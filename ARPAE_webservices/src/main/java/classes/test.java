package classes;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		JsonObject user = new JsonObject();
		
		user.addProperty("username", "pippo");
		user.addProperty("password", "123454");
		
		try {
			
			Map<String, String> claims = new HashMap<>();
			JwtGenerator generator = new JwtGenerator();
			
			
			user.keySet().forEach(keyStr ->
		    {
		        Object keyvalue = user.get(keyStr);
		        claims.put(keyvalue.toString(), keyStr);
		      
		    });
			
			String token = generator.generateJwt(claims);
			System.out.println(token);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		

	}

}
