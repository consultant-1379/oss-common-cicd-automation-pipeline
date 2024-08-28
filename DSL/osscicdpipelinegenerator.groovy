
import commonclasses.CommonSteps
import commonclasses.CommonAuthorization

///////////////////////////
//   DSL SCRIPT START   //
/////////////////////////

CommonSteps commonSteps = new CommonSteps()
CommonAuthorization commonAuthorization = new CommonAuthorization()

def pipelineBeingGeneratedName = "OSS-CICD-AUTOMATION_Pipeline_Generator"

pipelineJob(pipelineBeingGeneratedName) {

    String[] permissionGroupsArray = "${PERMISSION_GROUPS}".split(',')
    permissionGroupsArray.each {
        authorization(commonAuthorization.setPermissionGroup("${it}"))
    }

    description(commonSteps.defaultJobDescription(pipelineBeingGeneratedName, '<p>This job is used generate CICD E2E pipelines for IDUN</p>'))

    concurrentBuild(allowConcurrentBuild = true)

    parameters {
      stringParam('SLAVE', "${CICD_SLAVES}", 'Slave to run pipeline against')
      activeChoiceParam('pipeline_template') {
          description('Select the template associated with the pipeline you want to generator or update')
          choiceType('SINGLE_SELECT')
          groovyScript {
              script('''def parameterFiles = []
  parameterFilesUrl = "https://gerrit-gamma.gic.ericsson.se/plugins/gitiles/OSS/com.ericsson.oss.cicd/oss-common-cicd-pipeline-resources/+/refs/heads/master/pipeline_templates/?format=json"
  URL parameterFilesJson = new URL(parameterFilesUrl)
  def slurper = new groovy.json.JsonSlurper()
  def result = slurper.parseText(parameterFilesJson.text.substring(4))

  def list = []
  result.entries.name.each
  {
  list.add(it)
  }
  return list''')
              fallbackScript('"None"')
          }
      }
      activeChoiceParam('parameters_file') {
          description('Select the template associated with the pipeline you want to generator or update')
          choiceType('SINGLE_SELECT')
          groovyScript {
              script('''def parameterFiles = []
  parameterFilesUrl = "https://gerrit-gamma.gic.ericsson.se/plugins/gitiles/OSS/com.ericsson.oss.cicd/oss-common-cicd-pipeline-resources/+/refs/heads/master/parameter_files/?format=json"
  URL parameterFilesJson = new URL(parameterFilesUrl)
  def slurper = new groovy.json.JsonSlurper()
  def result = slurper.parseText(parameterFilesJson.text.substring(4))

  def list = []
  result.entries.name.each
  {
  list.add(it)
  }
  return list''')
              fallbackScript('"None"')
          }
      }
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
            scriptPath("Jenkinsfiles/spinnaker_pipeline_creation_flow/Jenkinsfile")
        }
    }
}