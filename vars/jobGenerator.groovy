def call(body) {
	
	def inputParams = [
		repoUrl         : null
	]

	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = inputParams
	body()
	
	echo 'repoUrl is :' + inputParams.repoUrl
	
    node {
        stage('Pull source') {
            echo 'Pulling ' + inputParams.repoUrl
			checkout scm: [$class: 'GitSCM',
									   userRemoteConfigs: [[url: inputParams.repoUrl]]
						]
        }
		stage('Update modules jobs') {
			jobDslExecute("""
				folder('module') {
					description 'Folder for module pipelines'
					displayName 'Module Pipelines'
				}
			""")
			
			echo 'Getting branches...'
			branches = getRepositoryBranches()
			echo 'Branch list: ' + branches
			
			List<String> moduleJobs = [];
			
			branches.each { branch ->
				String jobName = "module/" + branch
				echo 'jobName is: ' + jobName
				moduleJobs << jobName
				
				String scriptText = """
						modulePipeline()
						"""
				
				
				jobDslExecute("""
					pipelineJob('$jobName') {
						description '\"$jobName\" pipeline'
													
						definition {
							cps {
								script(\"\"\"$scriptText\"\"\")
								sandbox(true)
							}						
						}
							
						logRotator {
							numToKeep(5)
						}
							
					}		
				""")
			}
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

private void jobDslExecute(String jobDslScript) {
	echo jobDslScript
	jobDsl scriptText: jobDslScript
}
