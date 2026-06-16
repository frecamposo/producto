pipeline {
    agent any
    tools {
        maven 'Maven_3.9' 
    }
    stages {
        stage('1. Clonar Codigo') {
            steps {
                checkout scm
            }
        }

        stage('2. Ejecutar Pruebas') {
            steps {
                echo 'Ejecutando pruebas automáticas...'
                sh 'mvn clean test'
            }
        }

        stage('3. Analisis SonarQube') {
            steps {
                // 'SonarQube-Server' debe estar en la sección "System" de Jenkins
                withSonarQubeEnv('SonarQube-Server') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=producto'
                }
            }
        }
    }
    post {
        always {
            cleanWs() 
        }
    }
}
