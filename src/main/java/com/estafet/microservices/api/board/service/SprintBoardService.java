package com.estafet.microservices.api.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estafet.microservices.api.board.model.Sprint;
import com.estafet.microservices.api.board.model.SprintBoard;

@Service
public class SprintBoardService {

	@Autowired SprintService sprintService;

	public SprintBoard getSprintBoard(int sprintId) {
		Sprint sprint = sprintService.getSprint(sprintId);
		SprintBoard sprintBoard = new SprintBoard();
		return sprintBoard.addStories(sprint.getStories()).setSprint(sprint);
	}

}
