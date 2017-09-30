package com.estafet.microservices.api.board.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.SprintBoard;
import com.estafet.microservices.api.board.model.Story;
import com.estafet.microservices.api.board.model.Task;

@Service
public class TaskService {

	@Autowired 
	private SprintService sprintService;
	
	@Autowired
	private SprintBoardService sprintBoardService;
	
	public Task getTask(int taskId) {
		return new RestTemplate().getForObject(System.getenv("STORY_API_SERVICE_URI") + "/task/{id}", Task.class,
				taskId);
	}

	public Task saveTask(Task task) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", task.getId());
		template.put(System.getenv("STORY_API_SERVICE_URI") + "/task/{id}", task, params);
		return getTask(task.getId());
	}

	public Task reopenTask(int taskId) {
		return saveTask(getTask(taskId).reopen());
	}

	public Task claimTask(int taskId) {
		return saveTask(getTask(taskId).claim());
	}

	public Task updateRemainingTaskHours(int taskId, Integer remainingHours) {
		return saveTask(getTask(taskId).setRemainingHours(remainingHours));
	}

	public Task completeTask(int taskId) {
		Story story = new RestTemplate().getForObject(System.getenv("STORY_API_SERVICE_URI") + "/task/{id}/story",
				Story.class, taskId);
		String lastSprintDay = sprintService.getLastSprintDay(story.getSprintId());
		Task task = saveTask(getTask(taskId).complete(lastSprintDay));
		SprintBoard board = sprintBoardService.getSprintBoard(story.getSprintId());
		if (board.isComplete()) {
			sprintService.completeSprint(story.getSprintId());
		}
		return task;
	}

}
