#!/usr/bin/env groovy

def bob = "bob/bob -r \${WORKSPACE}/cicd_files/spinnaker/jenkins/rulesets/ThunderbeeOssCommonCicdPipelineRefspecGenerator.yaml"

pipeline {
    agent {
        label SLAVE
    }
    environment {
        TB_GERRIT_CREDS = credentials('TB_GERRIT_USER')
    }
    stages {
        stage('Set build description') {
            steps {
                script {
                    currentBuild.description = "<b>Parameter File = ${PATH_TO_CSV}<br>Pipeline Template = ${PATH_TO_JSON}</b>"
                }
            }
        }
        stage('Clean Workspace') {
            steps {
                sh 'git clean -xdff'
                sh 'git submodule sync'
                sh 'git submodule update --init --recursive'
            }
        }
        stage('Prepare Repo') {
            steps {
                script {
                    sh "${bob} prepare-repo"
                }
            }
        }
        stage('Modify CSV Values') {
            steps {
                script {
                    sh "${bob} modify-csv-values"
                }
            }
        }
        stage('Rollout or Update spinnaker pipelines') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'TB_SPINNAKER_USER', usernameVariable: 'USER', passwordVariable: 'PASS'),
                    usernamePassword(credentialsId: 'TB_GERRIT_USER', usernameVariable: 'GERRIT_USER', passwordVariable: 'GERRIT_TOKEN')
                ]) {
                  echo 'Set up spin cli config file'
                  sh 'sh ${WORKSPACE}/configure_spin_cli_config.sh \"${USER}\" \"${PASS}\"'
                  echo 'Rolling out spinnaker pipeline changes...'
                  sh '''
                    docker run --user "$(id -u):$(id -g)" --rm \
                    -v ${WORKSPACE}/.spin/config:/mnt/.spin/config:ro \
                    -v ${WORKSPACE}/spinnaker-pipelines-repo/oss-common-cicd-pipeline-resources:/mnt/spinnaker-pipelines \
                    armdocker.rnd.ericsson.se/proj-eric-oss-dev-test/common_cicd_automation_pipeline:latest \
                    python3 -m spinnaker_pipeline_creation create-or-update-test-pipeline -fpf \"${PATH_TO_CSV}\" -fptf \"${PATH_TO_JSON}\" -gc \"${GERRIT_USER}:${GERRIT_TOKEN}\"
                  '''
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
