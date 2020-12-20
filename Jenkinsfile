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
                    whoami
                    echo "HOME = ${HOME}"
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    gpg --list-keys
                '''
            }
        }

        stage ('Build') {
            steps {
                // mvn activates the jfxtras profile, using that pgp can be setup in mvn's settings.xml, most notably gpg.passphrase and gpg.keyname
                // http://maven.apache.org/plugins/maven-gpg-plugin/sign-mojo.html
                // UI test don't well on Jenkins
                // Javadoc generation using asciidoclet is not working well
                sh 'mvn install -P jfxtras -DskipTests -Dmaven.javadoc.skip=true'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }
}