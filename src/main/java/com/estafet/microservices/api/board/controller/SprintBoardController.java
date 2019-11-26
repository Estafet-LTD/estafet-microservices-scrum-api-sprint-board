package com.estafet.microservices.api.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.board.model.SprintBoard;
import com.estafet.microservices.api.board.service.SprintBoardService;

import io.opentracing.Tracer;

@RestController
public class SprintBoardController {

	@Value("${app.version}")
	private String appVersion;
	
	@Autowired
	private SprintBoardService sprintBoardService;
	
	@Autowired
	private Tracer tracer;	

	@GetMapping("/api")
	public SprintBoard getAPI() {
		tracer.activeSpan().deactivate();
		return SprintBoard.getAPI(appVersion);
	}
	
	@GetMapping("/sprint/{sprintId}/board")
	public SprintBoard getSprintBoard(@PathVariable int sprintId) {
		return sprintBoardService.getSprintBoard(sprintId);
	}

	

}
