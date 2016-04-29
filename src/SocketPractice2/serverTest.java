package SocketPractice2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Control.UserControl;

public class serverTest {
	static ServerSocket server;
	static List<ConnectionToClient> clients;
	static UserControl userControl;
	
	

	serverTest() throws IOException {
		server = new ServerSocket(5000);
		clients = new ArrayList<>();
		userControl = new UserControl();
	}

	public static void main(String[] args) {
		try {
			new serverTest();
			Socket socket = null;
			while ((socket = server.accept()) != null) {
				// Connect to client
				ConnectionToClient client = new ConnectionToClient(socket);
				clients.add(client);
				
				System.out.println("[Info] client connected");
				
				// Init New User
				String name = (String) client.read();
				double latitude = (double)client.read();
				double longitude = (double)client.read();
				userControl.initAndAddUser(name, latitude, longitude);

				// Thread start
				new MessageHandler(client).start();
			}

			server.close();
		} catch (Exception e) {
			try {
				Iterator<ConnectionToClient> itr = clients.iterator();
				while (itr.hasNext()) {
					itr.next().close();
				}
			} catch (Exception g) {

			}
			e.printStackTrace();
		}
	}

	private static class MessageHandler extends Thread {
		ConnectionToClient client;

		MessageHandler(ConnectionToClient client) {
			this.client = client;
		}

		public void run() {
			try {
				String input = "";
				
				while ((input = (String) client.read()) != null) {
					System.out.println("Read : " + input);
					String[] nameAndLocation = input.split(":");
					String name = nameAndLocation[0];
					String[] Location = nameAndLocation[1].split(" ");
					double latitude = Double.parseDouble(Location[0]);
					double longitude = Double.parseDouble(Location[1]);
					userControl.setLatitude(name, latitude);
					userControl.setLongitude(name, longitude);
					
					// Game play
					userControl.attack();

					tellEveryOne(userControl.getUserMap());
				}
			}catch(ArrayIndexOutOfBoundsException idxE){
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

	public static void tellEveryOne(Object message) throws IOException {
		for (ConnectionToClient client : clients) {
			client.write(message);
			client.reset();
		}
	}

	private static class ConnectionToClient {
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;

		ConnectionToClient(Socket socket) throws IOException {
			this.socket = socket;
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		}

		public void write(Object message) throws IOException {
			oos.writeUnshared(message);
			oos.flush();
		}

		public Object read() throws Exception {return ois.readObject();}

		public void reset() throws IOException {oos.reset();}

		public void close() throws IOException {
			socket.close();
			ois.close();
			oos.close();
		}
	}
}
