def getDslParamaters() {
    return [SLAVE: env.SLAVE]
}

def getPipelineJobList() {
    def pipelineJobList = []

    pipelineJobList.add("cicd_files/dsl/pipeline_jobs/BuildAndPublish.groovy")
    pipelineJobList.add("cicd_files/dsl/pipeline_jobs/PreCodeReview.groovy")

    pipelineJobList.add("cicd_files/spinnaker/dsl/pipeline_jobs/OssCommonCicdPipelineGenerator.groovy")
    pipelineJobList.add("cicd_files/spinnaker/dsl/pipeline_jobs/ThunderbeeOssCommonCicdPipelineRefspecGenerator.groovy")

    pipelineJobList.add('cicd_files/dsl/pipeline_operations/PipelineGenerator.groovy')
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
                error('Required parameter(s) not set. Please provide a value for all required parameters')
            }
        }

        stage ('Generate RPT Client Pipeline Jobs') {
            steps {
                jobDsl targets: getPipelineJobList(),
                additionalParameters: getDslParamaters(),
                additionalClasspath: env.DSL_CLASSPATH
            }
        }

        stage ('Update RPT Client List View') {
            steps {
                jobDsl targets: 'cicd_files/dsl/pipeline_views/View.groovy'
            }
        }
    }

    post {
        success {
            build propagate: true, wait: true, job: 'oss-common-cicd-automation-pipeline_Build_And_Publish'
        }
        always {
            cleanWs()
        }
    }
}
