import commonclasses.CommonSteps
import commonclasses.CommonAuthorization
import commonclasses.CommonViewProperties
import commonclasses.CommonPipelineGenerationParameters

///////////////////////////
//   DSL SCRIPT START   //
/////////////////////////

CommonSteps commonSteps = new CommonSteps()
CommonAuthorization commonAuthorization = new CommonAuthorization()
CommonPipelineGenerationParameters commonParams = new CommonPipelineGenerationParameters()

def pipelineBeingGeneratedName = "OSS_Common_CICD_DSL_Job_Generator"

pipelineJob(pipelineBeingGeneratedName) {

    String[] permissionGroupsArray = "${PERMISSION_GROUPS}".split(',')
    permissionGroupsArray.each {
        authorization(commonAuthorization.setPermissionGroup("${it}"))
    }

    description(commonSteps.defaultJobDescription(pipelineBeingGeneratedName, '''<p>This Job is used to generate the DSL Jobs needed for CICD pipeline automation.</p>'''))

    parameters {
        stringParam(commonParams.dslSlaves())
        stringParam(commonParams.cicdSlaves())
        stringParam(commonParams.teamEmail())
        stringParam(commonParams.permissionGroups())
    }

    logRotator(commonSteps.defaultLogRotatorValues())

    definition {
        cpsScm {
            scm {
                git {
                    branch('master')
                        remote {
                            url("\${GERRIT_MIRROR_HTTPS}/OSS/com.ericsson.oss.cicd/oss-common-cicd-automation-pipeline")
                        }
                        extensions {
                             cleanBeforeCheckout()
                             localBranch 'master'
                        }
                }
            }
            scriptPath("DSL/DSL_Job_Generation/Jenkinsfiles/Jenkinsfile")
        }
    }
}
