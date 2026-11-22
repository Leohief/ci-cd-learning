
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


        stage('Docker Tag') {
            steps {
                bat '"C:\\Users\\HP\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --host npipe:////./pipe/dockerDesktopLinuxEngine tag ci-cd-learning:%BUILD_NUMBER% leohief/ci-cd-learning:%BUILD_NUMBER%'
            }
        }

        
        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"

                        $docker = "C:\\Users\\HP\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe"
                        $dockerHost = "npipe:////./pipe/dockerDesktopLinuxEngine"
                        $env:DOCKER_CONFIG = Join-Path $env:WORKSPACE ".docker-jenkins"

                        New-Item -ItemType Directory -Force -Path $env:DOCKER_CONFIG | Out-Null

                        try {
                            $env:DOCKER_TOKEN | & $docker --host $dockerHost login -u $env:DOCKER_USER --password-stdin

                            if ($LASTEXITCODE -ne 0) {
                                throw "Docker Login fehlgeschlagen"
                            }

                            & $docker --host $dockerHost push "leohief/ci-cd-learning:$env:BUILD_NUMBER"

                            if ($LASTEXITCODE -ne 0) {
                                throw "Docker Push fehlgeschlagen"
                            }
                        }
                        finally {
                            Remove-Item -Recurse -Force $env:DOCKER_CONFIG -ErrorAction SilentlyContinue
                        }
                    '''
                }
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
