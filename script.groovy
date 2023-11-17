def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    sh 'ls -l /target'
    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t sumanrizvi/ec2-jenkins-pipeline:1.0 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push sumanrizvi/ec2-jenkins-pipeline:1.0'
    }
}

def deployApp() {
    echo 'deploying the application...'
}

return this
