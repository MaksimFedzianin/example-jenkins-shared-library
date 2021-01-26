def call() {
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