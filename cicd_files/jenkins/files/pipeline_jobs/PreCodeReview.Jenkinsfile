#!/usr/bin/env groovy

def bob = "bob/bob -r \${WORKSPACE}/cicd_files/jenkins/rulesets/PreCodeReview.yaml"

pipeline {
    agent {
        label SLAVE
    }
    environment {
        CHANGED_PYTHON_FILES = sh(returnStdout: true, script: "git diff-tree --diff-filter=ACM --no-commit-id --name-only -r $GIT_COMMIT -- 'spinnaker_pipeline_creation/*.py'").replaceAll("\\n", " ")
    }
    stages {
        stage('Clean Workspace') {
            steps {
                sh 'git clean -xdff'
                sh 'git submodule sync'
                sh 'git submodule update --init --recursive'
            }
        }
        stage('Python changed files linting'){
            when {
                expression
                    { env.CHANGED_PYTHON_FILES != null }
            }
            steps {
                script {
                    try {
                        sh "${bob} run-python-linting"
                        sh '''
                        echo "======================"
                        echo "Python linting passed!"
                        echo "======================"
                        '''
                    } catch(error) {
                        sh '''
                        echo "+++++++++++++++++++++++++++++++++++++++++++"
                        echo "Linting errors detected. Please check above"
                        echo "+++++++++++++++++++++++++++++++++++++++++++"
                        '''
                        currentBuild.result = "FAILURE"
                    }
                }
                sh 'echo -e "\n"'
            }
        }
        stage('Python unit testing') {
            steps {
                script {
                    try {
                        sh "${bob} run-python-unit-tests"
                        sh '''
                        echo "========================="
                        echo "Python unit tests passed!"
                        echo "========================="
                        '''
                    } catch(error) {
                        sh '''
                        echo "++++++++++++++++++++++++++++++++++++++++++++"
                        echo "Python unit tests failed. Please check above"
                        echo "++++++++++++++++++++++++++++++++++++++++++++"
                        '''
                        currentBuild.result = "FAILURE"
                    }
                }
                sh 'echo -e "\n"'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
