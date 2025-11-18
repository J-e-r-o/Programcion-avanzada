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

        stage('Build & Test') {
            steps {

                withMaven(maven: 'M3_HOME') {

                    echo "Preparando permisos y ejecutando Build con Maven..."

                    // Asegura permisos para el wrapper de Maven (mvnw)
                    sh 'chmod +x mvnw || true'

                    // Hace el build de la aplicación (Requisito 2b)
                    sh "mvn ${MVN_OPTS} clean package"

                    // Corre tests automáticos (Requisito 2c)
                    sh 'mvn -B test'
                }
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
                    echo "Iniciando prueba de humo (Smoke Test) en puerto 8082"
                    // Una prueba rápida para asegurar que la app se levanta
                    def jar = sh(script: "ls target/*.jar | head -n 1", returnStdout: true).trim()
                    if (!jar) { error "No se encontró jar en target/" }

                    // Comando Shell multi-línea para levantar la aplicación, esperar y hacer una petición
                    sh """
            nohup java -jar ${jar} --server.port=8082 > pipeline-app.log 2>&1 &
            echo \$! > pipeline-app.pid
            sleep 6
            # El comando curl con --fail fallará el pipeline si el endpoint no responde OK
            curl --fail http://localhost:8082/api/videos || (cat pipeline-app.log && exit 1)
          """
                }
            }
        }

        stage('Cleanup') {
            steps {
                echo "Deteniendo aplicación de prueba de humo"
                // Detiene la app de la prueba 'Smoke Run'
                sh '''
          if [ -f pipeline-app.pid ]; then
            kill $(cat pipeline-app.pid) || true
            rm -f pipeline-app.pid || true
          fi
        '''
            }
        }

        // 🚀 ESTAPA DE DEPLOY
        stage('Deploy') {
            steps {
                script {
                    echo "Iniciando proceso de Deploy"
                    // Realiza el deploy (Requisito 2d)
                    def jar = sh(script: "ls target/*.jar | head -n 1", returnStdout:true).trim()
                    if (!jar) error "No se encontró jar para deploy"

                    // Ejecuta el script de deployment.
                    sh "chmod +x ./deploy-mac.sh || true"
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