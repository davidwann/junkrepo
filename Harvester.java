package com.javacareerlab.clscrammer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.Queue;

public class Harvester implements Runnable {

	// constructor
	
	private Queue<CityCat> cityCats;
	
	private boolean isHarvesting;
	
	// private boolean stopHa
	
	/* ==================================================== */
	
	public Harvester() {
		this.isHarvesting = false;
		this.cityCats = new LinkedList<CityCat>();
	}
	
	/* ==================================================== */

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.startHarvesting();
	}
	
	/* ==================================================== */
	
	public void startHarvesting() {
		
		// and away we go!
		
		this.isHarvesting = true;

		// populate queue of city cats
		this.buildQueue();
		
		int threadCount = this.cityCats.size();
		
		if (threadCount > 10)
			threadCount = 10;
		
		// System.out.println("Thread Count: " + threadCount);
		
		// create an array of objects ( 0 to 10 elements )
		// each object will have a queue of city cats
		
		CityCatGroupProcessor[] ccgp = new CityCatGroupProcessor[threadCount];
		
		for (int i = 0; i < threadCount; i++)
			ccgp[i] = new CityCatGroupProcessor();

		// System.out.println("Number of elements in ccgp: " + ccgp.length);
		
		int idx = 0;
		
		while (!this.cityCats.isEmpty()) {
			if (idx >= threadCount)
				idx = 0;
			ccgp[idx].addCityCatToQueue(this.cityCats.remove());
			idx++;
		}

		Thread[] threads = new Thread[threadCount];

		idx = 0;

		for (CityCatGroupProcessor c : ccgp) {
			
			// System.out.println(c.getLength());
			
			threads[idx] = new Thread(c);
			threads[idx].start();
			
			idx++;
		}
		
		for (int i = 0; i < threadCount; i++)
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		this.isHarvesting = false;
		
		System.out.println(" ");
		System.out.println("Harvester has finished.");
		System.out.println(" ");
	}
	
	/* ==================================================== */
	
	public void stopHarvesting() {
		
		// let's shut her down!
		
		
		
		
	}
	
	/* ==================================================== */

	private void buildQueue() {
		
		Connection conn = null;
		Statement ps;
		String sql;

		this.cityCats.clear();
		
		try {
			conn = DriverManager.getConnection("jdbc:derby:SpamDB");

			ps = conn.createStatement();
			
			sql = "SELECT a.city,                            " +  
		          "       b.category,                        " +
		          "       a.city_sector,                     " +
		          "       a.website,                         " +
		          "       a.website_type,                    " + 
		          "       a.spam                             " +
		          "   FROM Cities a                          " +
		          "      JOIN Categories b                   " +
		          "         ON 1 = 1                         " +
		          "   WHERE a.spam = 'yes' AND               " +
		          "         a.website_type = 'homesfsbo' AND " + 
		          "         b.category = 'reo'               " +
		          "UNION                                     " +
		          "SELECT a.city,                            " +
		          "       b.category,                        " +
		          "       a.city_sector,                     " +
		          "       a.website,                         " +
		          "       a.website_type,                    " + 
		          "       a.spam                             " +
		          "   FROM Cities a                          " +
		          "      JOIN Categories b                   " +
		          "         ON 1 = 1                         " +
		          "   WHERE a.spam = 'yes' AND               " + 
		          "         a.website_type = 'classifieds'   "; 

//		sql = "SELECT a.city,                            " +  
//	          "       b.category,                        " +
//	          "       a.city_sector,                     " +
//	          "       a.website,                         " +
//	          "       a.website_type,                    " + 
//	          "       a.spam                             " +
//	          "   FROM Cities a                          " +
//	          "      JOIN Categories b                   " +
//	          "         ON 1 = 1                         " +
//	          "   WHERE a.spam = 'yes' AND               " +
//	          "         a.website_type = 'homesfsbo' AND " + 
//	          "         b.category = 'reo'               " + 
//			  "   FETCH NEXT 100 ROWS ONLY               ";
			
			ResultSet rs = ps.executeQuery(sql);
			
			while (rs.next())
				this.cityCats.add(new CityCat(rs.getString(1), 
                                              rs.getString(2), 
                                              rs.getString(3), 
                                              rs.getString(4), 
                                              rs.getString(5), 
					                          rs.getString(6), 200));
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}   // end of method buildQueue() 

	/* ==================================================== */
	
	
	

	/* ==================================================== */
	
}
