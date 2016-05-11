package Model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class User implements Serializable{
	private static final long serialVersionUID = -505870546358096892L;
	
	private String userName;
	private int hp;
	private boolean isZombie;
	private boolean isDead;
	private double latitude;
	private double longitude;
	// ToDo
	public User(){}
	public User(String userName, double latitude, double longitude, boolean isZombie){
		this.userName = userName;
		this.hp = 100;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isZombie = isZombie;
		this.isDead = false;
	}	
	public String getUserName(){return userName;}
	public int getHP(){return hp;}
	public void setHP(int hp){this.hp = hp;} 
	public boolean getisZombie(){return isZombie;}
	public double getLatitude(){return latitude;}
	public double getLongitude(){return longitude;}
	public void setLatitude(double latitude){this.latitude = latitude;}
	public void setLongitude(double longitude){this.longitude = longitude;}
	public boolean isDead(){return isDead;}
	public void setDead(){isDead = true;}
}
