package com.estafet.microservices.api.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.board.model.SprintBoard;
import com.estafet.microservices.api.board.service.SprintBoardService;

// changing
@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class SprintBoardController {

	@Autowired
	private SprintBoardService sprintBoardService;

	@GetMapping("/sprint/{sprintId}/board")
	public SprintBoard getSprintBoard(@PathVariable int sprintId) {
		return sprintBoardService.getSprintBoard(sprintId);
	}

	@PostMapping("/task/{id}/complete")
	public ResponseEntity completeTask(@PathVariable int id) {
		return new ResponseEntity(sprintBoardService.completeTask(id), HttpStatus.OK);
	}

	@PostMapping("/task/{id}/reopen")
	public ResponseEntity reopenTask(@PathVariable int id) {
		return new ResponseEntity(sprintBoardService.reopenTask(id), HttpStatus.OK);
	}

	@PostMapping("/task/{id}/claim")
	public ResponseEntity claimTask(@PathVariable int id) {
		return new ResponseEntity(sprintBoardService.claimTask(id), HttpStatus.OK);
	}

	@PostMapping("/task/{id}/updatehours")
	public ResponseEntity updateRemainingTaskHours(@PathVariable int id, @PathVariable Integer remainingHours) {
		return new ResponseEntity(sprintBoardService.updateRemainingTaskHours(id, remainingHours), HttpStatus.OK);
	}

}
