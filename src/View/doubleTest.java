package View;

public class doubleTest {
	static final double attackableDistance = 0.0001;
	public static void main(String[] args){
		double lat1, long1, lat2, long2;
		
		lat1 = 37.297521;
		long1 = 126.835771;
		lat2 = 37.297505;
		long2 = 126.835741;
		
		double distance = Math.sqrt(Math.pow(lat1-lat2, 2) + Math.pow(long1-long2, 2));
		boolean isAttackable = distance < attackableDistance;
		System.out.println(distance + " : " + isAttackable);
		
	}
}
