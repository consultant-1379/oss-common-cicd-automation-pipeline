package commonclasses

class CommonSteps {


    static Object defaultLogRotatorValues() {
        return {
            daysToKeep 25
            numToKeep 20
        }
    }

    static Object exportParametersProperties() {
        return {
            filePath('parameters')
            fileFormat('properties')
            keyPattern('')
            useRegexp(false)
        }
    }

    static String defaultJobDescription(String jobNameBeingGenerated, String jobDescription) {
        String defaultJobDescriptionHeader = '''<h3>Job Description</h3>
'''
        String defaultJobDescriptionFooter = '''<br>
<br>
<b> Job developed and maintained by Thunderbee : </b> <a href="mailto:PDLENMCOUN@pdl.internal.ericsson.com?Subject=''' + jobNameBeingGenerated + '''%20Job" target="_top">Send Mail</a> to provide feedback'''
        return defaultJobDescriptionHeader + jobDescription + defaultJobDescriptionFooter
    }

}
