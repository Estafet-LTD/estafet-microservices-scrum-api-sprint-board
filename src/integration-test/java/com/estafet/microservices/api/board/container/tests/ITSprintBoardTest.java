package com.estafet.microservices.api.board.container.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;

public class ITSprintBoardTest {

	@Before
	public void before() throws Exception {
		RestAssured.baseURI = System.getenv("SPRINT_BOARD_API_SERVICE_URI");
	}

	@Test
	public void testGetAPI() {
		get("/api").then()
			.body("id", is(1));
	}

	@Test
	public void testGetSprintBoard() {
		get("sprint/1/board").then()
			.body("sprint.id", is(1))
			.body("sprint.startDate", is("2017-10-16 00:00:00"))
			.body("sprint.endDate", is("2017-10-20 00:00:00"))
			.body("sprint.number", is(1))
			.body("sprint.projectId", is(2))
			.body("todo.id", hasItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11, 12));
	}


}
