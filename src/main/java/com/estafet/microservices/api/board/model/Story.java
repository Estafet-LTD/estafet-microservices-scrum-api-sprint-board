package com.estafet.microservices.api.board.model;

import java.util.ArrayList;
import java.util.List;

public class Story {

	private int id;

	private String title;

	private String description;

	private Integer storypoints;

	private Integer sprintId;

	private Integer projectId;

	private List<Task> tasks = new ArrayList<Task>();

	private List<AcceptanceCriterion> criteria = new ArrayList<AcceptanceCriterion>();

	private String status;

	public List<AcceptanceCriterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<AcceptanceCriterion> criteria) {
		this.criteria = criteria;
	}

	public void setSprintId(Integer sprintId) {
		this.sprintId = sprintId;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStorypoints() {
		return storypoints;
	}

	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public Story setProjectId(Integer projectId) {
		this.projectId = projectId;
		return this;
	}

	public Story setId(int id) {
		this.id = id;
		return this;
	}

	public Story setTitle(String title) {
		this.title = title;
		return this;
	}

	public Story setDescription(String description) {
		this.description = description;
		return this;
	}

	public Story setStorypoints(Integer storypoints) {
		this.storypoints = storypoints;
		return this;
	}

}
