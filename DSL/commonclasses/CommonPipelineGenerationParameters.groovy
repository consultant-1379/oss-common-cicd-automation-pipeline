package commonclasses

class CommonPipelineGenerationParameters {
    static List cicdSlaves() {
        return ['CICD_SLAVES', '', 'These are slaves which must be capable of running Jenkinsfile. They require the ability to run docker.']
    }

    static List dslSlaves() {
        return ['DSL_SLAVES', '', 'These are slaves which must be capable of running DSL groovy scripts. Slaves must also be capable of cloning repos from gerrit.']
    }

    static List teamEmail() {
        return ['TEAM_EMAIL', '', 'This is the email of the team who the pipelines are being generated for.']
    }

    static List permissionGroups() {
        return ['PERMISSION_GROUPS', '', 'Group or groups of users who have the ability to run pipeline etc. If left blank, users signed in on the Jenkins instance will have the ability to run jobs.']
    }
}
