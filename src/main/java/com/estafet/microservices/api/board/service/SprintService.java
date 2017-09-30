package com.estafet.microservices.api.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.Sprint;
import com.estafet.microservices.api.board.model.SprintBoard;
import com.estafet.microservices.api.board.model.Story;

@Service
public class SprintService {

	@Autowired
	private SprintBoardService sprintBoardService;
	
	public void completeSprint(int sprintId) {
		SprintBoard board = sprintBoardService.getSprintBoard(sprintId);
		if (!board.isComplete()) {
			for (Integer storyId : board.getIncompleteStoryIds()) {
				new RestTemplate().postForEntity(System.getenv("STORY_API_SERVICE_URI") + "/remove-story-from-sprint",
						new Story().setId(storyId), Story.class);
			}
		}
		new RestTemplate().put(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{id}",
				new Sprint().setStatus("Completed"), sprintId);
	}
	
	public Sprint getSprint(int sprintId) {
		return new RestTemplate().getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{sprintId}",
				Sprint.class, sprintId);
	}

	@SuppressWarnings("unchecked")
	public String getLastSprintDay(int sprintId) {
		List<String> days = new RestTemplate().getForObject(
				System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{sprintId}/days", List.class, sprintId);
		return days.get(days.size() - 1);
	}

}
