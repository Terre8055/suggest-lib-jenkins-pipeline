pipeline {

    agent any

    tools {
        maven "maven"
        jdk "JDK 1.8"
    }

    triggers {
        gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
    }
    
    options {
        timestamps()
        timeout(time: 15, unit: 'MINUTES') 
        gitLabConnection('gitlab')
        ansiColor("xterm")
    }

    parameters {
        booleanParam(name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }


        stage("Compile->Test->Verify->Install->Deploy") {
            when {
                branch "main"
            }
            steps {
                sh "mvn -B deploy"
            }
        }

        stage("Compile->Test->Verify") {
            when {
                expression { BRANCH_NAME ==~ /^feature\/.*$/ }
            }
            steps {
                sh "mvn verify"
            }
        }

        stage("Release") {
            when {
                expression { params.RELEASE }
            }
            steps {
                sh "mvn versions:set -DnewVersion=1.0"
                sh "mvn -Dresume=false -DignoreSnapshots=true -Dconnectionurl= release:prepare"
                sh "mvn -Dresume=false -DignoreSnapshots=true -Dconnectionurl= release:perform"
            }
        }

    }

    post {
        always {
            deleteDir()
        }

        success {
            echo 'Build and deploy successful'
            
        }
        failure {
            echo 'Build and deploy failed'
            
        }

    }
}