#!/usr/bin/env groovy

library identifier: 'jenkins-shared-library@master', retriever: modernSCM(
    [$class: 'GitSCMSource',
    remote: 'https://github.com/Sumanrizvi/jenkins-shared-library.git',
    credentialsID: 'GitHub-Creds'
    ]
)

pipeline {
    agent {
        docker {
            image 'docker:dind'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    tools {
        maven 'Maven'
    }
    environment {
        IMAGE_NAME = 'sumanrizvi/app-server:1.0'
    }
    stages {
        // stage('increment version') {
        //     steps {
        //         script {
        //             echo 'incrementing app version...'
        //             sh 'mvn build-helper:parse-version versions:set \
        //                 -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        //                 versions:commit'
        //             def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
        //             def version = matcher[0][1]
        //             env.IMAGE_NAME = "$version-$BUILD_NUMBER"
        //         }
        //     }
        // }
        stage('build app') {
            steps {
                echo 'building application jar...'
                buildJar()
            }
        }
        stage('build image') {
            steps {
                script {
                    echo 'building the docker image...'
                    // dockerLogin()
                    // buildImage(env.IMAGE_NAME)
                    // dockerPush(env.IMAGE_NAME)
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', passwordVariable: 'PASS', usernameVariable: 'USER')]){
                        sh 'docker ps'
                        sh "docker build -t ${env.IMAGE_NAME} ."
                        sh 'echo $PASS | docker login -u $USER --password-stdin'
                        sh "docker push ${env.IMAGE_NAME}"
                    }
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    echo 'deploying docker image to EC2...'
                    def dockerCmd = "docker run -p 8080:8080 -d ${IMAGE_NAME}"
                    sshagent(['ec2-server-key']) {
                        sh "ssh -o StrictHostKeyChecking=no ec2-user@54.175.44.239 ${dockerCmd}"
                    }

                    // def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME}"
                    // def ec2Instance = "ec2-user@54.175.44.239"

                    // sshagent(['ec2-server-key']) {
                    //     sh "scp server-cmds.sh ${ec2Instance}:/home/ec2-user"
                    //     sh "scp docker-compose.yaml ${ec2Instance}:/home/ec2-user"
                    //     sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
                    // }
                }
            }               
        }
        // stage('commit version update'){
        //     steps {
        //         script {
        //             withCredentials([usernamePassword(credentialsId: 'GitHub-Creds', passwordVariable: 'PASS', usernameVariable: 'USER')]){
        //                 sh "git remote set-url origin https://$USER:$PASS@github.com/Sumanrizvi/java-maven-app.git"
        //                 sh 'git config --global user.email "sumanrizvi@gmail.com"'
        //                 sh 'git config --global user.name "Suman"'
        //                 sh 'git add .'
        //                 sh 'git commit -m "ci: version bump"'
        //                 sh 'git push origin HEAD:jenkins-jobs'
        //             }
        //         }
        //     }
        // }
    }
}

