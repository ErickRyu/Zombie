package Socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import Control.UserControl;

public class Server {
	static final int WAIT_TIME = 2500;			// 2.5sec
	static ServerSocket serverSocekt;
	static List<ConnectionToClient> clients;
	static UserControl userControl;
	static boolean isGameOver = true;
	static boolean isGameStart = false;

	Server() throws IOException {
		serverSocekt = new ServerSocket(5000);
		clients = new ArrayList<>();
		userControl = new UserControl();
	}

	public static void main(String[] args) {
		try {

			Server server = new Server();
			server.clientListener();
		} catch (Exception e) {
			try {
				Iterator<ConnectionToClient> itr = clients.iterator();
				while (itr.hasNext()) {
					itr.next().close();
				}
			} catch (Exception g) {
				g.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void clientListener() throws IOException {

		Socket socket = null;
		new waitForGo().start();
		while (!isGameStart) {
			socket = serverSocekt.accept();
			// Connect to client
			if (!isGameStart) {
				ConnectionToClient client = new ConnectionToClient(socket);
				// Init New User
				initUser(client);
				clients.add(client);
				System.out.println("[Info] client connected");
			}
		}
		while (!isGameOver) {
			;
		}
		serverSocekt.close();
	}

	private class waitForGo extends Thread {
		public void run() {
			String commandToGo = "";
			Scanner sc = new Scanner(System.in);
			while (!commandToGo.equals("GO")) {
				commandToGo = sc.next();
			}
			isGameStart = true;
			isGameOver = false;
			
			// Thread start
			Runnable r = new MessageHandler();
			Thread thread = new Thread(r);
			thread.start();
			sc.close();
		}
	}

	private void initUser(ConnectionToClient client) {
		try {
			String name = (String) client.read();
			double latitude = (double) client.read();
			double longitude = (double) client.read();
			userControl.initAndAddUser(name, latitude, longitude);
		} catch (IOException e) {
			System.out.println("IOExcpetion \n" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MessageHandler implements Runnable {
		// ConnectionToClient client;

		MessageHandler() {
			// this.client = client;
		}

		public void run() {
			try {
				String input = "";
				System.out.println("[Info] MessageHandler started");
				while (!isGameOver) {
					Thread.sleep(WAIT_TIME);
					for (ConnectionToClient client : clients) {
						System.out.println("[Info] Giving command to all clients");
						client.write("giveLocation");
						input = (String) client.read();
						System.out.println("Read : " + input);
						String[] nameAndLocation = input.split(":");
						String name = nameAndLocation[0];
						String[] Location = nameAndLocation[1].split(" ");
						double latitude = Double.parseDouble(Location[0]);
						double longitude = Double.parseDouble(Location[1]);
						userControl.setLatitude(name, latitude);
						userControl.setLongitude(name, longitude);
					}
					// Game play
					userControl.attack();

					tellEveryOne(userControl.getUserMap());
				}

			} catch (EOFException e) {
				System.out.println("EOFException\n" + e.getMessage());
			} catch (ArrayIndexOutOfBoundsException idxE) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void tellEveryOne(Object message) throws IOException {
		for (ConnectionToClient client : clients) {
			client.write(message);
			client.reset();
		}
	}

	private class ConnectionToClient {
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

		public Object read() throws Exception {
			return ois.readObject();
		}

		public void reset() throws IOException {
			oos.reset();
		}

		public void close() throws IOException {
			socket.close();
			ois.close();
			oos.close();
		}
	}
}
