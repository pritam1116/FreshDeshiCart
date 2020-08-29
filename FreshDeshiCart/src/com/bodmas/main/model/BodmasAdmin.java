package com.bodmas.main.model;

public class BodmasAdmin {
	
	private int id;
	private String email;
	private String password;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public BodmasAdmin(int id, String email, String password) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}
	public BodmasAdmin(String email, String password) {

		this.email=email;
		this.password=password;
	}
	@Override
	public String toString() {
		return "BodmasAdmin [id=" + id + ", email=" + email + ", password=" + password + "]";
	}
	
	
}
