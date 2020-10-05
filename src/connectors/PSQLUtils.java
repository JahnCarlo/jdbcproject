package connectors;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class PSQLUtils {
	private String url = "";
	private String user = "";
	private String password = "";
	private String userSequenceName = "";
	private String sessionSequenceName = "";
	private String inventorySequenceName = "";
	
	public static void main(String[] args) {
		PSQLUtils psql = new PSQLUtils();
		
	}
	
	//Main Constructor
	public PSQLUtils() {
		initiate();
	}
	
	public void initiate() {
		Properties prop1 = new Properties();
		prop1.setFilePath("C:\\Users\\juanb\\Desktop\\Java Workspace\\recordsview\\src\\props\\properties");
		
		this.url = prop1.getProperty("db.url");
		this.password = prop1.getProperty("db.password");
		this.user = prop1.getProperty("db.username");
		this.userSequenceName = prop1.getProperty("db.usersequencename");
		this.sessionSequenceName = prop1.getProperty("db.sessionsequencename");
		this.inventorySequenceName = prop1.getProperty("db.inventorysequencename");
	}
	
	public ResultSet executeGetQuery(String query) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(this.url, this.user, this.password);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			return rs;
		} finally {
			
		}
	}
	
	public long logSession(Integer userid, String activity) {
		PSQLUtils psql = new PSQLUtils();
		psql.initiate();
		//Try to log these changes as Activity REGISTERED
		try {
			long id_sessiontable = psql.insertIntoSessionTableQuery(userid, activity);
			if (id_sessiontable == -1) {
				System.out.println("Could not insert session information in DB!");
				JOptionPane.showMessageDialog(null, "Error registering session in DB!", "Register Session Info Error", JOptionPane.ERROR_MESSAGE);
			} else {
				System.out.println("Inserted session information at row id " + id_sessiontable + " at session_log table");
			}
			return id_sessiontable;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public long insertIntoSessionTableQuery(Integer userid, String activity) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		PSQLUtils psql = new PSQLUtils();
		psql.initiate();
		
		String SQL = "INSERT INTO public.\"session_log\" VALUES (nextval('"+this.sessionSequenceName+"'),?,?)";
		try {
			conn = DriverManager.getConnection(this.url, this.user, this.password);
			ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, userid);
			ps.setString(2, activity);
			
			int affectedRows = ps.executeUpdate();
			long id = 0;
			
			if (affectedRows > 0) {
				try(ResultSet rs = ps.getGeneratedKeys()){
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("Could not insert "+ userid + ", " + activity +" in DB!");
				return -1;
			}
			return id;
		} finally {
			
		}
	}
	
	public long insertIntoInventoryTableQuery(String vin, String make, String year, String color, String price, String userid) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		String SQL = "INSERT INTO public.inventory VALUES (nextval('"+this.inventorySequenceName+"'),?,?,?,?,?,?)";
		try {
			conn = DriverManager.getConnection(this.url, this.user, this.password);
			ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, vin);
			ps.setString(2, make);
			ps.setString(3, year);
			ps.setString(4,  color);
			ps.setInt(5, Integer.parseInt(price));
			ps.setInt(6, Integer.parseInt(userid));
			
			int affectedRows = ps.executeUpdate();
			long id = 0;
			
			if (affectedRows > 0) {
				try(ResultSet rs = ps.getGeneratedKeys()){
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("Could not insert in DB!");
				return -1;
			}
			System.out.println("Inserted information at row id " + id + " at inventory table");
			return id;
		} finally {
			
		}
	}
	
	public long insertIntoUserTableQuery(String username, String password) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		String SQL = "INSERT INTO public.\"user\" VALUES (nextval('"+this.userSequenceName+"'),?,?)";
		try {
			conn = DriverManager.getConnection(this.url, this.user, this.password);
			ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, password);
			
			int affectedRows = ps.executeUpdate();
			long id = 0;
			
			if (affectedRows > 0) {
				try(ResultSet rs = ps.getGeneratedKeys()){
					if (rs.next()) {
						id = rs.getLong(1);
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("Could not insert in DB!");
				return -1;
			}
			System.out.println("Inserted information at row id " + id + " at user table");
			return id;
		} finally {
			
		}
	}
}
