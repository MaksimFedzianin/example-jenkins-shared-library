def call(body) {
	
	def inputParams = [
		metaRelativePath     : 'jenkins/meta.groovy',
		gitCredentialsId     : 'jenkins-git',
		devopsGitUrl         : null,
		devopsGitBranch      : 'dev',
		ansibleInventoryName : null
	]

	body.resolveStrategy = Closure.DELEGATE_FIRST
	body.delegate = inputParams
	body()
	
	echo 'devopsGitUrl is :' + inputParams.devopsGitUrl
	
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