// Declarative pipeline for the MediSync backend.
// Builds and tests each Spring Boot service with its Maven wrapper (the tests use an
// in-memory H2 database, so the agent needs only a JDK 17+), then publishes the test
// reports and archives the jars.
pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('patient-service') {
            steps {
                dir('patient-service') {
                    sh 'chmod +x mvnw && ./mvnw -B -ntp verify'
                }
            }
        }
        stage('doctor-service') {
            steps {
                dir('doctor_service/doctor_service') {
                    sh 'chmod +x mvnw && ./mvnw -B -ntp verify'
                }
            }
        }
        stage('appointment-service') {
            steps {
                dir('appointment-service') {
                    sh 'chmod +x mvnw && ./mvnw -B -ntp verify'
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
        success {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }
}
