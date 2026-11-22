
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
                bat '"C:\\Users\\HP\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --host npipe:////./pipe/dockerDesktopLinuxEngine build -t ci-cd-learning:%BUILD_NUMBER% .'
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
                        credentialsId: 'dockerhub-test-2',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"

                        $docker = "C:/Users/HP/AppData/Local/Programs/DockerDesktop/resources/bin/docker.exe"
                        $dockerHost = "npipe:////./pipe/dockerDesktopLinuxEngine"

                        $config = Join-Path $env:TEMP (
                            "docker-push-" + [guid]::NewGuid().ToString()
                        )

                        New-Item -ItemType Directory -Force -Path $config | Out-Null
                        $env:DOCKER_CONFIG = $config

                        try {
                            $credentials = $env:DOCKER_USER + ":" + $env:DOCKER_TOKEN

                            $auth = [Convert]::ToBase64String(
                                [System.Text.Encoding]::UTF8.GetBytes($credentials)
                            )

                            $dockerConfig = @{
                                auths = @{
                                    "https://index.docker.io/v1/" = @{
                                        auth = $auth
                                    }
                                }
                            }

                            $json = $dockerConfig | ConvertTo-Json -Depth 5

                            [System.IO.File]::WriteAllText(
                                (Join-Path $config "config.json"),
                                $json,
                                (New-Object System.Text.UTF8Encoding($false))
                            )

                            Write-Host "Docker-Konfiguration erstellt"

                            $image = "leohief/ci-cd-learning:$env:BUILD_NUMBER"

                            Write-Host "Docker Push gestartet: $image"

                            & $docker --host $dockerHost push $image

                            if ($LASTEXITCODE -ne 0) {
                                throw "Docker Push fehlgeschlagen"
                            }

                            Write-Host "Docker Push erfolgreich: $image"
                        }
                        finally {
                            Remove-Item -Recurse -Force $config -ErrorAction SilentlyContinue
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
