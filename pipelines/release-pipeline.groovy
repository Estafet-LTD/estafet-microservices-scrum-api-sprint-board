@NonCPS
def getImage(json, microservice) {
	def item = new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice}
	return item.status.dockerImageRepository
}

@NonCPS
boolean deploymentConfigurationExists(json, microservice) {
	return new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice} != null
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

def project = "test"
def microservice = "sprint-board"

def developmentVersion
def releaseVersion
def releaseTag

node('maven') {

	stage("checkout") {
		git branch: "master", url: "https://${username()}:${password()}@github.com/Estafet-LTD/estafet-microservices-scrum-api-sprint-board"
	}
	
	stage("deploy container") {
		sh "oc get is -o json -n ${project} > is.json"
		def is = readFile('is.json')
		def image = getImage (is, microservice)
		sh "oc get dc -o json -n test > dc.json"
		def dc = readFile ('dc.json')
		if (deploymentConfigurationExists (dc, microservice)) {
			openshiftDeploy namespace: project, depCfg: microservice
		} else {
			def template = readFile ('openshift/test-deployment-config.json').replaceAll(/\$\{image\}/, image).replaceAll(/\$\{microservice\}/, microservice)
			def serviceTemplate = readFile ('openshift/test-service-config.yaml').replaceAll(/\$\{microservice\}/, microservice)
			openshiftCreateResource namespace:project, jsonyaml:template
			openshiftCreateResource namespace:project, jsonyaml:serviceTemplate
		}
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "600000"
	}	
	
	stage("execute acceptance tests") {
		sh "oc start-build qa-pipeline -n cicd"	
	}
	
	stage("increment version") {
		def pom = readFile('pom.xml');
		def matcher = new XmlSlurper().parseText(pom).version =~ /(\d+\.\d+\.)(\d+)(\-SNAPSHOT)/
		developmentVersion = "${matcher[0][1]}${matcher[0][2].toInteger()+1}-SNAPSHOT"
		releaseVersion = "${matcher[0][1]}${matcher[0][2]}"
		releaseTag = "v${releaseVersion}"
	}
	
	stage("perform release") {
        sh "git config --global user.email \"jenkins@estafet.com\""
        sh "git config --global user.name \"jenkins\""
        withMaven(mavenSettingsConfig: 'microservices-scrum') {
			sh "mvn release:clean release:prepare release:perform -DreleaseVersion=${releaseVersion} -DdevelopmentVersion=${developmentVersion} -DpushChanges=false -DlocalCheckout=true -DpreparationGoals=initialize -B"
			sh "git push origin master"
			sh "mvn versions:set -DnewVersion=${releaseVersion}"
			sh "git tag ${releaseTag}"
			sh "git push origin ${releaseTag}"
		} 
	}	

	stage("promote to production") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'PrepareForTesting', destinationNamespace: 'prod', destinationStream: microservice, destinationTag: releaseVersion
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'PrepareForTesting', destinationNamespace: 'prod', destinationStream: microservice, destinationTag: 'latest'
	}	
	
}



