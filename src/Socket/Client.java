package Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import Model.User;

public class Client {
	static Socket socket;
	static final String ip = "127.0.0.1";
	static final int port = 5000;
	static String name;
	static int userId;
	static boolean isFirstUser = false;
	static boolean isGameOver = true;

	Client() {
		try {
			socket = new Socket(ip, port);
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		try {
			Client client = new Client();
			ConnectionToServer server = new ConnectionToServer(socket);

			client.initUser(server);
			System.out.println("[Info] Connection complete");
			String commandToGo = "";

			new MessageHandler(server).start();
			// socket.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void initUser(ConnectionToServer server) throws Exception {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		String input = "";

		// Login
		boolean matched = false;
		while(!matched){
			System.out.print("Input id : ");
			userId = Integer.parseInt(keyboard.readLine());
			server.write(userId);
			matched = (boolean)server.read();
		}

		double latitude, longitude;
		System.out.println("Input initial latitude : ");
		latitude = Double.parseDouble(keyboard.readLine());
		server.write(latitude);

		System.out.println("Input initial longitude : ");
		longitude = Double.parseDouble(keyboard.readLine());
		server.write(longitude);
		keyboard.close();

	}

	public static double getLatitude() {
		return 0.3;
	}

	public static double getLongitude() {
		return 0.3;
	}

	private static class MessageHandler extends Thread {
		static ConnectionToServer server;

		MessageHandler(ConnectionToServer server) {
			this.server = server;
		}

		public void run() {
			ConcurrentHashMap<Integer, User> userMap = new ConcurrentHashMap<>();
			try {
				System.out.println("==============================");
				String input = "";
				while (true) {
					System.out.println("[Info] Watting command from server");
					while (!(input = (String) server.read()).equals("giveLocation")) {
						;
					}
					System.out.println("[Info] Got comman from server");
					double latitude = getLatitude();
					double longitude = getLongitude();
					server.write(userId + ":" + latitude + " " + longitude);

					System.out.println("\n");
					System.out.println("*********************************");
					userMap = (ConcurrentHashMap<Integer, User>) server.read();
					for (User user : userMap.values()) {
						String name = user.getUserName();
						latitude = user.getLatitude();
						longitude = user.getLongitude();
						int hp = user.getHP();
						boolean isZombie = user.getisZombie();

						System.out.println("-------------------\n" + name);
						System.out.println("(" + latitude + ", " + longitude + ")");
						System.out.println(isZombie ? "Zombie" : "Person");
						System.out.println("hp : " + hp);
					}
					userMap.clear();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static class ConnectionToServer {
		static Socket socket;
		static ObjectInputStream ois;
		static ObjectOutputStream oos;

		ConnectionToServer(Socket socket) {
			try {
				this.socket = socket;
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void write(Object message) throws Exception {
			oos.writeUnshared(message);
			oos.flush();
		}

		public Object read() throws Exception {
			return ois.readObject();
		}

		public void close() throws Exception {
			ois.close();
			oos.close();
		}

	}
}
