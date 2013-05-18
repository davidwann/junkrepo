package com.javacareerlab.clscrammer;

import java.util.LinkedList;
import java.util.Queue;

public class CityCatGroupProcessor implements Runnable {
	
	private Queue<CityCat> cityCats;
	
	/* ==================================================== */
	
	public CityCatGroupProcessor() {
		this.cityCats = new LinkedList<CityCat>();
	}
	
	/* ==================================================== */

	public void run() {
		
		CityCat cc;

		System.out.println("CityCatGroupProcessor Object is doing its thing in its own thread!");
		
		while (!this.cityCats.isEmpty()) {
			
			cc = this.cityCats.remove();
			cc.process();
		}
	}
	
	/* ==================================================== */
		
	public void addCityCatToQueue(CityCat cc) {
		this.cityCats.add(cc);
	}
	
	/* ==================================================== */
	
	public int getLength() {
		return this.cityCats.size();
	}
	
	/* ==================================================== */


	
	
	
	/* ==================================================== */

	
	
	
	
	/* ==================================================== */
	
}
