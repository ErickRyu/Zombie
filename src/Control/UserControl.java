package Control;

import java.util.concurrent.ConcurrentHashMap;

import Model.User;
import Model.UserDatabase;

public class UserControl {
	
	static final float zombiePossiblity = 0.3f;
	static final int _MaxPlyaer = 7;
	static final int _MaxZombie = 3;
	
	static int zombieNum = 0;
	
	ConcurrentHashMap<Integer, User> userMap;
	UserDatabase userDB;
	GameControl gameControl;
	public UserControl() {
		userMap = new ConcurrentHashMap<Integer, User>();
		userDB = new UserDatabase();
		gameControl = new GameControl();
	}

	public User initUser(int userId, double latitude, double longitude) {
		// ToDo change to getting from client initial Location
		boolean isZombie = Math.random() > zombiePossiblity ? (zombieNum < _MaxZombie ? true : false) : false;
		if (isZombie)
			zombieNum++;
		String name = userDB.getUserName(userId);
		User user = new User(userId, name, latitude, longitude, isZombie);
		return user;
	}
	public void putUser(User user){
		userMap.put(user.getUserId(), user);
	}

	public void setLatitude(int userId, double latitude){
		if(userMap.get(userId) == null){
			System.out.println("userMap is null");
		}
		userMap.get(userId).setLatitude(latitude);
	}
	public void setLongitude(int userId, double longitude){
		userMap.get(userId).setLongitude(longitude);
	}

	public ConcurrentHashMap<Integer, User> getUserMap() {
		return userMap;
	}

	public boolean isThereMatchedUser(int userId){
		String name = userDB.getUserName(userId);
		return name!=null? true:false;
	}
	
	public void attack(){
		gameControl.attack(userMap);
	}
}