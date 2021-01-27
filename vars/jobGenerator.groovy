def call(body) {
	
	def inputParams = [
		repoUrl         : null
	]

	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = inputParams
	body()
	
	echo 'repoUrl is :' + inputParams.repoUrl
	
    node {
        //checkout scm
		stage('Pull source') {
            echo 'Pulling ' + inputParams.repoUrl
			checkout scm: [$class: 'GitSCM',
									   userRemoteConfigs: [[url: inputParams.repoUrl]]
						]
        }
		stage('Get branches') {
			echo 'Getting branches...'
			branches = getRepositoryBranches()
			echo 'Branch list: ' + branches
		}
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
	
	List result =  [] 
	
	remotes.readLines().each {line ->
	    result.add(line.trim())
	}
	
	return result
}