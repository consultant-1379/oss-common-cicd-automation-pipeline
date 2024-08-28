package common_classes

class ExternalParameters {

    static List pipelineRefspec(String defaultValue='') {
        return ['PIPELINE_REFSPEC', defaultValue, 'Refspec for oss-common-cicd-pipeline-resources repo']
    }

    static List pathToJson(String defaultValue='') {
        return ['PATH_TO_JSON', defaultValue, 'Full relative path to the pipeline template JSON file']
    }

    static List pathToCsv(String defaultValue='') {
        return ['PATH_TO_CSV', defaultValue, 'Full relative path to the corresponding CSV parameters file']
    }

    static List destinationApplication(String defaultValue='thunderbeetest') {
        return ['DESTINATION_APPLICATION', defaultValue, 'Application where the pipeline will be saved (Note: This WILL overwrite existing pipelines)']
    }

    static List generatedPipelineName(String defaultValue='') {
        return ['GENERATED_PIPELINE_NAME', defaultValue, 'Name of the pipeline to be generated within thunderbeetest area (Note: This must be the second column in the CSV file)']
    }

}
