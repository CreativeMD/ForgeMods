import java.util.jar.*;
import groovy.json.*

class ResourceHelper {
    
    static void processJsonFile(properties, file) {
        String fileContents = file.text.replaceAll(/\$\{[^}]*\}/, "0")
        def input = new JsonSlurper().parseText(fileContents)
        processJson(properties, input)
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(JsonOutput.prettyPrint(JsonOutput.toJson(input)).replace("\n", "\r\n"));
        writer.close();
    }
    
    static void processJson(properties, object) {
        object.each {
            entry -> {
                if(entry.value instanceof Map) {
                    processJson(properties, entry.value)
                } else if(properties.keySet().contains(entry.key)) {
                    entry.value = properties.get(entry.key)
                    if(entry.value.isNumber()) {
                        entry.value = entry.value.toInteger()
                    }
                }
            }
        }
    }
    
}
