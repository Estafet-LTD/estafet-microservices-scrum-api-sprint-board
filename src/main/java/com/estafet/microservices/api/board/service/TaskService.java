package com.estafet.microservices.api.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.Story;
import com.estafet.microservices.api.board.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskService {

	@Autowired
	RestTemplate restTemplate;

	@SuppressWarnings({ "rawtypes" })
	public List<Task> getTasks(Story story) {
		List objects = restTemplate.getForObject(System.getenv("TASK_API_SERVICE_URI") + "/story/{storyId}/tasks",
				List.class, story.getId());
		List<Task> tasks = new ArrayList<Task>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Task task = mapper.convertValue(object, new TypeReference<Task>() {
			});
			tasks.add(task);
		}
		return tasks;
	}

}
