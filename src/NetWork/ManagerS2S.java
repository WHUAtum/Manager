package NetWork;

import java.net.*;

public class ManagerS2S {
	static InetAddress Register() {
		InetAddress address;
		try {
			ServerSocket Reg = new ServerSocket(18888);
			Socket Regreceiver = Reg.accept();
			address = Regreceiver.getInetAddress();
			Regreceiver.close();
			return address;
		} catch (Exception e) {
			System.out.println("Register error!!");
			return (InetAddress) new Object();
		}

	}

	public static void main(String[] args) {

		try {
			// connect the Unity3D Server
			DBManager db = new DBManager("root", "",
					"jdbc:mysql://localhost:3306");
			System.out.print("Press enter if the Unity3DServer ready!");
			System.in.read();
			Socket ss = new Socket("127.0.0.1", 17000);
			System.out.println("Connect the Server successfully!");
			while (!db.connectdb()) {
				System.out
						.println("please check the database and press enter!");
				System.in.read();
			}
			// connect the devices

			System.out.print("Press enter if the DemonServer ready!");
			System.in.read();
			Socket rs = new Socket("192.168.1.88", 7002);
			System.out.println("Client to manager successfully");
			// Create a new channel to for a device
			new ChannelS2S(ss, rs, db).start();

		} catch (Exception e) {
			System.out.println("Can not Connect the server!");
			e.printStackTrace();
		}

	}

}
