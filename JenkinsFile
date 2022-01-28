pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                bat "./gradlew test"
                bat "./gradlew jacocoTestReport"
            }
        }
        stage('Build') {
            steps {
                bat "./gradlew build"
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('sonarqube-9.2.4.50792') {
                    bat "./gradlew sonarqube"
                }
            }
        }
        stage("Quality gate") {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }
    }
    post {
        success {
            deploy adapters: [
                                tomcat9(url: 'http://localhost:8080',
                                        credentialsId: 'tomcat-deployer')
                             ],
                             war: '**/*.war',
                             contextPath: 'app'
        }
    }
}