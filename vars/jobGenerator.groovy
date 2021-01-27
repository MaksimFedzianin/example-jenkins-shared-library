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
	
	branches = getRepositoryBranches()
	echo 'parsed branches are: ' + branches
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

private List getRepositoryBranches(){
	remotes = bat(returnStdout: true, script: '@echo off | git branch -a | findstr \"remotes/origin\"')
	echo remotes
	remotes = remotes.replace("remotes/origin/", "")
	remotes = remotes.replace("/", "_")
	echo 'branches without prefix is: ' + remotes
	
	List result  = remotes.readLines()
	echo 'result size is: ' + result.size()  
	return result
}