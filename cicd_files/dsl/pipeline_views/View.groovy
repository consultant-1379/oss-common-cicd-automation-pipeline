sectionedView('Common CICD Automation Pipeline') {
    description('''<div style="padding:1em;border-radius:1em;text-align:center;background:#fbf6e1;box-shadow:0 0.1em 0.3em #525000">
        <b>Common CICD Automation Pipeline</b><br>
       CICD Pipelines and Source Control Jobs.<br><br>
        Team: <b>Thunderbee &#x26A1</b><br>
    </div>''')
    sections {
        listView {
            name('Common CICD Automation Production Jobs')
            jobs {
                name('OSS-CICD-AUTOMATION_Pipeline_Generator')
            }
            columns setViewColumns()
        }
        listView {
            name('Common CICD Automation CICD Jobs')
            jobs {
                name('oss-common-cicd-automation-pipeline_Build_And_Publish')
                name('oss-common-cicd-automation-pipeline_Pre_Code_Review')
            }
            columns setViewColumns()
        }
        listView {
            name('Common CICD Automation Pipeline Jobs Source Control')
            jobs {
                name("oss-common-cicd-automation-pipeline_Pipeline_Generator")
                name("oss-common-cicd-automation-pipeline_Pipeline_Updater")
            }
            columns setViewColumns()
        }
        listView {
            name('Common CICD Automation Pipeline Thunderbee Test Jobs')
            jobs {
                name('oss-common-cicd-automation-pipeline_Pipeline_Refspec_Generator')
            }
            columns setViewColumns()
        }
    }
}

static Object setViewColumns() {
    return {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
