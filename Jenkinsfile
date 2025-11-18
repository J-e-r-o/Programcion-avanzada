pipeline {

    agent any

    environment {
        MVN_OPTS = '-DskipTests=false -B'
    }

    stages {
        stage('Checkout') {
            steps {
                // Toma el código del repositorio (Requisito 2a)
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw || true'
            }
        }

        stage('Build') {
            steps {
                // Hace el build de la aplicación (Requisito 2b)
                sh "mvn ${MVN_OPTS} clean package"
            }
        }

        stage('Test') {
            steps {
                // Corre tests automáticos (Requisito 2c)
                sh 'mvn -B test'
            }
        }

        stage('Archive') {
            steps {
                // Guarda el JAR y los resultados de JUnit
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                junit 'target/surefire-reports/**/*.xml'
            }
        }

        stage('Smoke Run') {
            steps {
                script {
                    // Una prueba rápida para asegurar que la app se levanta
                    def jar = sh(script: "ls target/*.jar | head -n 1", returnStdout: true).trim()
                    if (!jar) { error "No se encontró jar en target/" }
                    sh """
            nohup java -jar ${jar} --server.port=8082 > pipeline-app.log 2>&1 &
            echo \$! > pipeline-app.pid
            sleep 6
            curl --fail http://localhost:8082/api/videos || (cat pipeline-app.log && exit 1)
          """
                }
            }
        }

        stage('Cleanup') {
            steps {
                // Detiene la app de la prueba 'Smoke Run'
                sh '''
          if [ -f pipeline-app.pid ]; then
            kill $(cat pipeline-app.pid) || true
            rm -f pipeline-app.pid || true
          fi
        '''
            }
        }

        // 🚀 ESTAPA DE DEPLOY CORREGIDA
        stage('Deploy') {
            steps {
                script {
                    // Realiza el deploy (Requisito 2d)
                    def jar = sh(script: "ls target/*.jar | head -n 1", returnStdout:true).trim()
                    if (!jar) error "No se encontró jar para deploy"

                    // Asegúrate de que tu script de deploy maneje el entorno Mac/Windows según el Requisito 3
                    sh "chmod +x ./deploy-mac.sh || true" // Asegura que el script tenga permisos
                    sh "./deploy-mac.sh"
                }
            }
        }
    }

    post {
        success { echo 'Pipeline finalizado OK' }
        failure { echo 'Pipeline falló' }
        always { sh 'ls -l target || true' }
    }
}