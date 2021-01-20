#!groovy

pipeline {
    agent { label 'macos' }
    parameters {
        string defaultValue: 'master', description: 'Branch Name', name: 'AutomationBranch', trim: true
        string defaultValue: 'AllTests', description: 'XML Name', name: 'xml', trim: true
    }
    tools {
        maven 'Maven 3.6.3'
    }
    options {
        timestamps()
        ansiColor("xterm")
    }
    stages {
        stage('Set Variables') {
            steps {
                script {
                    currentBuild.displayName = "${xml}"
                    currentBuild.description = "Automation Branch -> $AutomationBranch | xml -> $xml"
                }
            }
        }
        stage('Set Branch') {
            steps {
                cleanWs notFailBuild: true
                checkout([
                        $class           : 'GitSCM',
                        branches         : [[name: "${AutomationBranch}"]],
                        userRemoteConfigs: [[url          : "git@gitlab.betconstruct.int:automation_qa/qa_big_hilo_automation.git",
                                             credentialsId: "3ee75bb3-c7f2-48de-b1da-e31347bd8c4c"]]
                ])
            }
        }
        stage("Run") {
            steps {
                withEnv(['PATH+EXTRA=/usr/sbin:/usr/bin:/sbin:/bin:/usr/local/bin']) {
                    sh "mvn clean test -DsuiteXmlFile=${xml}"
                }
            }
            post {
                always {
                    script {
                        withEnv(['PATH+EXTRA=/usr/sbin:/usr/bin:/sbin:/bin:/usr/local/bin']) {
                            allure([includeProperties: false, jdk: '', reportBuildPolicy: 'ALWAYS', results: [[path: 'allure-results']]])
                            sh "allure generate --clean"
                        }
                    }
                }
            }
        }

    }
}