package com.javacareerlab.clscrammer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbLoader {
	
	public void loadEmailAccounts() {
		
		String file = "EmailAccounts\\email_accounts.txt";
		String line;
		
		Connection con = null;
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));

			// con = DriverManager.getConnection("jdbc:derby:SpamDB;create=true");
			con = DriverManager.getConnection("jdbc:derby:SpamDB");

			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				// insert record into database table "email_accounts"
				this.insertEmailAccountRecord(con, line);
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException was thrown");
		}
		catch (IOException ex) {
			System.out.println("IOException was thrown");
		}
		catch (SQLException ex) {
			System.out.println("SQLException was thrown");
			System.out.println(ex.getMessage());
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException ex) {
					// do nothing
				}
			}
			
			// close database connection
			if (con != null) {
				
				try {
					con.close();
				}
				catch (SQLException ex) {
					// do nothing
				}

			}
			
		}

		System.out.println("Email Accounts have been loaded into db table email_accounts");
	}

	/* --------------------------------------------------------- */

	private void insertEmailAccountRecord(Connection con, String line) {
		
		String[] pieces;
		String sql;
		
		Statement statement = null;
		
		try {
			statement = con.createStatement();
			
			pieces = line.split("\\|");
			
			sql = "INSERT INTO email_accounts                         " + 
			      "      (email_address, smtp_server, port_number,    " + 
				  "       email_local_part, email_domain_part,        " + 
			      "       username, password, status, is_used, notes) " + 
				  "   VALUES ( '" + 
			      pieces[1] + "', '" + 
				  pieces[4] + "', " + 
			      pieces[5] + ", '" + 
				  pieces[6] + "', '" + 
			      pieces[7] + "', '" + 
				  pieces[2] + "', '" + 
			      pieces[3] + "', '" + 
				  "untested" + "', '" + 
			      pieces[0] + "', 'no notes')";
			
			statement.executeUpdate(sql);
		}
		catch (SQLException ex) {
			// do anything?
			System.out.println(" ");
			System.out.println("Insert failed!");
			System.out.println(ex.getMessage());
			System.out.println(" ");
		}
		finally {
			if (statement != null) {
				try {
					statement.close();
				}
				catch (SQLException ex) {
					// do nothing
				}
			}
		}
	}

	/* --------------------------------------------------------- */
	
	public void loadCategoriesTable() {
		
		Connection conn;
		Statement ps;
		
		BufferedReader br;
		String line;

		String[] pieces;
		
		String sql;
		
		try {
			// connect to a database
			conn = DriverManager.getConnection("jdbc:derby:SpamDB;create=true");
		
			ps = conn.createStatement();
			
			// open text file
			br = new BufferedReader(new FileReader("temp\\categories.txt"));
		
			while ((line = br.readLine()) != null) {
			
				if (line.length() > 5) {

					pieces = line.split("\\|");
					
					sql = "INSERT INTO Categories          " + 
					      "   ( category, category_desc,   " + 
						  "     parent_category )          " + 
					      "   VALUES ('" + pieces[0] + "', " + 
						  "           '" + pieces[1] + "', " + 
						  "           '" + pieces[2] + "') ";

					// System.out.println(sql);

					ps.executeUpdate(sql);
				}
			}
			
			br.close();
			conn.close();
		
		}
		catch (SQLException ex) {

			System.out.println("Error!");
			System.out.println(ex.getMessage());
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// do nothing - I know the file is there
			System.out.println("File not found");
		}
		catch (IOException e) {
			System.out.println("IOException occured!");
		}
		
	}   // end of method loadCategoriesTable()
	
	/* ==================================================== */

	public void loadCitiesTable() {
		
		Connection conn;
		Statement ps;
		
		BufferedReader br;
		String line;

		String[] pieces;
		
		String sql;
		
		try {
			// connect to a database
			conn = DriverManager.getConnection("jdbc:derby:SpamDB;create=true");
		
			ps = conn.createStatement();
			
			// open text file
			br = new BufferedReader(new FileReader("temp\\cities.txt"));
		
			while ((line = br.readLine()) != null) {
			
				if (line.length() > 5) {

					pieces = line.split("\\|");
					
					sql = "INSERT INTO Cities              " + 
					      "   (city, city_sector, website, " + 
						  "    website_type, spam, region) " + 
					      "   VALUES ('" + pieces[2] + "', " + 
						  "           '" + pieces[5] + "', " + 
					      "           '" + pieces[3] + "', " + 
						  "           '" + pieces[0] + "', " + 
					      "           '" + pieces[1] + "', " + 
						  "           '" + pieces[4] + "') ";

					// System.out.println(sql);

					ps.executeUpdate(sql);
				}
			}
			
			br.close();
			conn.close();
		
		}
		catch (SQLException ex) {

			System.out.println("Error!");
			System.out.println(ex.getMessage());
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// do nothing - I know the file is there
			System.out.println("File not found");
		}
		catch (IOException e) {
			System.out.println("IOException occured!");
		}
		
	}   // end of method loadCitiesTable()
	
	/* ==================================================== */
	
	// WebsiteRegions
	
	public void loadWebsiteRegionsTable() {
				
		Connection conn;
		Statement ps;
		String sql;
		
		try {
			// connect to a database
			conn = DriverManager.getConnection("jdbc:derby:SpamDB;create=true");
			ps = conn.createStatement();
			
			sql = "INSERT INTO WebsiteRegions      " + 
			      "   ( website, region )          " + 
			      "SELECT DISTINCT website, region " + 
				  "   FROM  Cities                 ";

			ps.executeUpdate(sql);
			
			conn.close();
		}
		catch (SQLException ex) {

			System.out.println("Error!");
			System.out.println(ex.getMessage());
		}
		
	}   // end of method loadWebsiteRegionsTable()
	
	/* ==================================================== */

	
	
	
	
	/* ==================================================== */

}
