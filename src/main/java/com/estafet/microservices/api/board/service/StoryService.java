package com.estafet.microservices.api.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.Sprint;
import com.estafet.microservices.api.board.model.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StoryService {

	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("rawtypes")
	public List<Story> getStories(Sprint sprint) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("projectId", sprint.getProjectId());
		params.put("sprintId", sprint.getId());
		List objects = restTemplate.getForObject(
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
