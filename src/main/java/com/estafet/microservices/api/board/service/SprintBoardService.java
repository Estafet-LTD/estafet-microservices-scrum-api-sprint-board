package com.estafet.microservices.api.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estafet.microservices.api.board.model.Sprint;
import com.estafet.microservices.api.board.model.SprintBoard;

@Service
public class SprintBoardService {

	@Autowired
	private SprintService sprintService;

	@Autowired
	private StoryService storyService;
	
	@Autowired
	private TaskService taskService;

	public SprintBoard getSprintBoard(int sprintId) {
		Sprint sprint = sprintService.getSprint(sprintId);
		SprintBoard sprintBoard = new SprintBoard(taskService);
		return sprintBoard.addStories(storyService.getStories(sprint)).setSprint(sprint);
	}

}
