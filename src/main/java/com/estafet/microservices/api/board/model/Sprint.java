package com.estafet.microservices.api.board.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@SuppressWarnings("rawtypes")
	public List<Story> getStories() {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("projectId", projectId);
		params.put("sprintId", id);
		List objects = new RestTemplate().getForObject(
				System.getenv("STORY_API_SERVICE_URI") + "/project/{projectId}/stories?sprintId={sprintId}", List.class,
				params);
		List<Story> stories = new ArrayList<Story>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Story story = mapper.convertValue(object, new TypeReference<Story>() {
			});
			stories.add(story);
		}
		return stories;
	}

}
