import common_classes.CommonSteps
import common_classes.CommonParameters

CommonSteps commonSteps = new CommonSteps()
CommonParameters commonParams = new CommonParameters()


def pipelineBeingGeneratedName = 'oss-common-cicd-automation-pipeline_Pipeline_Generator'

pipelineJob(pipelineBeingGeneratedName) {
    description(commonSteps.defaultJobDescription(pipelineBeingGeneratedName,
        """The ${pipelineBeingGeneratedName} job is used to generate initial OSS Common CICD Automation Pipeline pipelines and a CICD pipeline updater job""",
        "cicd_files/dsl/pipeline_operations/PipelineGenerator.groovy",
        "cicd_files/jenkins/files/pipeline_operations/PipelineGenerator.Jenkinsfile"
    ))

    parameters {
        stringParam(commonParams.slave())
    }
    logRotator(commonSteps.defaultLogRotatorValues())

    definition {
        cpsScm {
            scm {
                git {
                    branch('master')
                    remote {
                        url(commonParams.repoUrl())
                    }
                    extensions {
                        cleanBeforeCheckout()
                        localBranch 'master'
                    }
                }
            }
            scriptPath('cicd_files/jenkins/files/pipeline_operations/PipelineGenerator.Jenkinsfile')
        }
    }
}

