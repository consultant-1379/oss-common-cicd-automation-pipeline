pipeline {
    agent {
        node {
            label SLAVE
        }
    }
    stages {
        stage('Set build description') {
            steps {
                script {
                    currentBuild.description = "<b>Parameter File = ${parameters_file}<br>Pipeline Template = ${pipeline_template}</b>"
                }
            }
        }
        stage('Prepare Repo') {
            steps {
                script {
                    sh '''
                    touch parameter.csv
                    touch pipeline_template.json
                    touch pipeline.json
                    '''
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
                    -v ${WORKSPACE}:/mnt/runtime-tempfiles \
                    armdocker.rnd.ericsson.se/proj-eric-oss-dev-test/common_cicd_automation_pipeline:latest \
                    python3 -m spinnaker_pipeline_creation create-or-update-pipeline -ar \"${area}\" -fl \"${flows}\" -pf \"${parameters_file}\" -ptf \"${pipeline_template}\" -gc \"${GERRIT_USER}:${GERRIT_TOKEN}\"
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
