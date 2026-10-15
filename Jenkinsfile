pipeline {
    agent any
tools {
    jdk 'JDK-21'
}
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Build and Test') {
            steps {
                bat 'mvn -B clean test package'
            }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t regulatory-checklist:1.0 .'
            }
        }
        stage('Deploy Container') {
            steps {
                bat 'docker rm -f regulatory-checklist-demo 2>nul || exit /b 0'
                bat 'docker run -d --name regulatory-checklist-demo -p 8084:8080 regulatory-checklist:1.0'
            }
        }
        stage('Smoke Test') {
            steps {
                bat 'powershell -NoProfile -Command "$r=$null; for($i=0;$i -lt 12;$i++){try{$r=Invoke-WebRequest http://localhost:8084 -UseBasicParsing;break}catch{Start-Sleep -Seconds 2}}; if($r -eq $null){exit 1}; if($r.StatusCode -ne 200){exit 1}"'
            }
        }
    }
    post {
        success { echo 'Build, tests, Docker deployment and smoke test completed successfully.' }
        failure { echo 'Pipeline failed. Open the failed stage console output and fix that error first.' }
    }
}
