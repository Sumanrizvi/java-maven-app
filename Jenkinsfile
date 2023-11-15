#!/usr/bin.env groovy

pipeline {   
    agent any
    stages {
        stage("test") {
            steps {
                script {
                    echo "Testing the application..."

                }
            }
        }
        stage("build") {
            steps {
                script {
                    echo "Building the application..."
                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    sshagent(['ec2-server-key']) {
                        def dockerCmd = 'docker run -p 3080:3080 -d sumanrizvi/ec2-jenkins-pipeline:1.1.1-14'
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@34.228.165.200 ${dockerCmd}"
                    }
                }
            }
        }               
    }
} 
