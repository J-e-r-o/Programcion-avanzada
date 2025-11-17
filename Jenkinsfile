pipeline {
    agent any
    tools { maven 'MAVEN_HOME'; jdk 'JDK11' }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Build') { steps { sh 'mvn -B -DskipTests=false clean package' } }
        stage('Tests') { steps { sh 'mvn test' } }
        stage('Archive') { steps { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true } }
        stage('Run smoke') {
            steps {
                // Ejecuta el jar en background (efímero) y hace un curl al health o a /api/videos
                sh '''
          JAR=$(ls target/*.jar | head -n 1)
          nohup java -jar $JAR >/tmp/playlist-ci.log 2>&1 &
          sleep 5
          curl --fail http://localhost:8080/api/videos || (cat /tmp/playlist-ci.log && exit 1)
        '''
            }
        }
    }
    post {
        success { echo 'Pipeline OK' }
        failure { echo 'Pipeline falló' }
    }
}
