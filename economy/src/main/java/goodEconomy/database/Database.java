package goodEconomy.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import goodEconomy.GoodEconomy;
import me.vagdedes.mysql.database.MySQL;

public class Database {
	private static GoodEconomy ins;
	
	private static Connection connection;    
    private static String tablename;
	
	public void init() {
		ins = GoodEconomy.getInstance();
		tablename = "mcserver";
		try {
			checkTable();
		} catch (SQLException e) {
			e.printStackTrace();
			ins.getLogger().info("Error Table");

		}
	}
	
	// EXMPLE USE CASE
	// doListing (true , PRICE, "workbench", BLOCK ); Gets PRICE using BLOCK
	// doListing (true , BLOCK, 6969, PRICE ); Gets  BLOCK PRICE
	
	// doListing (false, BLOCK, 324, PRICE); Posts a block with the price of 324
	public static ResultSet getListing( Listing what, String option, Listing where)throws SQLException{
		connection = MySQL.getConnection();
        String yourquery ="SELECT * FROM "+ tablename +" WHERE " + where.toString() + " = ?;";
        PreparedStatement stmnt = connection.prepareStatement(yourquery);	
        stmnt.setString(1, option);
        return stmnt.executeQuery();

	}
	
	public static void updateListing(Listing what, String option, String block)throws SQLException {
		connection = MySQL.getConnection();
        String yourquery ="UPDATE " + tablename + " SET " + what.toString() + " = ? WHERE "+ Listing.BLOCK +" = ?;";
        PreparedStatement stmnt = connection.prepareStatement(yourquery);
        stmnt.setString(1, option);
        stmnt.setString(2, block);
        stmnt.executeUpdate();

	}	
	
	public static void addListing(Listing what, String option)throws SQLException {
			connection = MySQL.getConnection();
	        String yourquery ="INSERT INTO "+ tablename + " ("+ what.toString() +") VALUES (?);";
	        PreparedStatement stmnt = connection.prepareStatement(yourquery);
	        stmnt.setString(1, option);
	        stmnt.executeUpdate();

	}
    		 
	public static void addTransaction (String[] items) throws SQLException {
		connection = MySQL.getConnection();
        String yourquery ="INSERT INTO gdp (uuid, player, blocks, isprice, ammount) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmnt = connection.prepareStatement(yourquery);
        int pos = 1;
        for(String item : items) {
        	stmnt.setString(pos, item);
        	pos++;
        }
        stmnt.executeUpdate();
	}
	private void checkTable() throws SQLException {
		connection= MySQL.getConnection();

        Statement statement = connection.createStatement(); 
		ResultSet result = statement.executeQuery("SHOW TABLES LIKE \"" + tablename +"\";");
		if(!result.next() ) {
			 statement.execute("	");
			 ins.getLogger().info("CREATING TABLE");
		} 
		
		
	}
}
