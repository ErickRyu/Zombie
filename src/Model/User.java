package Model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -505870546358096892L;
	
	private String userName;
	private int hp;
	private boolean isZombie;
	private String strLocation;
	// ToDo
	private List<User> enemyList;
	private Map<User, Integer> mostHitMeUser;
	
	public User(String userName, String location, boolean isZombie){
		this.userName = userName;
		this.hp = 100;
		this.strLocation = location;
		this.isZombie = isZombie;
	}	
	
	public String getUserName(){return userName;}
	
	public int getHP(){return hp;}
	public void setHP(int hp){this.hp = hp;} 

	public String getLocation(){return strLocation;}
	
	// ToDo setLocation when it changed enough distance
	public void setLocation(String location){this.strLocation = location;}
	
	public boolean getisZombie(){return isZombie;}
	
	
}
