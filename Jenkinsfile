def NOW = new Date().format('yyyyMMddHHmm')

pipeline {
    environment {
        registry = 'https://reg.bitgadak.com'
        registryCredential = 'docker-repository'
        dockerImage = 'reg.bitgadak.com/closememo/mailsender'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '30'))
    }

    agent any

    stages {
        stage('Test') {
            steps {
                sh './gradlew clean test'
            }
        }
        stage('Build jar') {
            when {
                branch 'master'
            }
            steps {
                sh './gradlew clean bootJar'
            }
        }
        stage('Build image and Push') {
            when {
                branch 'master'
            }
            steps {
                echo "Starting to build docker image with tag: $NOW"
                script {
                    app = docker.build(dockerImage)

                    docker.withRegistry(registry, registryCredential) {
                        app.push(NOW)
                        app.push("latest")
                    }
                }
            }
        }
    }
}