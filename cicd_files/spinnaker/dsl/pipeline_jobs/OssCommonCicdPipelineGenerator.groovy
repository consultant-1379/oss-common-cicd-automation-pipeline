import common_classes.CommonSteps
import common_classes.CommonParameters

CommonSteps commonSteps = new CommonSteps()
CommonParameters commonParams = new CommonParameters()


def pipelineBeingGeneratedName = "OSS-CICD-AUTOMATION_Pipeline_Generator"

pipelineJob(pipelineBeingGeneratedName) {

    description(commonSteps.defaultJobDescription(pipelineBeingGeneratedName,
        """<p>The ${pipelineBeingGeneratedName} job is used generate CICD E2E pipelines for IDUN</p>""",
        "cicd_files/spinnaker/dsl/pipeline_jobs/OssCommonCicdPipelineGenerator.groovy",
        "cicd_files/spinnaker/jenkins/files/pipeline_jobs/OssCommonCicdPipelineGenerator.Jenkinsfile"
    ))

    parameters {
      stringParam(commonParams.slave())
      activeChoiceParam('area') {
          description('Select the area associated with the pipeline you want to generate or update')
          choiceType('SINGLE_SELECT')
          groovyScript {
              script('''import groovy.json.JsonSlurper
// Set your username and token
def username = "tbadmin100"
def password = "${TB_GERRIT_TOKEN}"
// Set the API endpoint URL
def apiUrl = "https://gerrit-gamma.gic.ericsson.se/a/plugins/gitiles/OSS/com.ericsson.oss.cicd/oss-common-cicd-pipeline-resources/+/refs/heads/master/cicd_pipelines_parameters_and_templates?format=json"
// Create the URL object
def url = new URL(apiUrl)
// Open a connection
def connection = url.openConnection()
// Set the username and password for basic authentication
def authString = "${username}:${password}"
def authHeaderValue = "Basic " + authString.bytes.encodeBase64().toString()
connection.setRequestProperty("Authorization", authHeaderValue)
// Set the request method to GET
connection.setRequestMethod("GET")
// Get the response code
def responseCode = connection.getResponseCode()
if (responseCode == 200) {
    // Read the response as JSON
    def responseString = connection.getInputStream().text.substring(4)
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(responseString)
    // Process the JSON data as needed
  	def list = []
    json.entries.name.each {
      if (it != "readme") {
          list.add(it)
      }
    }
    return list
} else {
  println("Request failed with status code: ${responseCode}")
  return []
}
// Close the connection
connection.disconnect()''')
              fallbackScript('"None"')
          }
      }
      activeChoiceReactiveParam('flows') {
          description('Select the flows associated with the pipeline you want to generate or update')
          choiceType('SINGLE_SELECT')
          groovyScript {
              script('''import groovy.json.JsonSlurper
// Set your username and token
def username = "tbadmin100"
def password = "${TB_GERRIT_TOKEN}"
// Set the API endpoint URL
def apiUrl = "https://gerrit-gamma.gic.ericsson.se/a/plugins/gitiles/OSS/com.ericsson.oss.cicd/oss-common-cicd-pipeline-resources/+/refs/heads/master/cicd_pipelines_parameters_and_templates/" + area + "?format=json"
// Create the URL object
def url = new URL(apiUrl)
// Open a connection
def connection = url.openConnection()
// Set the username and password for basic authentication
def authString = "${username}:${password}"
def authHeaderValue = "Basic " + authString.bytes.encodeBase64().toString()
connection.setRequestProperty("Authorization", authHeaderValue)
// Set the request method to GET
connection.setRequestMethod("GET")
// Get the response code
def responseCode = connection.getResponseCode()
if (responseCode == 200) {
    // Read the response as JSON
    def responseString = connection.getInputStream().text.substring(4)
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(responseString)
    // Process the JSON data as needed
  	def list = []
    json.entries.name.each {
      if (it != "readme") {
          list.add(it)
      }
    }
    return list
} else {
  println("Request failed with status code: ${responseCode}")
  return []
}
// Close the connection
connection.disconnect()''')
              fallbackScript('')
          }
          referencedParameter('area')
      }
      activeChoiceReactiveParam('pipeline_template') {
          description('Select the pipeline template associated with the pipeline you want to generate or update')
          choiceType('SINGLE_SELECT')
          groovyScript {
              script('''import groovy.json.JsonSlurper
// Set your username and token
def username = "tbadmin100"
def password = "${TB_GERRIT_TOKEN}"
// Set the API endpoint URL
def apiUrl = "https://gerrit-gamma.gic.ericsson.se/a/plugins/gitiles/OSS/com.ericsson.oss.cicd/oss-common-cicd-pipeline-resources/+/refs/heads/master/cicd_pipelines_parameters_and_templates/" + area + "/" + flows + "/pipeline_template/?format=json"
// Create the URL object
def url = new URL(apiUrl)
// Open a connection
def connection = url.openConnection()
// Set the username and password for basic authentication
def authString = "${username}:${password}"
def authHeaderValue = "Basic " + authString.bytes.encodeBase64().toString()
connection.setRequestProperty("Authorization", authHeaderValue)
// Set the request method to GET
connection.setRequestMethod("GET")
// Get the response code
def responseCode = connection.getResponseCode()
if (responseCode == 200) {
    // Read the response as JSON
    def responseString = connection.getInputStream().text.substring(4)
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(responseString)
    // Process the JSON data as needed
  	def list = []
    json.entries.name.each {
      if (it != "readme") {
          list.add(it)
      }
    }
    return list
} else {
  println("Request failed with status code: ${responseCode}")
  return []
}
// Close the connection
connection.disconnect()''')
              fallbackScript('')
          }
          referencedParameter('area,flows')
      }
      activeChoiceReactiveParam('parameters_file') {
          description('Select the parameter files associated with the pipeline you want to generate or update')
          choiceType('SINGLE_SELECT')
          groovyScript {
              script('''import groovy.json.JsonSlurper
// Set your username and token
def username = "tbadmin100"
def password = "${TB_GERRIT_TOKEN}"
// Set the API endpoint URL
def apiUrl = "https://gerrit-gamma.gic.ericsson.se/a/plugins/gitiles/OSS/com.ericsson.oss.cicd/oss-common-cicd-pipeline-resources/+/refs/heads/master/cicd_pipelines_parameters_and_templates/" + area + "/" + flows + "/parameter_files/?format=json"
// Create the URL object
def url = new URL(apiUrl)
// Open a connection
def connection = url.openConnection()
// Set the username and password for basic authentication
def authString = "${username}:${password}"
def authHeaderValue = "Basic " + authString.bytes.encodeBase64().toString()
connection.setRequestProperty("Authorization", authHeaderValue)
// Set the request method to GET
connection.setRequestMethod("GET")
// Get the response code
def responseCode = connection.getResponseCode()
if (responseCode == 200) {
    // Read the response as JSON
    def responseString = connection.getInputStream().text.substring(4)
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(responseString)
    // Process the JSON data as needed
  	def list = []
    json.entries.name.each {
      if (it != "readme") {
          list.add(it)
      }
    }
    return list
} else {
  println("Request failed with status code: ${responseCode}")
  return []
}
// Close the connection
connection.disconnect()''')
              fallbackScript('')
          }
          referencedParameter('area,flows')
      }
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
            scriptPath("cicd_files/spinnaker/jenkins/files/pipeline_jobs/OssCommonCicdPipelineGenerator.Jenkinsfile")
        }
    }
}
