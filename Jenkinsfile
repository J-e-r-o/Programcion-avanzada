// Jenkinsfile - Dockerized Maven agent (recomendado para Java 21)
pipeline {
    agent {
        docker {
            image 'maven:3.9.5-jdk-21'
            args '-v /root/.m2:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        MVN_OPTS = '-DskipTests=false -B'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw || true'    // en caso de que uses mvnw
            }
        }

        stage('Build') {
            steps {
                // usa mvn del container
                sh "mvn ${MVN_OPTS} clean package"
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                junit 'target/surefire-reports/**/*.xml'
            }
        }

        stage('Smoke Run') {
            steps {
                script {
                    // seleccionar el primer jar
                    def jar = sh(script: "ls target/*.jar | head -n 1", returnStdout: true).trim()
                    if (!jar) {
                        error "No se encontró jar en target/"
                    }
                    // arrancar la app en background en puerto 8082 (para no chocar con host)
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
                sh '''
          if [ -f pipeline-app.pid ]; then
            kill $(cat pipeline-app.pid) || true
            rm -f pipeline-app.pid || true
          fi
        '''
            }
        }
    }

    post {
        success { echo 'Pipeline finalizado OK' }
        failure { echo 'Pipeline falló' }
        always { sh 'ls -l target || true' }
    }
}
