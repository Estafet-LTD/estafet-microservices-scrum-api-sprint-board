package com.estafet.microservices.api.board.model;

public class Sprint {

	private Integer id;

	private String startDate;

	private String endDate;

	private Integer number;

	private String status;

	public String getStatus() {
		return status;
	}

	public Sprint setStatus(String status) {
		this.status = status;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Integer getNumber() {
		return number;
	}

}
