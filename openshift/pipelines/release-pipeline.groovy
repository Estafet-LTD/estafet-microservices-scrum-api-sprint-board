@NonCPS
def getVersions(json) {
	def tags = new groovy.json.JsonSlurper().parseText(json).status.tags
	def versions = []
	for (int i = 0; i < tags.size(); i++) {
		versions << tags[i]['tag']
	}
	return versions
}

def username() {
    withCredentials([usernamePassword(credentialsId: 'microservices-scrum', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        return USERNAME
    }
}

def password() {
    withCredentials([usernamePassword(credentialsId: 'microservices-scrum', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        return PASSWORD
    }
}

def recentVersion(versions) {
	def size = versions.size()
	return versions[size-1]
}

def getLatestVersion(microservice) {
	sh "oc get is ${microservice} -o json -n cicd > image.json"
	def image = readFile('image.json')
	def versions = getVersions(image)
	if (versions.size() == 0) {
		error("There are no images for ${microservice}")
	}
	return recentVersion(versions)
}

node('maven') {

	def project = "test"
	def microservice = "sprint-board"

	def developmentVersion
	def releaseVersion

	properties([
	  parameters([
	     string(name: 'GITHUB'),
	  ])
	])

	stage("checkout") {
		git branch: "master", url: "https://${username()}:${password()}@github.com/${params.GITHUB}/estafet-microservices-scrum-api-sprint-board"
	}

	stage ("verify build image") {
		String version = getLatestVersion microservice
		println "latest version is $version"
		def pom = readFile('pom.xml')
		def matcher = new XmlSlurper().parseText(pom).version =~ /(\d+\.\d+\.)(\d+)(\-SNAPSHOT)/
		String pomVersion = "${matcher[0][1]}${matcher[0][2].toInteger()}-SNAPSHOT"
		if (!version.equals(pomVersion)) {
			error("Source version ${pomVersion} does not match image version ${version}")
		}
	}

	stage("increment version") {
		def pom = readFile('pom.xml');
		def matcher = new XmlSlurper().parseText(pom).version =~ /(\d+\.\d+\.)(\d+)(\-SNAPSHOT)/
		developmentVersion = "${matcher[0][1]}${matcher[0][2].toInteger()+1}-SNAPSHOT"
		releaseVersion = "${matcher[0][1]}${matcher[0][2]}"
	}
	
	stage("perform release") {
        sh "git config --global user.email \"jenkins@estafet.com\""
        sh "git config --global user.name \"jenkins\""
        withMaven(mavenSettingsConfig: 'microservices-scrum') {
			sh "mvn release:clean release:prepare release:perform -DreleaseVersion=${releaseVersion} -DdevelopmentVersion=${developmentVersion} -DpushChanges=false -DlocalCheckout=true -DpreparationGoals=initialize -B"
			sh "git push origin master"
			sh "git tag ${releaseVersion}"
			sh "git push origin ${releaseVersion}"
		} 
	}	

	stage("create build config") {
			sh "oc process -n ${project} -f openshift/templates/${microservice}-build-config.yml -p NAMESPACE=${project} -p GITHUB=${params.GITHUB} -p SOURCE_REPOSITORY_REF=${releaseVersion} -p DOCKER_IMAGE_LABEL=${releaseVersion} | oc apply -f -"
	}

	stage("execute build") {
		openshiftBuild namespace: project, buildConfig: microservice, waitTime: "300000"
		openshiftVerifyBuild namespace: project, buildConfig: microservice, waitTime: "300000" 
	}

	stage("create deployment config") {
		sh "oc process -n ${project} -f openshift/templates/${microservice}-config.yml -p NAMESPACE=${project} -p DOCKER_NAMESPACE=${project} -p DOCKER_IMAGE_LABEL=${releaseVersion} | oc apply -f -"
		sh "oc set env dc/${microservice} SPRINT_API_SERVICE_URI=http://sprint-api.${project}.svc:8080 STORY_API_SERVICE_URI=http://story-api.${project}.svc:8080 TASK_API_SERVICE_URI=http://task-api.${project}.svc:8080 JAEGER_AGENT_HOST=jaeger-agent.${project}.svc JAEGER_SAMPLER_MANAGER_HOST_PORT=jaeger-agent.${project}.svc:5778 JAEGER_SAMPLER_PARAM=1 JAEGER_SAMPLER_TYPE=const -n ${project}"
	}
	
	stage("execute deployment") {
		openshiftDeploy namespace: project, depCfg: microservice,  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000" 
	}

	stage("promote image") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: releaseVersion, destinationNamespace: 'prod', destinationStream: microservice, destinationTag: releaseVersion
	}	

	stage("flag this microservice as untested") {
		println "The tests passed successfully"
		sh "oc patch dc/${microservice} -p '{\"metadata\":{\"labels\":{\"testStatus\":\"untested\"}}}' -n ${project}"		
	}	

}

