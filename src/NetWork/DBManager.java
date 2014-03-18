package NetWork;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	public String USER;
	public String PASS;
	public String DB_URL;
	public Connection conn = null;
	public Statement stmt = null;

	
	public DBManager(String username, String password, String URL) {
		try {
			USER = username;
			PASS = password;
			DB_URL = URL;
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	
	public DBManager() {
		try {
			USER = "root";
			PASS = "";
			DB_URL = "jdbc:mysql://localhost:3306";
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public boolean connectdb() {

		try {
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connect to database successfully");
			stmt = conn.createStatement();
			return true;
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			return false;
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return false;
		}
	}

	

	public void add(String valus, String table) {
		String sql = "INSERT INTO " + table + " values(0,'" + valus + "')";
		System.out.println(sql);
		try {
			stmt.execute(sql);
			System.out.println("register successful!");
		} catch (Exception e) {
			System.out.println("register error!");
			e.printStackTrace();
		}
	}

	public void del(String mac,String table) {
		String sql="delete from "+table+" where mac='"+mac+"'";
		System.out.println(sql);
		try {
			stmt.execute(sql);
			System.out.println("unregister successful!");
		} catch (Exception e) {
			System.out.println("unregister error!");
			e.printStackTrace();
		}
	}
	
	
	public void end() throws Exception{
		
		conn.close();
		System.out.println("Goodbye!");
	}

	public boolean isexist(String mac, String table) {

		String sql = "SELECT " + mac + " FROM " + table + " order by id";
		System.out.println(sql);
		try {
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.wasNull())
				return true;
			else return false;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
		
	}

	public void update() {
		String sql;

	}
	public void addtable() {

	}
}
