package com.javacareerlab.clscrammer;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import com.javacareerlab.clscrammer.gui.GuiMaster;

public class Scrammer {
	
	public static void main(String[] args) {
		
		// DbLoader dbl = new DbLoader();
		// dbl.loadEmailAccounts();
		
		// launch the gui?  but how?
		
		GuiMaster.launchGui();	
		
		// set viz
		
		
		
		System.out.println("Scrammer main() is finished.");
	}

	/* ---------------------------------------------------------- */

	/**
	 * @param args
	 */
	public static void main2(String[] args) {

		// Harvester harvester = new Harvester();
		Emailer emailer = new Emailer();
		
		emailer.sendOneEmail();
		// emailer.displayEmailAccounts();
		

		// Thread harvesterThread = new Thread(harvester);
		// Thread emailerThread = new Thread(emailer);
		
		// harvesterThread.start();
		// emailerThread.start();
		
				
		// Thursday - 7:10am - Email Count = 13,348
		// Thursday - 6:00pm - Email Count = 19,282
		

		System.out.println("   ");
		System.out.println("Harvest and Email processes have started...");
		System.out.println("   ");

//		try {
//			// harvesterThread.join();
//			emailerThread.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		System.out.println("   ");
		System.out.println("Harvest and Email processes have finished.");
		System.out.println("   ");

		System.out.println("   ");
		System.out.println("Program is finished.");
		System.out.println("   ");
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	

	
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static void test002() {
		
		// Pattern p = Pattern.compile("fred");
		
		String s1 = Pattern.quote("foo bar");
		
		System.out.println("");
		System.out.println("s1:");
		System.out.println(s1);
		
		System.out.println("");
		System.out.println("Test 002 is complete.");
	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	private static void testWebPageClass() {
		
		WebPage wp = new WebPage();
		
		// wp.setUrl("http://memphis.craigslist.org/reo/");
		wp.setUrl("http://memphis.craigslist.org/reo/index200.html");
		// wp.setUrl("http://memphis.craigslist.org/reo/3085638564.html");
		// wp.setUrl("http://memphis.craigslist.org/reo/3075865633.html");
		// wp.setUrl("http://www.bluffcityfsbo.com");

		// - - - - - - - - - - - - - - - - - - - - - - - - -
		// spit out the object properties
		// - - - - - - - - - - - - - - - - - - - - - - - - -
		System.out.println("");
		System.out.println("URL:");
		System.out.println(wp.getUrl());
		
		System.out.println("");
		System.out.println("Page Type:");
		System.out.println(wp.getPageType());
		
		System.out.println("");
		System.out.println("Ad Date:");
		System.out.println(wp.getAdDate().toString());
		System.out.println((new SimpleDateFormat("MM/dd/yyyy")).format(wp.getAdDate())); 
		
		System.out.println("");
		System.out.println("Email Addresses:");
		
		for (String emailAddress : wp.getEmailAddresses())
			System.out.println(emailAddress);
		
		System.out.println("");
		System.out.println("Sublinks:");
		System.out.println("");
		
		for (String subLink : wp.getSubLinks()) {
			
			// System.out.println("");
			// System.out.println("One SubLink:");
			// System.out.println("- - -");
			System.out.println(subLink);
			// System.out.println("- - -");
		}
		
		// System.out.println("");
		// System.out.println("HTML:");
		// System.out.println(wp.getHtml());
		
		System.out.println("");
		System.out.println("Test is complete.");
		
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	

	
	
	
	
	
	
	

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
}
