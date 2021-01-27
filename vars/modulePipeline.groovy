def call(body) {

	inputParams = [:]
	
	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = inputParams
	body()

	echo "build id is ${env.BUILD_ID}"
	echo "branch is " + inputParams.gitBranch

	node {
        stage('Build') {
            echo 'Building...'
			bat 'gradlew clean build -x test'
        }
        stage('Test') {
            echo 'Testing...'
			bat 'gradlew test'
        }
        stage('Deploy') {
            echo 'Deploying'
        }
    }
}