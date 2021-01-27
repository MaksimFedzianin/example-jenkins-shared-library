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
	remotes = bat(returnStdout: true, script: 'git branch -a | findstr \"remotes/origin\"')
	echo remotes
	remotes = remotes.replace("remotes/origin/", "")
	echo 'branches without prefix is: ' + remotes
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