def returnParametersForDsl() {
    return [SLAVE: env.SLAVE]
}

def getPipelineJobs() {
    def pipelineJobList = []

    pipelineJobList.add('cicd_files/dsl/pipeline_jobs/BuildAndPublish.groovy')
    pipelineJobList.add('cicd_files/dsl/pipeline_jobs/PreCodeReview.groovy')

    pipelineJobList.add('cicd_files/spinnaker/dsl/pipeline_jobs/OssCommonCicdPipelineGenerator.groovy')
    pipelineJobList.add('cicd_files/spinnaker/dsl/pipeline_jobs/ThunderbeeOssCommonCicdPipelineRefspecGenerator.groovy')

    pipelineJobList.add('cicd_files/dsl/pipeline_operations/PipelineUpdater.groovy')

    return pipelineJobList.join('\n')
}

pipeline {
    agent {
        node {
            label SLAVE
        }
    }

    environment {
        DSL_CLASSPATH = 'cicd_files/dsl'
    }

    stages {
        stage ('Validate required parameters set') {
            when {
                expression {
                    env.SLAVE == null
                }
            }

            steps {
                error ('Required parameter(s) not set. Please provide a value for all required parameters')
            }
        }

        stage ('Generate OSS Common CICD Automation Pipeline jobs') {
            steps {
                jobDsl targets: getPipelineJobs(),
                additionalParameters: returnParametersForDsl(),
                additionalClasspath: env.DSL_CLASSPATH
            }
        }

        stage ('Generate OSS Common CICD Automation Pipeline List View') {
            steps {
                jobDsl targets: 'cicd_files/dsl/pipeline_views/View.groovy'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
