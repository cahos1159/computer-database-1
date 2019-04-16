package com.excilys.cdb.model;

public class Company extends Model{
	private String name;
	
	public Company(int id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}