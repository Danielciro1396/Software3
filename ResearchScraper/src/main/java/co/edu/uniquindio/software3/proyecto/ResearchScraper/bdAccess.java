package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bdAccess {
	public static void main (String args []) throws SQLException
	  {    

	    DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
	    Connection conn = DriverManager.getConnection
	          ("jdbc:oracle:thin:@localhost:1521:xe", "prueba", "prueba");
	    Statement stmt = conn.createStatement();
	    ResultSet rset = 
	              stmt.executeQuery("select * from cosas");
	    while (rset.next())
	         System.out.println (rset.getString(2));   // Print col 1
	    stmt.close();
	    

	  }
	
}
