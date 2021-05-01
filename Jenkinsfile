pipeline{
    agent any
stages {
stage('Build'){
    steps{
        withMaven{
            sh 'mvn clean package'
        }
    }
}

stage('Deployment'){
    steps{
        sh '''
        docker-compose -f /opt/bt-backend/docker-compose.yml down || true
        docker image rm bt-backend || true
        docker build -t bt-backend .
        docker-compose -f /opt/bt-backend/docker-compose.yml up -d
        '''
}}}}
