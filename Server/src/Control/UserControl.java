package Control;

import java.util.concurrent.ConcurrentHashMap;

import Model.User;
import Model.UserDatabase;

public class UserControl {
	
	static final float zombiePossiblity = 0.3f;
	static final int _MaxPlyaer = 7;
	static final int _MaxZombie = 3;
	static boolean isFirst = true;
	public static int zombieNum = 0;
	public static int humanNum = 0;
	ConcurrentHashMap<Integer, User> userMap;
	UserDatabase userDB;
	GameControl gameControl;
	public UserControl() {
		userMap = new ConcurrentHashMap<Integer, User>();
		userDB = new UserDatabase();
		gameControl = new GameControl();
	}

	public User initUser(int userId, double latitude, double longitude) {
		/* init for test */
		boolean isZombie;
		if(isFirst){
//		 isZombie = Math.random() > zombiePossiblity ? (zombieNum < _MaxZombie ? true : false) : false;
			isZombie = true;
		 isFirst = false;
		}else{
			isZombie = false;
		}
		
		if (isZombie)
			zombieNum++;
		else
			humanNum++;
		
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
	public void printUserStat(){
		for(User user : userMap.values()){
			System.out.println(user.getUserName());
			System.out.println(user.getHP());
			System.out.println(user.getisZombie()? "Zombie" : "Human");
			System.out.println(user.isDead()? "Alive" : "Dead");
			
		}
	}
	public void decreaseHumanNum(){
		humanNum--;
	}
}