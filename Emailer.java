package com.javacareerlab.clscrammer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
// import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer implements Runnable {

	private String sql;
	
	private long email_id;
	private String email_email;
	private String email_ad_url;
	private Date email_ad_date;
	private boolean email_contacted;
	private String email_city;
	private String email_city_sector;
	private String email_cl_ad_category;
	private String email_website;
	private String email_website_type;
	private Timestamp email_created;
	private Timestamp email_modified;
	private String email_region;
	
	private List<EmailAccount> emailAccounts;

	private int emailAccountIndex;
	
	/* ==================================================== */
	
	public Emailer() {

		this.loadEmailAccounts();
		this.emailAccountIndex = -1;
		
		this.setSql();
	}
	
	/* ==================================================== */
	
	@Override
	public void run() {

		// TODO Auto-generated method stub
		this.sendEmails();

	}

	/* ==================================================== */

	public void sendEmails() {
		
		Connection conn = null;
		Statement statement;
		ResultSet rs;
		
		java.util.Date threadStartDate;
		java.util.Date startDate;
		java.util.Date endDate;
		
		String modified;
		
		long milliseconds;
		
		long counter = 0;

		threadStartDate = new java.util.Date();

		int minimumTimeSpan = 24 * 60000;

		try {
			conn = DriverManager.getConnection("jdbc:derby:SpamDB");

			statement = conn.createStatement();

			while (true) {

				startDate = new java.util.Date();

				// - - - - - - - - - - - - - - - - - - - - - - - - -
				
				//between 0 (inclusive) and the specified value (exclusive), 
				// 0 - 10,001
				// add 32,000
				// will yield 32000 to 42000 milliseconds
				// int minimumTimeSpan = (new Random(startDate.getTime())).nextInt(10001) + 32000;

				// - - - - - - - - - - - - - - - - - - - - - - - - -

				// 3.5 minutes = 180 + 30 =     210 seconds
				//                          210,000 milliseconds

				// - - - - - - - - - - - - - - - - - - - - - - - - -
				
				// 7 minutes = 60 x 7 =      430 seconds
				//                       430,000 milliseconds

				// - - - - - - - - - - - - - - - - - - - - - - - - -
				
				// int minimumTimeSpan = 430000;

				// - - - - - - - - - - - - - - - - - - - - - - - - -
				
				//minutes = 
				//16 - (( count - 1 ) * 4)

				// int minimumTimeSpan =  (16 - ((this.emailAccounts.size() - 1) * 4)) * 60 * 1000;
				
				// minimumTimeSpan = 24 / this.emailAccounts.size() * 60000;
				minimumTimeSpan = (32 * 60000) / this.emailAccounts.size();

				// - - - - - - - - - - - - - - - - - - - - - - - - -
				
				rs = statement.executeQuery(this.sql);

				if (rs.next()) {
					
					this.email_id = rs.getLong("id");
					this.email_email = rs.getString("email");
					this.email_ad_url = rs.getString("ad_url");
					this.email_ad_date = rs.getDate("ad_date");
					this.email_contacted = rs.getBoolean("contacted");
					this.email_city = rs.getString("city");
					this.email_city_sector = rs.getString("city_sector");
					this.email_cl_ad_category = rs.getString("cl_ad_category");
					this.email_website = rs.getString("website");
					this.email_website_type = rs.getString("website_type");
					this.email_created = rs.getTimestamp("created");
					this.email_modified = rs.getTimestamp("modified");
					this.email_region = rs.getString("region");
					
					if (this.contactProspect()) {
						
						// System.out.println((new SimpleDateFormat("MM/dd/yyyy")).format(wp.getAdDate())); 
						// "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
						//  yyyy-MM-dd HH:mm:ss
						
						modified = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(startDate);
						
						statement.executeUpdate("UPDATE EmailAddresses " + 
						                        "   SET contacted = true, " + 
								                "       modified = '" + modified + "' " + 
								                "   WHERE id = " + this.email_id);
					}
					else {
						if (this.emailAccounts.size() == 0) {
							
							System.out.println(" ");
							System.out.println("Out of working email accounts!");
							System.out.println("Email process is terminating!");
							System.out.println("Test email accounts!");
							System.out.println(" ");
							break;
						}
					}
				}
				else {
					System.out.println(" ");
					System.out.println("rs.next() returned FALSE!");
					System.out.println("Verify we are out of uncontacted prospects!");
					System.out.println(" ");
					break;
				}
				
				// - - - - - - - - - - - - - - - - - - - - - - - - - 
				
				endDate = new java.util.Date();

				milliseconds = endDate.getTime() - startDate.getTime();
				
				if (milliseconds < minimumTimeSpan)
					Thread.sleep(minimumTimeSpan - milliseconds);

				// - - - - - - - - - - - - - - - - - - - - - - - - - 

				//1 minute = 60 seconds
				//1 minute = 60,000 milliseconds

				//1 hour = 60 x 60 seconds ( 3600 seconds )
				//1 hour = 3,600,000 milliseconds

				// 23 hours = 23 x 60 x 60 seconds ( 82,800 seconds )
				// 23 hours = 82,800,000 milliseconds

				// 10 minutes  =     600,000
				// 20 minutes  =   1,200,000
				//  1 hour     =   3,600,000
				// 10 hours    =  36,000,000
				
				// run for 5 hours  / 12:25 - 17:25 / 18,000,000
				// run for 23 hours / 18:50 - 17:50 / 82,800,000
				// run for 14 hours / 17:30 - 07:30 / 50,400,000 
				// run for  4 hours / 08:35 - 12:35 / 14,400,000
				// run for 10 hours / 02:45 - 12:45 / 36,000,000
				// run for 12 hours / 23:40 - 11:40 / 43,200,000
				// run for  9 hours / 13:30 - 22:30 / 32,400,000
				// run for 12 hours / 00:25 - 12:25 / 43,200,000
				// run for  2 hours / 14:15 - 16:15 /  7,200,000
				// run for  6 hours / 13:00 - 19:00 / 21,600,000
				// run for 22 hours / 19:30 - 17:30 / 79,200,000
				// run for 20 hours / 20:45 - 16:45 / 72,000,000
				// run for 21 hours / 20:25 - 17:25 / 75,600,000
				// run for 18 hours / 23:00 - 17:00 / 64,800,000
				
				
				
				
				//   1 HOUR * 60 MINUTES * 60 SECONDS * 1,000 
				//   ( 1 * 60 ) * 60 * 1,000
				//   
				
				// RUN FOR 9 HOURS
				if (endDate.getTime() - threadStartDate.getTime() > ((9 * 60) * 60 * 1000)) {
					
					break;
					
				}

				// - - - - - - - - - - - - - - - - - - - - - - - - - 

				counter++;
				if (counter >= 2) {
					
					// break;
					
				}

				// - - - - - - - - - - - - - - - - - - - - - - - - - 

			}
			
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(" ");
		System.out.println("Emailer has finished.");
		System.out.println(" ");
		
	}   // end of method sendEmails()
	
	/* ==================================================== */

	private boolean contactProspect() {

		boolean retval = true;

		String noreply_local_part; 
		String noreply_domain; 
		
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		
		// retval = false;
		
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		
		this.emailAccountIndex++;
		
		if (this.emailAccountIndex >= this.emailAccounts.size())
			this.emailAccountIndex = 0;

		EmailAccount ea = this.emailAccounts.get(this.emailAccountIndex);
		
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		if (ea.getNoreplyLocalPart().equalsIgnoreCase("x"))
			noreply_local_part = "noreply";
		else
			noreply_local_part = ea.getNoreplyLocalPart();
		
		if (ea.getNoreplyDomain().equalsIgnoreCase("x"))
			noreply_domain = this.email_website.toLowerCase();
		else
			noreply_domain = ea.getNoreplyDomain();

		// send the email
		
		Authenticator auth = new MyAuthenticator(ea.getUsername(), ea.getPassword());
		
		Properties props = new Properties();

		props.put("mail.smtp.host", ea.getSmtpHost());
		props.put("mail.smtp.port", ea.getSmtpPort());
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, auth);
		
		MimeMessage message = new MimeMessage(session);

		try {
			Address sender = new InternetAddress(noreply_local_part + "@" + noreply_domain, 
			                                     this.email_website);
			
			// Address recipTO = new InternetAddress("david@crestwoodcomputing.com");
			Address recipTO = new InternetAddress(this.email_email);
			
			// Address recipCC = new InternetAddress("david.wann@gmail.com");
			Address recipBCC = new InternetAddress("joecopy@crestwoodcomputing.com");
			
			// message.setText("Jackpot!\n\nToday is a good day!\n\nWhat should we do with all of our money?");
			// message.setSubject("Get ready to be happy");

			message.setSubject(this.setEmailSubject());
			message.setText(this.setEmailMessage());

			message.setFrom(sender);
			message.addRecipient(Message.RecipientType.TO, recipTO);
			// message.addRecipient(Message.RecipientType.CC, recipCC);
			message.addRecipient(Message.RecipientType.BCC, recipBCC);
			
			message.saveChanges();
			
			Transport.send(message);
		}	
		catch (MessagingException e) {
			// TODO Auto-generated catch block

//			switch (e.getMessage()) {
//			
//				case "Invalid Addresses":
//					retval = true;
//					break;
//				
//				case "Domain contains dot-dot":
//					retval = true;
//					break;
//					
//				default:
//					retval = false;
//					this.emailAccounts.remove(this.emailAccountIndex);
//					break;
//			}
			
			
			String errorMsg = e.getMessage();
			
			if (errorMsg.equalsIgnoreCase("Invalid Addresses")) {
				retval = true;
			}
			else {
				if (errorMsg.equalsIgnoreCase("Domain contains dot-dot")) {
					retval = true;
				}
				else {
					retval = false;
					this.emailAccounts.remove(this.emailAccountIndex);
				}
			}
			
//			if (e.getMessage().equalsIgnoreCase("Invalid Addresses")) {
//				retval = true;
//			}
//			else {
//				retval = false;
//				this.emailAccounts.remove(this.emailAccountIndex);
//			}

			System.out.println(" ");
			System.out.println("Email Error!");
			System.out.println(" ");
			System.out.println(e.getMessage());
			System.out.println(" ");

			this.displayProspectDetails();
			
			// ea.dis
			ea.displayEmailAccount();
			
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

			retval = false;
			
			e.printStackTrace();
		}
		
		// this.displayProspectDetails();
		
		return retval;
	}

	/* ==================================================== */

	private String setEmailSubject() {
		
		String retval;

		// - - - - - - - - - -
		// Possible Values:
		// - - - - - - - - - -
		// classifieds
		// homesfsbo
		// - - - - - - - - - -

		if (this.email_website_type.equalsIgnoreCase("homesfsbo")) {
			retval = this.email_website + " - FREE FSBO Listing Website";
		}
		else {
 			retval = this.email_website + " - FREE Online Classified Ads for " + this.email_region;
		}
		
		return retval;
	}
	
	/* ==================================================== */

	private String setEmailMessage() {

		String retval;
		
		// - - - - - - - - - -
		// Possible Values:
		// - - - - - - - - - -
		// classifieds
		// homesfsbo
		// - - - - - - - - - -

		if (this.email_website_type.equalsIgnoreCase("homesfsbo")) {

            retval = "Maximize your exposure and get more responses " + 
			         "by listing your FSBO property on " + this.email_website + ".\n\n" +
                     this.email_website + " is a *FREE* FSBO listing website " + 
			         "exclusively for " + this.email_region + ".\n\n" +
			         "8 Great Reasons to List Your Property on " + this.email_website + "...\n\n" +
					 "1. It's FREE!\n" + 
					 "2. Listing runs as long as you wish\n" +
					 "3. Straightforward website design makes it easy to list or search properties\n" +
					 "4. Listing entry form allows for very detailed and thorough property descriptions\n" +
					 "5. Upload up to 12 photos per listing\n" +
					 "6. Create, save, print, or email PDF flyers with your listing's photos and detail data\n" +
					 "7. Log in and modify your listing details and add/remove/reorder photos whenever you want\n" +
					 "8. Directly targets your primary market location\n\n" + 
					 "(Did we mention it was FREE?)\n\n" +
					 "Try it today.\n\n" +
					 "www." + this.email_website + "\n\n";
		}
		else {
			
			retval = "Maximize your exposure and get more responses by listing your ad on " +
			         this.email_website + ".\n\n" +
			         this.email_website + " is a *FREE* classified ads website exclusively " + 
			         "for " + this.email_region + ".\n\n" + 
			         "6 Great Reasons to List Your Ad on " + this.email_website + "...\n\n" +
			         "1. It's FREE!\n" +
			         "2. Straightforward website design makes it very easy to list or view ads\n" +
			         "3. Place up to 3 ads at a time\n" +
			         "4. Upload up to 4 photos per ad\n" +
			         "5. Log in and modify your ad and add/remove photos whenever you want\n" +
			         "6. Directly targets your primary market location\n\n" +
					 "(Did we mention it was FREE?)\n\n" +
			         "Try it today.\n\n" +
			         "www." + this.email_website + "\n\n";
		}
		
		return retval;
	}
	
	/* ==================================================== */
	
	public void sendOneEmail() {
		
		String[] pieces;
		String data;
		
		String noreply_domain;
		String noreply_local_part;
		
		System.out.println("Email being sent.");

		// ===================================================
		// Credentials
		// ===================================================

//		Username (email) : john-esparza@sbcglobal.net
//		Password         : bjbj1701

		data = "Y|emkay@citlink.net|llamas|smtp.frontier.com|25|noreply|citlink.net";

		pieces = data.split("\\|");
		
		Authenticator auth = new MyAuthenticator(pieces[1], pieces[2]);
		
		Properties props = new Properties();

		props.put("mail.smtp.host", pieces[3]);
		props.put("mail.smtp.port", pieces[4]);
		props.put("mail.smtp.auth", "true");

		noreply_local_part = pieces[5];
		noreply_domain = pieces[6];
		
		// ===================================================
		// 
		// ===================================================
		
		Session session = Session.getDefaultInstance(props, auth);
		
		MimeMessage message = new MimeMessage(session);

		try {
			// Address sender = new InternetAddress("noreply@" + noreply_domain, "FooBarFSBO.com");
			Address sender = new InternetAddress(noreply_local_part + "@" + noreply_domain, "FooBarFSBO.com");
			
			Address recipTO = new InternetAddress("david@crestwoodcomputing.com");
			Address recipCC = new InternetAddress("david.wann@gmail.com");

			message.setText("Jackpot!");
			message.setSubject("Get ready to be happy");

			message.setFrom(sender);
			message.addRecipient(Message.RecipientType.TO, recipTO);
			message.addRecipient(Message.RecipientType.CC, recipCC);
			
			message.saveChanges();
			
			Transport.send(message);
			
			// Transport transport = session.getTransport("smtp");
			
		}	
//		catch (AddressException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
		System.out.println("Email has been sent!");
		
	}   // end of method sendOneEmail()
	
	/* ==================================================== */
	
	public void sendEmailTest() {

		String noreply_domain;
		
		System.out.println("Email being sent.");

		// ===================================================
		// RICK and FAYE MARKS Credentials
		// ===================================================

//		Authenticator auth = new MyAuthenticator("bluefox3@windstream.net", "pizza4");
//		
//		Properties props = new Properties();
//
//		props.put("mail.smtp.host", "smtp.windstream.net");
//		props.put("mail.smtp.port", "25");
//		props.put("mail.smtp.auth", "true");
//
//		noreply_domain = "foobarfsbo.com";

		
		// ===================================================
		// Kristen Crouthamel Credentials
		// ===================================================

		Authenticator auth = new MyAuthenticator("kristen5613@q.com", "princess1978");
		
		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.q.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		noreply_domain = "q.com";
		

		// ===================================================
		// David Wann Credentials
		// ===================================================
		
//		Authenticator auth = new MyAuthenticator("david+crestwoodcomputing.com", "gohogs");
//		
//		Properties props = new Properties();
//
//		props.put("mail.smtp.host", "mail.crestwoodcomputing.com");
//		props.put("mail.smtp.port", "25");
//		props.put("mail.smtp.auth", "true");
//
//		noreply_domain = "foobarfsbo.com";
		
		
		// ===================================================
		// 
		// ===================================================
		
		Session session = Session.getDefaultInstance(props, auth);
		
		MimeMessage message = new MimeMessage(session);

		try {
			Address sender = new InternetAddress("noreply@" + noreply_domain, "FooBarFSBO.com");
			
			Address recipTO = new InternetAddress("david@crestwoodcomputing.com");
			Address recipCC = new InternetAddress("david.wann@gmail.com");

			message.setText("Jackpot!");
			message.setSubject("Get ready to be happy");

			message.setFrom(sender);
			message.addRecipient(Message.RecipientType.TO, recipTO);
			message.addRecipient(Message.RecipientType.CC, recipCC);
			
			message.saveChanges();
			
			Transport.send(message);
			
			// Transport transport = session.getTransport("smtp");
			
		}	
//		catch (AddressException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Email has been sent!");
		
	}

	/* ==================================================== */

	private void loadEmailAccounts() {
		
		BufferedReader br;
		String line;
		String[] pieces;

		this.emailAccounts = new ArrayList<EmailAccount>();

		// this.emailAccounts.add(new EmailAccount("", "", "", "", "", ""));
		
		// open text file
		try {
			br = new BufferedReader(new FileReader("EmailAccounts\\email_accounts.txt"));
			
			while ((line = br.readLine()) != null) {

				pieces = line.split("\\|");
				
				// System.out.println("pieces.length = " + pieces.length);

				if (pieces[0].equalsIgnoreCase("Y"))
					this.emailAccounts.add(new EmailAccount(pieces[1], 
				                                            pieces[2], 
				                                            pieces[3], 
				                                            pieces[4], 
				                                            pieces[5], 
				                                            pieces[6]));

				// this.emailAccounts.add(new EmailAccount("", "", "", "", "", ""));
			}

			br.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/* ==================================================== */

	private void setSql() {

//		this.sql = "SELECT a.id,               " +  
//                   "       a.email,             " +
//                   "       a.ad_url,             " +
//                   "       a.ad_date,             " +
//                   "       a.contacted,            " +
//                   "       a.city,                  " +
//                   "       a.city_sector,            " + 
//                   "       a.cl_ad_category,          " +
//                   "       a.website,                  " +
//                   "       a.website_type,              " +
//                   "       a.created,                    " +
//                   "       a.modified,                    " +
//                   "       b.region                        " +
//                   "   FROM EmailAddresses a                " + 
//                   "      JOIN WebsiteRegions b              " +
//                   "         ON b.website = a.website         " +
//                   "   WHERE a.website_type = 'homesfsbo' AND  " + 
//                   "         a.contacted = FALSE AND            " + 
//                   "         a.email NOT LIKE '%yahoo.com' AND   " +
//                   "         a.email NOT LIKE '%YAHOO.COM' AND   " +
//                   "         a.email NOT LIKE '%gmail.com' AND   " +
//                   "         a.email NOT LIKE '%GMAIL.COM' AND   " +
//                   "         a.email NOT LIKE '%aol.com' AND     " +
//                   "         a.email NOT LIKE '%AOL.COM' AND     " +
//                   "         a.email NOT LIKE '%msn.com' AND     " +
//                   "         a.email NOT LIKE '%MSN.COM' AND     " +
//                   "         a.email NOT LIKE '%hotmail.com' AND " +
//                   "         a.email NOT LIKE '%HOTMAIL.COM'     " +
//                   "   ORDER BY a.id                            " +
//                   "   FETCH FIRST 1 ROW ONLY                  ";
		
		//    ORDER BY a.created
		
		this.sql = "SELECT a.id,                 " +  
                   "       a.email,               " +
                   "       a.ad_url,               " +
                   "       a.ad_date,               " +
                   "       a.contacted,              " +
                   "       a.city,                    " +
                   "       a.city_sector,              " + 
                   "       a.cl_ad_category,            " +
                   "       a.website,                    " +
                   "       a.website_type,                " +
                   "       a.created,                      " +
                   "       a.modified,                      " +
                   "       b.region                          " +
                   "   FROM EmailAddresses a                  " + 
                   "      JOIN WebsiteRegions b                " +
                   "         ON b.website = a.website           " +
                   "   WHERE a.website_type = 'classifieds' AND " + 
                   "         a.contacted = FALSE                " + 
                   "   ORDER BY a.id                           " +
                   "   FETCH FIRST 1 ROW ONLY                 ";

	}
	
	/* ==================================================== */
	
	public void displayEmailAccounts() {
		
		for (EmailAccount ea : this.emailAccounts) {

			System.out.println(" ");

			System.out.println("Username           : " + ea.getUsername());
			System.out.println("Password           : " + ea.getPassword());
			System.out.println("Smtp Host          : " + ea.getSmtpHost());
			System.out.println("Smtp Port          : " + ea.getSmtpPort());
			System.out.println("Noreply Local Part : " + ea.getNoreplyLocalPart());
			System.out.println("Noreply Domain     : " + ea.getNoreplyDomain());

			System.out.println(" ");
		}
		
	}

	/* ==================================================== */
	
	public void displayProspectDetails() {

		System.out.println(" ");

		System.out.println("Id           : " + this.email_id);
		System.out.println("Email        : " + this.email_email);
		System.out.println("Ad Url       : " + this.email_ad_url);
		System.out.println("Ad Date      : " + this.email_ad_date);
		System.out.println("Contacted    : " + this.email_contacted);
		System.out.println("City         : " + this.email_city);
		System.out.println("City Sector  : " + this.email_city_sector);
		System.out.println("Ad Category  : " + this.email_cl_ad_category);
		System.out.println("Website      : " + this.email_website);
		System.out.println("Website Type : " + this.email_website_type);
		System.out.println("Created      : " + this.email_created);
		System.out.println("Modified     : " + this.email_modified);
		System.out.println("Region       : " + this.email_region);
		
		System.out.println(" ");
	}

	/* ==================================================== */
	
	
	
	

	/* ==================================================== */
	
	
	
	

	/* ==================================================== */
	
}
