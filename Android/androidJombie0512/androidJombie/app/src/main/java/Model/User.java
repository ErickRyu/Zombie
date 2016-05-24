package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    private static final long serialVersionUID = -505870546358096892L;

    private int mUserId;
    private String mUserName;
    private int mHp;
    private boolean mIsZombie;
    private boolean mIsDead;
    private double mLatitude;
    private double mLongitude;
    private int mKill;
    private int mDeath;

    private List<User> mNearEnemiesList;
    // ToDo
    public User(){}
    public User(int userId, String userName, double latitude, double longitude, boolean isZombie){
        this.mUserId = userId;
        this.mUserName = userName;
        this.mHp = 100;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mIsZombie = isZombie;
        this.mIsDead = false;
        mKill = 0;
        mDeath = 0;
        mNearEnemiesList = new ArrayList<>();
    }
    public int getUserId(){return mUserId;}

    public String getUserName(){return mUserName;}

    public int getHP(){return mHp;}
    public void setHP(int hp){this.mHp = hp;}

    public boolean getisZombie(){return mIsZombie;}

    public double getLatitude(){return mLatitude;}
    public double getLongitude(){return mLongitude;}
    public void setLatitude(double latitude){this.mLatitude = latitude;}
    public void setLongitude(double longitude){this.mLongitude = longitude;}

    public boolean isDead(){return mIsDead;}
    public void setDead(){mIsDead = true;}

    public List<User> getNearEnemies(){return mNearEnemiesList;}
    public void setNearEnemies(List<User> nearEnemies){this.mNearEnemiesList = nearEnemies;}

    public int getKill() {return mKill;}
    public void addKill() {mKill++;}

    public int getDeath() {return mDeath;}
    public void addDeath() {mDeath++;}

}