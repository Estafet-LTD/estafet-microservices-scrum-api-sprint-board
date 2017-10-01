package com.estafet.microservices.api.board.model;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

	private int id;

	private Integer sprintId;

	private Integer projectId;

	public int getId() {
		return id;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	public List<Task> getTasks() {
		return new RestTemplate().getForObject(System.getenv("TASK_API_SERVICE_URI") + "/story/{storyId}/tasks",
				List.class, id);
	}

}
