package NetWork;

import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class ChannelS2C extends Thread {
	public static byte[] buffer = new byte[20480];
	public String remain="";
	public static String table = "test.device";
	int receiveport, sendport;
	InetAddress saddr;
	// Socket ;
	Socket sendsocket, receivesocket;

	ChannelS2C(Socket ss, Socket rs) {
		sendsocket = ss;
		receivesocket = rs;

	}

	String cutoff(String s) {//cut the string by frame
		String cutr, cuts[] = { "\t0 0", "\t0 1", "\t1 0", "\t1 1" };//0/1 0/1  format data is the end of a frame
		System.out.println("s: "+s.length() + " "+ s);
		int i, cutlen = 1000000, cutindex = -1, t[] = new int[4];
		for (i = 0; i < 4; i++) {//find the first frame of the string
			t[i]=s.indexOf(cuts[i]);
			if (t[i] == -1)
				continue;
			if (cutlen > t[i]) {
				cutlen = t[i];
				cutindex = i;
			}

		}
		System.out.println(cutindex);
		System.out.println(cutlen);

		if (cutindex != -1) {//if their is a frame exist
			cutr = s.substring(0, cutlen + 4);
			remain = s.substring(cutlen + 4, s.length() - 1);
		} else {
			remain = s;
			cutr = "false";
		}
		System.out.println("cutr: "+cutr);
		return cutr;
	}

	String[] CreateJson(String initstring) {
		initstring = remain + initstring;
		String s = cutoff(initstring);
		System.out.println("fs: "+s);
		
		if(s.contains("false")) return null;
		String[] device = s.split("\t");
		String[] JSONs = new String[device.length];
		
		for (int i = 0; i < s.length(); i++) {
			if(i<0) continue;
			else if(i>0) break;
			String[] d = device[i].split(" ");
			JSONObject fjson = new JSONObject();
			JSONObject t = new JSONObject();
			t.put("x", d[0]);
			t.put("y", d[1]);
			t.put("z", d[2]);
			fjson.put("pos", t);
			fjson.put("mac", "advanced" + i);
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
						System.out.println("init: "+s);

						String[] toMgr = CreateJson(s);
						System.out.println(toMgr==null);
						if(toMgr==null) continue;
						else System.out.println(toMgr.length);
						for (int i = 0; i < 1; i++) {
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
