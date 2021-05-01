pipeline{
    agent any

    stages {
        stage('Build'){
            steps{
                withMaven{
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Stop the old node'){
            steps{
                sh '''
                        docker-compose -f /opt/bt-backend/docker-compose.yml down || true
                '''
            }
        }

        stage('Remove the old image'){
            steps{
                sh '''
                        docker image rm bt-backend || true
                '''
            }
        }
        stage('Build the new Image'){
            steps{
                sh '''
                        docker build -t bt-backend .
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                        docker-compose -f /opt/bt-backend/docker-compose.yml up -d
                '''
            }
        }
    }
}
