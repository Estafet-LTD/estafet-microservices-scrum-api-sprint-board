node("maven") {

	def project = "dev"
	def microservice = "sprint-board"
	
	currentBuild.description = "Build a container from the source, then execute unit and container integration tests before promoting the container as a release candidate for acceptance testing."

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-sprint-board"
	}

	stage("update wiremock") {
		sh "oc get pods --selector app=wiremock-docker -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc exec ${pod} -n ${project} -- /bin/sh -i -c \"rm -f /home/wiremock/mappings/*.json\""
		sh "oc rsync src/integration-test/resources/ ${pod}:/home/wiremock/mappings -n ${project}"
		openshiftDeploy namespace: project, depCfg: "wiremock-docker", showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "wiremock-docker", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}

	stage("build & deploy container") {
		openshiftBuild namespace: project, buildConfig: microservice, showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}

	stage("container tests") {
		try {
			withEnv(
				[ "SPRINT_BOARD_API_SERVICE_URI=http://${microservice}.${project}.svc:8080" ]) {
				sh "mvn verify -P integration-test"
			}
		} finally {
			junit "**/target/failsafe-reports/*.xml"
		}
	}
	
	stage("tag container for testing") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'latest', destinationNamespace: 'test', destinationStream: microservice, destinationTag: 'PrepareForTesting'
	}

}
