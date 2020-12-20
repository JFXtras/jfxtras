pipeline {
    agent any
    tools {
        maven 'maven-3.5.3'
        jdk 'jdk-11'
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