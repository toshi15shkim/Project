package com.roytuts.spring.activiti.integration.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class User {

	private String name;
	private long yearsOfExperience;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(long yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
