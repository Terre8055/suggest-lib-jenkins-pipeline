pipeline {

    agent any

    tools {
        maven "maven"
        jdk "openjdk-8"
    }

    triggers {
        gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
    }

    environment {
        GITLAB_CONN_URL = "scm:git:https://gitlab.com/Terre8055/imageio-ext.git"
        GITLAB_CONN_EMAIL = "michaelappiah2018@icloud.com"
        GITLAB_CONN_NAME = "Terre8055"
    }
    
    options {
        timestamps()
        timeout(time: 15, unit: 'MINUTES') 
        gitLabConnection('gitlab')
    }

    parameters {
        booleanParam(name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
        string(name: "VERSION", defaultValue: "1.0", description: "Specify the version for the release.")
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
                echo 'Deploying Snapshot from main.....'
                configFileProvider([configFile(fileId: 'artifactory-settings', variable: 'MAVEN_SETTINGS_XML')]) {
                            sh "mvn -B deploy"
                }
            }
        }

        stage("Compile->Test->Verify") {
            when {
                expression { BRANCH_NAME ==~ /^feature\/.*$/ }
            }
            steps {
                echo 'Verifying and Testing on feature branch....'
                configFileProvider([configFile(fileId: 'artifactory-settings', variable: 'MAVEN_SETTINGS_XML')]) {
                            sh "mvn verify"
                }
            }
        }

        stage("Release") {
            when {
                expression { params.RELEASE }
            }
            steps {
                script {
                    def releaseVersion = params.VERSION
                    echo "Releasing version: ${releaseVersion}"


                    configFileProvider([configFile(fileId: 'artifactory-settings', variable: 'MAVEN_SETTINGS_XML')]) {
                            sh "mvn versions:set -DnewVersion=${releaseVersion}"
                    }
                    
                    sh "git config user.email ${GITLAB_CONN_EMAIL}"
                    sh "git config user.name ${GITLAB_CONN_NAME}"

                    withCredentials([usernamePassword(credentialsId: 'gt_token', passwordVariable: 'GITLAB_PASSWORD', usernameVariable: 'GITLAB_USERNAME')]) {
                        sh "git commit -am 'Release version ${releaseVersion}'"
                        sh "git tag -a ${releaseVersion} -m 'Release version ${releaseVersion}'"
                        sh "git remote set-url origin https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@gitlab.com/Terre8055/suggest-lib.git"
                        sh "git push origin ${BRANCH_NAME} ${releaseVersion}"
                    }

                    configFileProvider([configFile(fileId: 'artifactory-settings', variable: 'MAVEN_SETTINGS_XML')]) {
                        sh "mvn -Dresume=false -DskipTests=true -DignoreSnapshots=true -DconnectionUrl=${GITLAB_CONN_URL} release:prepare release:perform -B -X"
                    }
                    
                }
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