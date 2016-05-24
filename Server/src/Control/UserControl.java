package Control;

import java.util.concurrent.ConcurrentHashMap;

import Model.User;
import Model.UserDatabase;

public class UserControl {
	
	private static final float _ZombiePossiblity = 0.3f;
	private static final int _MaxPlyaer = 7;
	private static final int _MaxZombie = 3;
	private static boolean mIsFirst = true;
	private static boolean mIsSecond = true;
	public static int mZombieNum = 0;
	public static int mHumanNum = 0;
	ConcurrentHashMap<Integer, User> mUserMap;
	UserDatabase mUserDB;
	GameControl mGameControl;
	public UserControl() {
		mUserMap = new ConcurrentHashMap<Integer, User>();
		mUserDB = new UserDatabase();
		mGameControl = new GameControl();
	}

	public User initUser(int userId, double latitude, double longitude) {
		boolean isZombie;
		/* Make first user zombie and second user human and others random */
		if(mIsFirst){
			isZombie = true;
			mIsFirst = false;
		}else if(mIsSecond){
			isZombie = false;
			mIsSecond = false;
		}else{
			isZombie = Math.random() > _ZombiePossiblity ? (mZombieNum < _MaxZombie ? true : false) : false;
		}
		
		if (isZombie)
			mZombieNum++;
		else
			mHumanNum++;
		
		String name = mUserDB.getUserName(userId);
		User user = new User(userId, name, latitude, longitude, isZombie);
		return user;
	}
	public void putUser(User user){
		mUserMap.put(user.getUserId(), user);
	}

	public void setLatitude(int userId, double latitude){
		if(mUserMap.get(userId) == null){
			System.out.println("userMap is null");
		}
		mUserMap.get(userId).setLatitude(latitude);
	}
	public void setLongitude(int userId, double longitude){
		mUserMap.get(userId).setLongitude(longitude);
	}

	public ConcurrentHashMap<Integer, User> getUserMap() {
		return mUserMap;
	}

	public boolean isThereMatchedUser(int userId){
		String name = mUserDB.getUserName(userId);
		return name!=null? true:false;
	}
	
	public void attack(){
		mGameControl.attack(mUserMap);
	}
	public void printUserStat(){
		for(User user : mUserMap.values()){
			System.out.println(user.getUserName());
			System.out.println(user.getHP());
			System.out.println(user.getisZombie()? "Zombie" : "Human");
			System.out.println(user.isDead()? "Alive" : "Dead");;
		}
	}
	public void decreaseHumanNum(){
		mHumanNum--;
	}
}