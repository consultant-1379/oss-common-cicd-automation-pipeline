import common_classes.CommonSteps
import common_classes.CommonParameters
import common_classes.ExternalParameters

CommonSteps commonSteps = new CommonSteps()
CommonParameters commonParams = new CommonParameters()
ExternalParameters externalParams = new ExternalParameters()


def pipelineBeingGeneratedName = "oss-common-cicd-automation-pipeline_Pipeline_Refspec_Generator"

pipelineJob(pipelineBeingGeneratedName) {

    description(commonSteps.defaultJobDescription(pipelineBeingGeneratedName,
        """<p>The ${pipelineBeingGeneratedName} job is used generate CICD E2E pipelines for IDUN from a Gerrit Refspec.
        It is intended to be used <b>for Thunderbee test purposes only</b></p>""",
        "cicd_files/spinnaker/dsl/pipeline_jobs/ThunderbeeOssCommonCicdPipelineRefspecGenerator.groovy",
        "cicd_files/spinnaker/jenkins/files/pipeline_jobs/ThunderbeeOssCommonCicdPipelineRefspecGenerator.Jenkinsfile",
        "cicd_files/spinnaker/jenkins/rulesets/ThunderbeeOssCommonCicdPipelineRefspecGenerator.yaml"
    ))

    parameters {
      stringParam(commonParams.slave())
      stringParam(externalParams.pipelineRefspec())
      stringParam(externalParams.pathToJson())
      stringParam(externalParams.pathToCsv())
      stringParam(externalParams.destinationApplication())
      stringParam(externalParams.generatedPipelineName())
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
                    }
                }
            }
            scriptPath("cicd_files/spinnaker/jenkins/files/pipeline_jobs/ThunderbeeOssCommonCicdPipelineRefspecGenerator.Jenkinsfile")
        }
    }
}
