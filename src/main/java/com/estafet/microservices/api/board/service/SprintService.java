package com.estafet.microservices.api.board.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.Sprint;

@Service
public class SprintService {

	public Sprint getSprint(int sprintId) {
		return new RestTemplate().getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{sprintId}",
				Sprint.class, sprintId);
	}

}
