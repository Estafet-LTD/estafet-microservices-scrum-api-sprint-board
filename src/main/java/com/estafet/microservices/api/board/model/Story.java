package com.estafet.microservices.api.board.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

	private int id;

	private Integer sprintId;

	private Integer projectId;

	private String title;

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public Integer getProjectId() {
		return projectId;
	}

}
