package com.estafet.microservices.api.board.model;

public class Sprint {

	private Integer id;

	private String startDate;

	private String endDate;

	private Integer number;

	private String status;

	private Integer projectId;

	public Integer getProjectId() {
		return projectId;
	}

	public String getStatus() {
		return status;
	}

	public Sprint setStatus(String status) {
		this.status = status;
		return this;
	}

	public Sprint setId(Integer id) {
		this.id = id;
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
