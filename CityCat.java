package com.javacareerlab.clscrammer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class CityCat {

	private Connection conn;
	private Statement statement;
	
	private String city;
	private String category;
	private String citySector;
	private String website;
	private String websiteType;
	private String spam;

	private int pageDepth;

	private List<String> adListPageUrls;
	
	/* ==================================================== */
	
	public CityCat(String city, 
			       String category, 
			       String citySector, 
			       String website, 
			       String websiteType, 
			       String spam, 
			       int pageDepth) {

		this.conn = null;
		
		this.city = city;
		this.category = category;
		this.citySector = citySector;
		this.website = website;
		this.websiteType = websiteType;
		this.spam = spam;
		this.pageDepth = pageDepth;
		
		this.adListPageUrls = new ArrayList<String>();
		
		this.setAdListPageUrls();
	}
	
	/* ==================================================== */	

	private void setAdListPageUrls() {
		
		String idx;
		String url;

		this.adListPageUrls.clear();
		
		for (int i = 0; i < this.pageDepth; i++) {
			
			if (i == 0)
				idx = "";
			else
				idx = "index" + i + "00.html";
			
			if (this.citySector.equalsIgnoreCase("xxx"))
				url = "http://" + this.city + 
			          ".craigslist.org/" + this.category + "/" + idx;
			else
				url = "http://" + this.city + 
				      ".craigslist.org/" + this.citySector + 
				      "/" + this.category + "/" + idx;
			
			this.adListPageUrls.add(url);
		}
	}   // end of method setAdListPageUrls()

	/* ==================================================== */	

	public String getCity() {
		return this.city;
	}
	
	/* ==================================================== */	

	public String getCategory() {
		return this.category;
	}
	
	/* ==================================================== */	

	public void displayCityCat() {
		
		System.out.println("City         :  " + this.city);
		System.out.println("Category     :  " + this.category);
		System.out.println("City Sector  :  " + this.citySector);
		System.out.println("Website      :  " + this.website);
		System.out.println("Website Type :  " + this.websiteType);
		System.out.println("Spam         :  " + this.spam);
		System.out.println("Page Depth   :  " + this.pageDepth);
	}
	
	/* ==================================================== */	

	public void process() {

		// find email addresses

		// connect to a database
		try {
			conn = DriverManager.getConnection("jdbc:derby:SpamDB");
			statement = conn.createStatement();

			WebPage page = new WebPage();

			for (String url : this.adListPageUrls) {
				
				page.setUrl(url);

				// System.out.println("Number of Sublinks: " + page.getSubLinks().size());
				
				if (page.getSubLinks().size() == 0) {
					break;
				}
				else {
					
					// System.out.println(" ");
					// page.displayInfo();
					
					this.processAdLinks(page.getSubLinks());
					
				}
			}
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
	
	}   // end of method process()

	/* ==================================================== */	

	private void processAdLinks(List<String> adLinks) {
		
		WebPage page = new WebPage();
		
		String sql;
		String adDate;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (String adLink : adLinks) {
			
			page.setUrl(adLink);

			if (page.getEmailAddresses().size() == 0) {
				// System.out.println("No email addresses found.");
			}
			else {
				
				adDate = sdf.format(page.getAdDate());
				
				for (String email : page.getEmailAddresses()) {
					
					// System.out.println("Email Address: " + email);
					
					if (!email.endsWith("craigslist.org")) {
						
						// System.out.println("Email Address: " + email);
						
						// jam email into database
						
						sql = "INSERT INTO EmailAddresses             " + 
						      "   ( email,                            " +
							  "     ad_url,                           " +
							  "     ad_date,                          " +
							  "     contacted,                        " +
							  "     city,                             " +
							  "     city_sector,                      " +
							  "     cl_ad_category,                   " +
							  "     website,                          " +
							  "     website_type )                    " + 
						      "   VALUES ('" + email + "',            " + 
							  "           '" + adLink + "',           " + 
							  "           '" + adDate + "',           " + 
							  "           FALSE,                      " + 
							  "           '" + this.city + "',        " + 
							  "           '" + this.citySector + "',  " + 
							  "           '" + this.category + "',    " + 
							  "           '" + this.website + "',     " + 
							  "           '" + this.websiteType + "') ";

						// System.out.println(sql);

						try {
							statement.executeUpdate(sql);
						} 
						catch (SQLException e) {

							if (e.getErrorCode() == 30000) {
								// do nothing - just a dupe insert error
							}
							else {
								
								System.out.println(" ");
								System.out.println("SQL Insert Error - Error Code : " + e.getErrorCode());
								System.out.println("SQL Insert Error - Message    : " + e.getMessage());
								System.out.println(" ");
								e.printStackTrace();
								System.out.println(" ");
							}
						}
					}
				}
			}
		}
	}   // end of method processAdLinks()
	
	/* ==================================================== */	

	
	
	
	
	/* ==================================================== */	

	
	
	
	
	/* ==================================================== */	

	
}
