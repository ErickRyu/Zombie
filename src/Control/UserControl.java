package Control;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Model.User;

public class UserControl {
	static final double attackableDistance = 0.0001;
	static final float zombiePossiblity = 0.3f;
	static final int maxPlyaer = 7;
	static final int maxZombie = 3;
	static int zombieNum = 0;
	static ConcurrentHashMap<String, User> userMap;

	public UserControl() {
		userMap = new ConcurrentHashMap<String, User>();
	}

	public void printUser() {

	}

	public static void initAndAddUser(String name, double latitude, double longitude) {
		// ToDo change to getting from client initial Location
		boolean isZombie = Math.random() > zombiePossiblity ? (zombieNum < maxZombie ? true : false) : false;
		if (isZombie)
			zombieNum++;
		User user = new User(name, latitude, longitude, isZombie);
		userMap.put(name, user);
	}

	public void addUser(String name, User user) {
		userMap.put(name, user);
	}
	public void setLatitude(String name, double latitude){
		userMap.get(name).setLatitude(latitude);
	}
	public void setLongitude(String name, double longitude){
		userMap.get(name).setLongitude(longitude);
	}

	public ConcurrentHashMap<String, User> getUserMap() {
		return userMap;
	}

	public void updateUserMap() {

	}


	/**
	 * Attack with Double
	 */
	public void attack() {

		for (User user1 : userMap.values()) {
			for (User user2 : userMap.values()) {
				if (user1 == user2)
					continue;
				double user1Lat, user1Long, user2Lat, user2Long;
				user1Lat = user1.getLatitude();
				user1Long = user1.getLongitude();
				user2Lat = user2.getLatitude();
				user2Long = user2.getLongitude();
				
				double distance = Math.sqrt(Math.pow(user1Lat-user2Lat, 2) + Math.pow(user1Long-user2Long, 2));
				boolean isAttackable = distance < attackableDistance;
				System.out.println(distance + " : " + isAttackable);
				
				if (isAttackable && user1.getisZombie() && !user2.getisZombie()) {
					user2.setHP(user2.getHP() - 20);
				}
			}
		}
	}
}