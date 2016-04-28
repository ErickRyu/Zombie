package SocketPractice2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import Model.User;

public class clientTest {
	static Socket socket;
	static final String ip = "127.0.0.1";
	static final int port = 5000;
	static String name;
	clientTest() {
		try {
			socket = new Socket(ip, port);
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		try {

			new clientTest();
			ConnectionToServer server = new ConnectionToServer(socket);

			new MessageHandler(server).start();

			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

			String input = "";
			
			// Login
			System.out.print("Input name : ");
			name = keyboard.readLine();
			server.write(name);
			String initialLocation = "";
			System.out.println("Input initial location : ");
			initialLocation = keyboard.readLine();
			server.write(initialLocation);
			
			System.out.println("[Info] Connection complete");
			while ((input = keyboard.readLine()) != null) {
				server.write(name + ":" + input);
			}
			socket.close();

			keyboard.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private static class MessageHandler extends Thread {
		static ConnectionToServer server;

		MessageHandler(ConnectionToServer server) {
			this.server = server;
		}
		ConcurrentHashMap<String, User> cHashMap;

		public void run() {
			ConcurrentHashMap<String, String> strtable = new ConcurrentHashMap<>();
			ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();
			try {
				while((userMap = (ConcurrentHashMap<String, User>)server.read())!=null){
					for(Entry<String, User> user : userMap.entrySet()){
						String name = user.getKey();
						String location = user.getValue().getLocation();
						System.out.println(name + ":" + location);
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
