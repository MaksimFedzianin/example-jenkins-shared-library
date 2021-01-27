def call(body) {
	node {
        stage('MP Install') {
            echo 'Installing in module pipeline...'
        }
        stage('MP Test') {
            echo 'Testing in module pipeline...'
        }
        stage('MP Deploy') {
            echo 'Deploying in module pipeline...'
        }
    }
}