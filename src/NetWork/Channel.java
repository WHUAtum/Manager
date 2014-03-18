package NetWork;

import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class Channel extends Thread {
	public static byte[] buffer = new byte[1024];
	public static String table = "test.device";
	DBManager db;
	int receiveport, sendport;
	InetAddress saddr;
	// Socket ;
	Socket sendsocket, receivesocket;

	Channel(Socket ss, Socket rs, DBManager db) {
		sendsocket = ss;
		receivesocket = rs;
		sendport = 17000;
		receiveport = 16000;
		this.db = db;
	}

	String ConvertJson(String s) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(s);
			JSONObject oldjson = (JSONObject) obj;
			JSONObject newjson = new JSONObject();
			String res = oldjson.get("res").toString();
			String mac = oldjson.get("mac").toString();
			String pos = oldjson.get("pos").toString();
			System.out.println(res+" "+pos+" "+res=="r");
			System.out.println(res.contains("r"));
			if (res.contains("r")) {
				if (!db.isexist(mac, table)) {
					System.out.println("registering");
					db.add(mac , table);
					
				}
				else{
					res="m";
					System.out.println("mac exist,we guess you will move");

				}
			}
			if (res.contains("u") || res.contains("m")) {
				if (!db.isexist(mac, table)) {
						db.add(mac  , table);
						System.out.println("mac is not exist,we guess you will register");

						res = "r";
				}
				if(res.contains("u")){
						System.out.println("unregistering");

						db.del(mac,table);
						
				}
			}
			newjson.put("res", res.toString());
			newjson.put("mac", mac.toString());
			newjson.put("pos", oldjson.get(pos));
			return newjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return s;
		}

	}

	public void run() {

		try {
			InputStream in = receivesocket.getInputStream();
			OutputStream out = sendsocket.getOutputStream();
			while (true) {
				int len = in.read(buffer);
				if (len != 0) {
					String s = new String(buffer).substring(0, len);
					System.out.println(s);
					String to3du = ConvertJson(s);
					out.write(to3du.getBytes());
					out.flush();
					System.out.println(to3du);

				}
				Thread.sleep(1000);

			}
		} catch (Exception e) {
			System.out.println("channel error!");
		}

	}

}
