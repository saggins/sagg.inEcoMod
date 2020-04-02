package goodEconomy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import goodEconomy.GoodEconomy;

public class Database {
	private static GoodEconomy ins;
	
	private Connection connection;
	private PreparedStatement stmt = null;
    private String host, database, username, password;
    private int port;
    
    private String tablename;
	
	public void init() {
		ins = GoodEconomy.getInstance();
		tablename = "sagginecon";
		
		host = ins.getConfig().getString("DB-HOST");
		database = ins.getConfig().getString("DB-NAME");
		username = ins.getConfig().getString("DB-USER");
		password = ins.getConfig().getString("DB-PASS");
		port = Integer.parseInt(ins.getConfig().getString("DB-PORT"));
		
		 try {    
            openConnection();
        } catch (ClassNotFoundException e) {
            throwSQLError();
            e.printStackTrace();
        } catch (SQLException e) {
            throwSQLError();
            e.printStackTrace();
        }
		try {
			checkTable();
		} catch (SQLException e) {
            throwSQLError();
			e.printStackTrace();
		}
	}
	private void throwSQLError() {
		ins.getLogger().severe("ERROR: SQL");
		Bukkit.getPluginManager().disablePlugin(ins);
	}

	private void openConnection() throws SQLException, ClassNotFoundException {
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
	    }
	}
	
	private void checkTable() throws SQLException {
        Statement statement = connection.createStatement(); 
		boolean result = statement.execute("SHOW TABLES LIKE ?;");
		
		if(!result) {
			 statement.executeUpdate("CREATE TABLE " + tablename + " (blocks TINYTEXT, price INT, sold BIGINT);");
		} 
		
		
	}
}
