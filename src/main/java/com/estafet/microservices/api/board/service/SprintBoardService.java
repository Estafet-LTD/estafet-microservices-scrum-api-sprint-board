package com.estafet.microservices.api.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.board.model.Sprint;
import com.estafet.microservices.api.board.model.SprintBoard;
import com.estafet.microservices.api.board.model.Story;
import com.estafet.microservices.api.board.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SprintBoardService {

	public void completeSprint(int sprintId) {
		SprintBoard board = getSprintBoard(sprintId);
		if (!board.isComplete()) {
			for (Integer storyId : board.getIncompleteStoryIds()) {
				new RestTemplate().postForEntity(System.getenv("STORY_API_SERVICE_URI") + "/remove-story-from-sprint",
						new Story().setId(storyId), Story.class);
			}
		}
		new RestTemplate().put(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{id}",
				new Sprint().setStatus("Completed"), sprintId);
	}

	public SprintBoard getSprintBoard(int sprintId) {
		Sprint sprint = getSprint(sprintId);
		return new SprintBoard().addStories(getSprintStories(sprintId)).setSprint(sprint);
	}

	@SuppressWarnings({ "rawtypes" })
	private List<Story> getSprintStories(int sprintId) {
		int projectId = new RestTemplate()
				.getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{id}", Sprint.class, sprintId)
				.getProjectId();

		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("projectId", projectId);
		params.put("sprintId", sprintId);
		List objects = new RestTemplate().getForObject(
				System.getenv("STORY_API_SERVICE_URI") + "/project/{projectId}/stories?sprintId={sprintId}", List.class,
				params);
		List<Story> stories = new ArrayList<Story>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Story story = mapper.convertValue(object, new TypeReference<Story>() {
			});
			stories.add(story);
		}
		return stories;
	}

	private Sprint getSprint(int sprintId) {
		return new RestTemplate().getForObject(System.getenv("SPRINT_API_SERVICE_URI") + "/sprint/{sprintId}",
				Sprint.class, sprintId);
	}

	private Task getTask(int taskId) {
		return new RestTemplate().getForObject(System.getenv("STORY_API_SERVICE_URI") + "/task/{id}", Task.class,
				taskId);
	}

	private Task saveTask(Task task) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", task.getId());
		template.put(System.getenv("STORY_API_SERVICE_URI") + "/task/{id}", task, params);
		return getTask(task.getId());
	}

	public Task completeTask(int taskId) {
		Task task = saveTask(getTask(taskId).complete());
		Story story = new RestTemplate().getForObject(System.getenv("STORY_API_SERVICE_URI") + "/task/{id}/story",
				Story.class, taskId);
		SprintBoard board = getSprintBoard(story.getSprintId());
		if (board.isComplete()) {
			completeSprint(story.getSprintId());
		}
		return task;
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

}
