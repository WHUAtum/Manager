package NetWork;

import java.net.*;

public class ManagerS2C {
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
			System.out.print("Press enter if the Server ready!");
			System.in.read();
			Socket rs = new Socket("127.0.0.1", 7002);
			System.out.println("Connect the Server successfully!");
		
			ServerSocket s = new ServerSocket(16000);
			// connect the devices

			System.out.println("Waiting for Clients");

			Socket ss = s.accept();
			System.out.println("Client connected");
			// Create a new channel to for a device
			new ChannelS2C(ss, rs).start();

		} catch (Exception e) {
			System.out.println("Can not Connect the server!");
			e.printStackTrace();
		}

	}

}
