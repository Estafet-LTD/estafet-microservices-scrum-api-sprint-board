package com.estafet.microservices.api.board.model;

public class AcceptanceCriterion {

	private int id;

	private String description;

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setId(int id) {
		this.id = id;
	}


	public AcceptanceCriterion setDescription(String description) {
		this.description = description;
		return this;
	}

}
