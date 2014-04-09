package NetWork;

import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class ChannelS2S extends Thread {
	public static byte[] buffer = new byte[10240];
	public static String table = "test.device";
	int t=0;
	DBManager db;
	int receiveport, sendport;
	InetAddress saddr;
	// Socket ;
	Socket sendsocket, receivesocket;

	ChannelS2S(Socket ss, Socket rs, DBManager db) {
		sendsocket = ss;
		receivesocket = rs;
		sendport = 17000;
		receiveport = 7002;
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
		
			if (!db.isexist(mac, table)) {
					System.out.println("registering");
					db.add(mac , table);
					res="r";
				}
			else{
					res="m";
					System.out.println("move");

			}
			
			newjson.put("res", res.toString());
			newjson.put("mac", mac.toString());
			newjson.put("pos", oldjson.get("pos"));
			return newjson.toString();

		}
		catch (Exception e) {
			e.printStackTrace();
			return s;
		}

	}
	String CreateJson(String s){
		
		JSONObject fjson = new JSONObject();
		
		String d[]=s.split(" ");
	
		JSONObject t = new JSONObject();
		t.put("x",d[0]);
		t.put("y", d[1]);
		t.put("z", d[2]);
		fjson.put("pos", t);
		fjson.put("mac","abcde");
		fjson.put("res", "r");
		return fjson.toString();
		
		
	}
	public void run() {

		try {
			InputStream in = receivesocket.getInputStream();
			OutputStream out = sendsocket.getOutputStream();
			while (true) {
				int len = in.read(buffer);
				if (len != 0) {
					String s2 = new String(buffer).substring(0, len);
					String s=CreateJson(s2);
					System.out.println(s);
					String to3du = ConvertJson(s);
					out.write(to3du.getBytes());
					out.flush();
					System.out.println(to3du);

				}
				Thread.sleep(1000);

			}
		} catch (Exception e) {
			System.out.println("ChannelS2S error!");
		}

	}

}
