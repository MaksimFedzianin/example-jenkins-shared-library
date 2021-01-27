def call(body) {

	inputParams = [:]
	
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = inputParams
	body()

	echo "build id is ${env.BUILD_ID}"
	echo "branch is " + inputParams.gitBranch
	
	node {
		stage('Checkout') {
			git(
					url          : inputParams.repoUrl,
					branch       : inputParams.gitBranch
				)
		}
        stage('Build') {
            echo 'Building...'
			bat 'gradlew clean build -x test'
        }
        stage('Test') {
            echo 'Testing...'
			bat 'gradlew test'
        }
		
		if(inputParams.gitBranch.equals('master')) {
			stage('Deploy') {
				echo 'Deploying'
				
				rtServer (
					id: "local-artifactory",
					url: "http://localhost:8081/artifactory",
					credentialsId: inputParams.artifactoryCredentialsId
					//username: 'admin',
					//password: 'Admin123'
				)

				rtUpload (
					serverId: 'local-artifactory',
					spec: """{
						"files": [
							{
								"pattern": "build/libs/*.jar",
								"target": "test-repo/example-shared-library-pipeline/jar/$inputParams.gitBranch/${BUILD_NUMBER}/"
							}
						]
					}"""				 
				)			
			}
		}
    }
}