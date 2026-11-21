
pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

       stage('Docker Check') {
            steps {
                bat '"C:\\Users\\HP\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --host npipe:////./pipe/dockerDesktopLinuxEngine version'
            }
        }

        stage('Docker Build') {
            steps {
                bat '"C:\\Users\\HP\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t ci-cd-learning:%BUILD_NUMBER% .'
            }
        }

        stage('Docker Test') {
            steps {
                bat '"C:\\Users\\HP\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --host npipe:////./pipe/dockerDesktopLinuxEngine run --rm ci-cd-learning:%BUILD_NUMBER%'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/TEST-*.xml'
        }

        success {
            archiveArtifacts artifacts: 'target/*.jar',
                             fingerprint: true
        }
    }
}
