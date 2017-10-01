package com.estafet.microservices.api.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.board.model.SprintBoard;
import com.estafet.microservices.api.board.service.SprintBoardService;

@RestController
public class SprintBoardController {

	@Autowired
	private SprintBoardService sprintBoardService;

	@GetMapping("/sprint/{sprintId}/board")
	public SprintBoard getSprintBoard(@PathVariable int sprintId) {
		return sprintBoardService.getSprintBoard(sprintId);
	}

	

}
