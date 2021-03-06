package com.excilys.cdb.dto;

public class ComputerDto extends Dto {
	private String name;
	private String introduction;
	private String discontinued;
	private String company;
	
	public ComputerDto(String id) {
		this(id,"",null,null,"-1");
	}
	
	public ComputerDto(String id, String name, String i, String d, String c) {
		super(id);
		this.setName(name);
		this.setIntroduction(i);
		this.setDiscontinued(d);
		this.setCompany(c);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String companyName) {
		this.company = companyName;
	}
	
	@Override
	public String toString() {
		return "Computer ["+this.getId()+"] " + this.getName() + " (" + this.getIntroduction() + ") (" + this.getDiscontinued() + ") " + this.getCompany();
	}
}