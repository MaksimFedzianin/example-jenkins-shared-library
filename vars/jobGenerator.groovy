def call(body) {
	
	def inputParams = [:]

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
					displayName '$inputParams.modulePipelineName'
				}
			""")
			
			echo 'Getting branches...'
			//branches = getRepositoryBranches()
			branches = new GitHelper(this).getBranches()
			echo 'Branch list: ' + branches
			
			List<String> moduleJobs = [];
			
			branches.each { branch ->
				String jobName = "module/" + branch.replaceAll('/', '_')
				echo 'jobName is: ' + jobName
				moduleJobs << jobName
				
				String scriptText = """
						modulePipeline {
							gitBranch = "$branch"
							repoUrl = "$inputParams.repoUrl"
							artifactoryCredentialsId = "$inputParams.artifactoryCredentialsId"
						}
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
		
		stage('Run new jobs') {
			
			try {
				jenkins.model.Jenkins.instance.getAllItems(Job.class).each{

					if (!it.fullName.startsWith('module/')) {
						return;
					}

					if (it.getBuilds().size() == 0) {
						println("Starting job $it.fullName")

						build job: it.fullName, quietPeriod: 0, wait: false;
					}

				}
			} catch (err) {
				echo "failed to run job : $err"
			}
        }
    }
}

private void jobDslExecute(String jobDslScript) {
	echo jobDslScript
	jobDsl scriptText: jobDslScript
}
