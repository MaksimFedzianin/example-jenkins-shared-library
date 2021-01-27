def call(body) {
	
	def inputParams = [
		repoUrl         : null
	]

	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = inputParams
	body()
	
	echo 'repoUrl is :' + inputParams.repoUrl
	
	checkout scm: [$class: 'GitSCM',
									   userRemoteConfigs: [[url: inputParams.repoUrl]]
						]
	remotes = bat(returnStdout: true, script: 'git remote -v')
	echo remotes
    node {
        //checkout scm
        stage('Install') {
            echo 'Installing...'
        }
        stage('Test') {
            echo 'Testing...'
        }
        stage('Deploy') {
            echo 'Deploying...'
        }
    }
}