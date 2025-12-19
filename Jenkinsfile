pipeline {
    agent any
    options {
        ansiColor('xterm')
    }
    stages {
        stage('Code Checkout'){
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    extensions: [
                        [$class: 'GitLFSPull'],
                        [$class: 'CloneOption', noTags: true, shallow: true, timeout: 30]
                    ],
                    userRemoteConfigs: [[credentialsId: 'GitHubToken', url: 'https://github.com/adamsiedlecki/McpDemo.git']]
                ])
            }
        }
        stage('Build') {
            steps {
                echo "STARTING BUILD"
                sh "whoami"
                sh "mvn clean install -DskipTests"
            }
        }
        stage('Run Surefire Tests') {
            steps {
                echo "STARTING SUREFIRE"
                script {
                    def surefireResult = sh(script: 'mvn surefire:test', returnStatus: true)
                    if (surefireResult != 0) {
                        currentBuild.result = 'FAILED'
                        error "Surefire tests failed"
                    }
                }
            }
        }
        stage('Deploy to Host') {
            steps {
                sh 'mv target/McpDemo-*.jar McpDemo.jar'

                sshagent(['jenkins_deploy_user-ssh-key-server1']) {
                            sh '''
                            rsync -avz McpDemo.jar jenkins_deploy_user@10.0.0.20:/opt/docker/docker-oms/mcp-test/McpDemo.jar
                            '''
                }
            }
        }
        stage('Rebuild Docker Compose') {
            steps {
                sshagent(['jenkins_deploy_user-ssh-key-server1']) {
                            sh '''
                            ssh jenkins_deploy_user@10.0.0.20 \
                            "cd /opt/docker/docker-oms && docker compose -p oms up -d --build mcp-test"
                            '''
                }
            }
        }
        stage('Healthcheck') {
            steps {
                script {
                    sleep time: 8, unit: 'SECONDS'
                    def response = httpRequest url: 'https://mcp-test.owocny.com/actuator/health', timeout: 5

                    if (response.status == 200) {
                        println("healthcheck 200")
                    } else {
                        println("healthcheck is not 200")
                        error("healthcheck is not 200")
                    }

                    if (!response.content.contains("UP")) {
                        println("content does not contain UP")
                        error("content does not contain UP")
                    }
                }
            }
        }
    }
}