package com.estafet.microservices.api.board.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.estafet.microservices.api.board.service.TaskService;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SprintBoard {
	
	@JsonIgnore
	private TaskService taskService;

	private Sprint sprint;

	private List<Task> todo = new ArrayList<Task>();

	private List<Task> inProgress = new ArrayList<Task>();

	private List<Task> completed = new ArrayList<Task>();

	public SprintBoard(TaskService taskService) {
		this.taskService = taskService;
	}

	public SprintBoard addStories(List<Story> stories) {
		for (Story story : stories) {
			addStory(story);
		}
		return this;
	}

	public void addStory(Story story) {
		addTodoTasks(story);
		addInProgressTasks(story);
		addCompletedTasks(story);
	}

	private void addTodoTasks(Story story) {
		addTasks("Not Started", story, todo);
	}

	private void addInProgressTasks(Story story) {
		addTasks("In Progress", story, inProgress);
	}

	private void addCompletedTasks(Story story) {
		addTasks("Completed", story, completed);
	}

	private void addTasks(String status, Story story, List<Task> to) {
		for (Task fromTask : taskService.getTasks(story)) {
			fromTask.setStoryId(story.getId());
			if (fromTask.getStatus().equals(status)) {
				to.add(fromTask);
			}
		}
	}

	public Sprint getSprint() {
		return sprint;
	}

	public SprintBoard setSprint(Sprint sprint) {
		this.sprint = sprint;
		return this;
	}

	public List<Task> getTodo() {
		return todo;
	}

	public List<Task> getInProgress() {
		return inProgress;
	}

	public List<Task> getCompleted() {
		return completed;
	}
	
	public boolean isComplete() {
		return todo.isEmpty() && inProgress.isEmpty() && !completed.isEmpty();
	}
	
	@JsonIgnore
	public List<Task> getIncompletedTasks() {
		List<Task> incomplete = new ArrayList<Task>();
		incomplete.addAll(inProgress);
		incomplete.addAll(todo);
		return incomplete;
	}
	
	@JsonIgnore
	public Set<Integer> getIncompleteStoryIds() {
		Set<Integer> ids = new HashSet<Integer>();
		for (Task task : getIncompletedTasks()) {
			ids.add(task.getStoryId());
		}
		return ids;
	}

}
