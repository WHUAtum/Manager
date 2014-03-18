package NetWork;

import java.net.*;

public class Manager {
	static InetAddress Register(){
		InetAddress address;
		try{
			ServerSocket Reg=new ServerSocket(18888);
			Socket Regreceiver=Reg.accept();	
			address= Regreceiver.getInetAddress();
			Regreceiver.close();
			return address;
		}
		catch(Exception e){
			System.out.println("Register error!!");
			return (InetAddress)new Object();
		}
		
		
	}
	public static void main(String[] args){
		
        
		try{
			//connect the Unity3D Server
			DBManager db=new DBManager("root", "" , "jdbc:mysql://localhost:3306");
			System.out.print("Press enter if the Server ready!");		
			System.in.read();
			Socket ss=new Socket("192.168.1.103",17000); 
	        System.out.println("Connect the Server successfully!");		
	        while(!db.connectdb()){
	        	 System.out.println("please check the database and press enter!");		
	        	 System.in.read();
	        }
	        ServerSocket s=new ServerSocket(16000);
	        //connect the devices
	        while(true){
				System.out.println("Waiting for Clients");
					
				Socket rs=s.accept();				
				System.out.println("Client connected");
			    //Create a new channel to for a device
				new Channel(ss,rs,db).start();			
			}
		}
		catch(Exception e){
			System.out.println("Can not Connect the server!");
			e.printStackTrace();
		}
	
		
	}
	
}
