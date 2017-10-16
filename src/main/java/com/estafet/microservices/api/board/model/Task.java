package com.estafet.microservices.api.board.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

	private Integer id;

	private String title;

	private String description;

	private Integer initialHours;

	private Integer storyId;

	@JsonInclude(Include.NON_NULL)
	private Integer remainingHours;

	@JsonInclude(Include.NON_NULL)
	private String remainingUpdated;

	private String status;

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getInitialHours() {
		return initialHours;
	}

	public Integer getRemainingHours() {
		return remainingHours;
	}

	public String getStatus() {
		return status;
	}

	public Integer getStoryId() {
		return storyId;
	}

	public void setStoryId(Integer storyId) {
		this.storyId = storyId;
	}

	public Task setTitle(String title) {
		this.title = title;
		return this;
	}

	public Task setDescription(String description) {
		this.description = description;
		return this;
	}

	public Task setInitialHours(Integer initialHours) {
		this.initialHours = initialHours;
		return this;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemainingUpdated() {
		return remainingUpdated;
	}

	public void setRemainingUpdated(String remainingUpdated) {
		this.remainingUpdated = remainingUpdated;
	}
	
	public static Task getAPI() {
		Task task = new Task();
		task.id = 1;
		task.description = "my task description";
		task.initialHours = 5;
		task.remainingHours = 5;
		task.remainingUpdated = "2017-10-16 00:00:00";
		task.status = "Not Started";
		task.title = "my task";
		task.storyId = 1;
		return task;
	}

}
