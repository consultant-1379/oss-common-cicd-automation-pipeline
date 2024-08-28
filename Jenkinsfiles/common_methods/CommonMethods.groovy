
def exportEnvVariablesToFile(String fileName) {
    String properties = ""
    env.getEnvironment().each { name, value ->
        if(!value.contains("#") && !value.isEmpty()) {
            properties += "${name}=${value}\n"
        }
    }
    writeFile file: fileName, text: properties
}

def loadProperties(String filePath) {
    file = readFile(filePath).trim()
    def lines = file.readLines()
    for (line in lines) {
        arr = line.split('=')
        env."${arr[0]}" = "${arr[1]}"
    }
}

return this