# Estafet Microservices Scrum Sprint Board Report
Microservices api retrieving information formatted for a sprint board.
## What is this?
This application is a microservice provides an API that aggregates sprint, story and tasks data to construct a sprint board when for a specific sprint.

Each microservice has it's own git repository, but there is a master git repository that contains links to all of the repositories [here](https://github.com/Estafet-LTD/estafet-microservices-scrum).
## Getting Started
You can find a detailed explanation of how to install this (and other microservices) [here](https://github.com/Estafet-LTD/estafet-microservices-scrum#getting-started).
## API Interface

### Sprint Board JSON object

```json
{
    "sprint": {
        "id": 2,
        "startDate": "2017-10-01 00:00:00",
        "endDate": "2017-10-04 00:00:00",
        "number": 2,
        "status": "Active",
        "projectId": 1,
        "stories": [
            {
                "id": 4,
                "sprintId": 2,
                "projectId": 1
            }
        ]
    },
    "todo": [
        {
            "id": 6,
            "title": "this is a task",
            "description": "some rubbish",
            "initialHours": 6,
            "storyId": 4,
            "remainingHours": 6,
            "status": "Not Started"
        }
    ],
    "inProgress": [
        {
            "id": 5,
            "title": "hghghg",
            "description": "jhjhjh",
            "initialHours": 10,
            "storyId": 4,
            "remainingHours": 10,
            "remainingUpdated": "2017-10-02 00:00:00",
            "status": "In Progress"
        }
    ],
    "completed": [
        {
            "id": 4,
            "title": "this is a task",
            "description": "testing",
            "initialHours": 12,
            "storyId": 4,
            "remainingHours": 0,
            "remainingUpdated": "2017-10-04 00:00:00",
            "status": "Completed"
        }
    ],
    "complete": false
}
```

### Restful Operations

To retrieve an example the sprint board object (useful for testing to see the microservice is online).

```
GET http://<sprint-board-microservice>/api
```

To retrieve a sprint board for a particular sprint

```
GET http://<sprint-board-microservice>/sprint/2/board
```

## Environment Variables
```
SPRINT_API_SERVICE_URI
STORY_API_SERVICE_URI
TASK_API_SERVICE_URI
```
