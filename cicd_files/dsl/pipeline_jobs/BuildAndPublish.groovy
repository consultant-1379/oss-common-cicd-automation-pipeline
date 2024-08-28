import common_classes.CommonSteps
import common_classes.CommonParameters

CommonSteps commonSteps = new CommonSteps()
CommonParameters commonParams = new CommonParameters()


def pipelineBeingGeneratedName = "oss-common-cicd-automation-pipeline_Build_And_Publish"
pipelineJob(pipelineBeingGeneratedName) {
    description(commonSteps.defaultJobDescription(pipelineBeingGeneratedName,
        """The ${pipelineBeingGeneratedName} job builds the image for the OSS Common CICD Automation Pipeline Repo.
        It then publishes the package to the armdocker JFrog artifactory""",
        "cicd_files/dsl/pipeline_jobs/BuildAndPublish.groovy",
        "cicd_files/jenkins/files/pipeline_jobs/BuildAndPublish.Jenkinsfile",
        "cicd_files/jenkins/rulesets/BuildAndPublish.yaml"))
    keepDependencies(false)
    logRotator(commonSteps.defaultLogRotatorValues())
    parameters {
        stringParam(commonParams.slave())
    }
     blockOn("oss-common-cicd-automation-pipeline_Pre_Code_Review", {
        blockLevel('GLOBAL')
        scanQueueFor('DISABLED')
    })

    properties {
        disableConcurrentBuilds {
            abortPrevious(false)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    branch('master')
                    remote {
                        name('gcn')
                        url(commonParams.repoUrl())
                    }
                    extensions {
                        cleanBeforeCheckout()
                        localBranch 'master'
                    }
                }
            }
            scriptPath('cicd_files/jenkins/files/pipeline_jobs/BuildAndPublish.Jenkinsfile')
        }
    }
    quietPeriod(5)
    disabled(false)
}
