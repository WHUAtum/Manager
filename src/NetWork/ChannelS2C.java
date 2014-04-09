package NetWork;

import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class ChannelS2C extends Thread {
	public static byte[] buffer = new byte[1024];
	public static String table = "test.device";
	int receiveport, sendport;
	InetAddress saddr;
	// Socket ;
	Socket sendsocket, receivesocket;

	ChannelS2C(Socket ss, Socket rs) {
		sendsocket = ss;
		receivesocket = rs;
		
	}



	String[] CreateJson(String s) {


		String[] device = s.split("\t");
		String[] JSONs = new String[device.length];
		
		for (int i = 0; i < device.length; i++) {
			String[] d = device[i].split(" ");
			JSONObject fjson = new JSONObject();
			JSONObject t = new JSONObject();
			t.put("x", d[0]);
			t.put("y", d[1]);
			t.put("z", d[2]);
			fjson.put("pos", t);
			fjson.put("mac", "advanced"+i);
			fjson.put("res", "r");
			JSONs[i] = fjson.toString();
		}
		return JSONs;

	}

	public void run() {
		int t = 0;
		try {
			InputStream in = receivesocket.getInputStream();
			OutputStream out = sendsocket.getOutputStream();
			while (true) {
				int len = in.read(buffer);
				if (len != 0) {
					if (t == 0) {
						String s = new String(buffer).substring(0, len);
						System.out.println(s);

						String[] toMgr = CreateJson(s);
						for (int i = 0; i < toMgr.length; i++) {
							out.write(toMgr[i].getBytes());
							out.flush();
							System.out.println(toMgr[i]);
						}
					}
					t = (t + 1) % 4;
				}
			}
		} catch (Exception e) {
			System.out.println("channel error!");
			e.printStackTrace();
		}

	}

}
