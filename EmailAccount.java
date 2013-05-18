package com.javacareerlab.clscrammer;

public class EmailAccount {

	private String username;
	private String password;
	private String smtpHost;
	private String smtpPort;
	private String noreplyLocalPart;
	private String noreplyDomain;
	
	public EmailAccount(String username,
	                    String password,
	                    String smtpHost,
	                    String smtpPort,
	                    String noreplyLocalPart,
	                    String noreplyDomain) {

		this.username = username;
		this.password = password;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.noreplyLocalPart = noreplyLocalPart;
		this.noreplyDomain = noreplyDomain;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

	public String getSmtpHost() {
		return this.smtpHost;
	}
	
	public String getSmtpPort() {
		return this.smtpPort;
	}

	public String getNoreplyLocalPart() {
		return this.noreplyLocalPart;
	}
	
	public String getNoreplyDomain() {
		return this.noreplyDomain;
	}

	public void displayEmailAccount() {
		
		System.out.println(" ");
		System.out.println("Username           : " + this.username);
		System.out.println("Password           : " + this.password);
		System.out.println("Smtp Host          : " + this.smtpHost);
		System.out.println("Smtp Port          : " + this.smtpPort);
		System.out.println("Noreply Local Part : " + this.noreplyLocalPart);
		System.out.println("Noreply Domain     : " + this.noreplyDomain);
		System.out.println(" ");
	}
	
}
