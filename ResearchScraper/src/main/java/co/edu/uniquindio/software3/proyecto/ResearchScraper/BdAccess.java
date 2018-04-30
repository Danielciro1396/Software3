package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BdAccess {
	
	public void conexion(String query) {
		try {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
		
	    Connection conn = DriverManager.getConnection
	          ("jdbc:oracle:thin:@localhost:1521:xe", "software3", "software3");
	    Statement stmt = conn.createStatement();
	    ResultSet rset =stmt.executeQuery(query);
 //while (rset.next())
//System.out.println (rset.getString(2));   // Print col 1
	    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
