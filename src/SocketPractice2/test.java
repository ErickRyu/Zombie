//package SocketPractice2;
//
//import java.util.HashMap;
//import java.util.Map.Entry;
//
//import Model.User;
//
//public class test {
//	public static void main(String[] args){
//		HashMap<String, User> userMap = new HashMap<>();
//		String name = "H";
//		User user = new User(name, "1 1", false);
//		userMap.put(name, user);
//		for(Entry<String, User> userEntry : userMap.entrySet()){
//			String location = userEntry.getValue().getLocation();
//			System.out.println(location);
//		}
//		userMap.get(name).setLocation("3 3");
//		for(Entry<String, User> userEntry : userMap.entrySet()){
//			String location = userEntry.getValue().getLocation();
//			System.out.println(location);
//		}
//	}
//}
