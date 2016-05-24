package Control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import Model.User;

public class GameControl {
	static final double _VisibleDistance = 0.0006;
	static final double _AttackableDistance = 0.00015;
	static final int _AttackPower = 20;
	
	
	/**
	 * Attack with Double
	 */
	public void attack(ConcurrentHashMap<Integer, User> userMap) {

		for (User user1 : userMap.values()) {
			List<User> nearEnemiesList = new ArrayList<>();
			for (User user2 : userMap.values()) {
				boolean isSameUser = user1 == user2;
				boolean isThereDeadUser = user1.isDead() || user2.isDead(); 
				if(isSameUser || isThereDeadUser)
					continue;
				
				double user1Lat, user1Long, user2Lat, user2Long;
				user1Lat = user1.getLatitude();
				user1Long = user1.getLongitude();
				user2Lat = user2.getLatitude();
				user2Long = user2.getLongitude();
				
				double distance = Math.sqrt(Math.pow(user1Lat-user2Lat, 2) + Math.pow(user1Long-user2Long, 2));
				boolean isAttackable = distance < _AttackableDistance;
				boolean isVisible = distance < _VisibleDistance;
				
				
				if (isAttackable && user1.getisZombie() && !user2.getisZombie()) {
					user2.setHP(user2.getHP() - _AttackPower);
					if(user2.getHP() <= 0) {
						user1.addKill();
						user2.setDead();
						user2.addDeath();
						UserControl.mHumanNum--;
					}
					
				}
				if(isVisible && !user2.isDead()){
					nearEnemiesList.add(user2);
				}
				
			}
			user1.setNearEnemies(nearEnemiesList);
		}
	}
	
}
