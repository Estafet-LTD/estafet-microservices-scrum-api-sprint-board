package com.estafet.microservices.api.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.Sprint;

@Service
public class SprintService {

	@Autowired
	private RestTemplate restTemplate;
	
	public Sprint getSprint(int sprintId) {
		return restTemplate.getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{sprintId}",
				Sprint.class, sprintId);
	}

}
