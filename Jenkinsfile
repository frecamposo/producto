pipeline {
    agent any

    stages {
        stage('1. Clonar Codigo') {
            steps {
                checkout scm
            }
        }

        stage('2. Ejecutar Pruebas') {
            steps {
                echo 'Ejecutando pruebas automáticas...'
                // REEMPLAZA AQUÍ según tu lenguaje:
                sh 'mvn test'
                 
            }
        }

        stage('3. Analisis SonarQube') {
            steps {
                // El nombre debe coincidir con el Paso 1.2
                withSonarQubeEnv('SonarQube-Server') {
                    script {
                        // El nombre debe coincidir con el Paso 1.3
                        def scannerHome = tool 'SonarScanner'
                        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=producto -Dsonar.sources=."
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs() // Limpia el espacio de trabajo al terminar
        }
    }
}
