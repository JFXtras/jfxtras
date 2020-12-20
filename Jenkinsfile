pipeline {
    agent any
    tools {
        maven 'Maven 3.5.3`'
        jdk 'jdk11'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "whoami"
                    echo "HOME = ${HOME}"
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo "gpg --list-keys"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn install -DskipTests -Dmaven.javadoc.skip=true'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }
}