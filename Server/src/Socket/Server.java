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
	static final int WAIT_TIME = 2800; // 2.8sec
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

		Runnable cmdRun = new commandThread();
		new Thread(cmdRun).start();

		while (!isGameStart) {
			socket = serverSocekt.accept();
			// Connect to client
			if (!isGameStart) {
				ConnectionToClient client = new ConnectionToClient(socket);
				// Init New User
				initUser(client);
			}
		}
		while (!isGameOver) {
			;
		}

		serverSocekt.close();
	}

	private class commandThread implements Runnable {
		public void run() {
			String command = "";
			Scanner sc = new Scanner(System.in);
			while (!command.equals("GO")) {
				command = sc.next();
			}
			try {
				for (ConnectionToClient client : clients) {
					client.write(command);
				}

				isGameStart = true;
				isGameOver = false;

				// Thread start
				Runnable r = new MessageHandler();
				Thread msgThread = new Thread(r);
				msgThread.start();
				while ((command = sc.next()).equals("EXIT")) {
					System.out.println("[Info] Exit occur");
					for (ConnectionToClient client : clients) {
						client.write(command);
					}
					msgThread.stop();
				}
			} catch (EOFException e) {
				System.out.println("EOFException : " + e);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException : " + e);
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initUser(ConnectionToClient client) {
		try {
			boolean matched = false;
			int userId = 0;
			System.out.println("client list size is : " + clients.size());
			userId = (int) client.read();
			System.out.println(userId);
			matched = userControl.isThereMatchedUser(userId);
			client.write(matched);
			
			if (matched) {
				System.out.println("[Info] client connected");
				clients.add(client);
				double latitude = 0;
				double longitude = 0;
				System.out.println("login user id : (" + userId + ")");
				userControl.putUser(userControl.initUser(userId, latitude, longitude));
			}
		} catch (EOFException e) {
			System.out.println("EOFException : " + e);
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println("IOExcpetion \n" + e);
			e.printStackTrace();
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
				boolean firstSend = true;

				String input = "";
				System.out.println("[Info] MessageHandler started");
				while (!isGameOver) {
					if (firstSend)
						Thread.sleep(5000);
					else
						Thread.sleep(WAIT_TIME);

					for (ConnectionToClient client : clients) {
						if (userControl.humanNum == 0) {
							client.write("EXIT");
							isGameOver = true;
						} else {
							System.out.println("[Info] Giving command to all clients");
							client.write("giveLocation");
							System.out.println("[Info] Sended giveLocation command");
							input = (String) client.read();
							System.out.println("Read : " + input);
							String[] idAndLocation = input.split(":");
							int userId = Integer.parseInt(idAndLocation[0]);
							String[] Location = idAndLocation[1].split(" ");
							double latitude = Double.parseDouble(Location[0]);
							double longitude = Double.parseDouble(Location[1]);

							userControl.setLatitude(userId, latitude);
							userControl.setLongitude(userId, longitude);
						}
					}

					// Game play
					userControl.attack();

					tellEveryOne(userControl.getUserMap());
					userControl.printUserStat();
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
