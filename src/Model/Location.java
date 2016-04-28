package Model;

import java.io.Serializable;

public class Location implements Serializable{
	/**
	 * 
	 */
	String y;
	String x;
	public Location(String y, String x){
		this.y = y;
		this.x = x;
	}
	public String getLocationX(){return x;}
	public String getLocationY(){return y;}
	public void setLocationY(String y){this.y = y;}
	public void setLocationX(String x){this.x = x;}
}
