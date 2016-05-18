package Model;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
	Map<Integer, String> userDatabase;
	String[] names = {"Eric", "SonDongjoo", "KimSangwoo", "ParkKeunHye", "Toyota", "Obama", "CaptinAmerica", "MaoMao", "Xhaomi", "SamsungMan", "Googler"};
	public UserDatabase(){
		userDatabase = new HashMap<>();
		int userId = 10000;
		for(String name : names){
			userDatabase.put(++userId, name);
		}
	}
	public String getUserName(int userId){
		return userDatabase.get(userId);
	}
	
}
