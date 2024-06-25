package de.officeryoda.plantuml

import java.io.File

object PlantUMLGenerator {

    fun generate(data: List<ClassData>) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("@startuml\n\n")

        for (classData: ClassData in data) {
            stringBuilder.append("class ${classData.name} {\n")

            for (field: FieldData in classData.fields) {
                stringBuilder.append("\t${visibilityPrefix(field.modifiers)}${field.name}")
                if (field.type.isNotEmpty()) stringBuilder.append(": ${field.type}")
                stringBuilder.append("\n")
            }

            for (method: MethodData in classData.methods) {
                stringBuilder.append("\t${visibilityPrefix(method.modifiers)}${method.name}()")
                if (method.returnType.isNotEmpty()) stringBuilder.append(": ${method.returnType}")
                stringBuilder.append("\n")
            }

            stringBuilder.append("}\n")
        }

        stringBuilder.append("\n@enduml\n")

        generateFile(stringBuilder.toString())
    }

    private fun generateFile(plantUML: String) {
        val file = File("src/main/resources/output/OutputUML.puml")
        file.writeText(plantUML)
    }

    private fun visibilityPrefix(modifiers: Set<Modifier>): String {
        return when {
            modifiers.contains(Modifier.PUBLIC) -> "+"
            modifiers.contains(Modifier.PRIVATE) -> "-"
            modifiers.contains(Modifier.PROTECTED) -> "#"
            modifiers.contains(Modifier.INTERNAL) -> "~"
            modifiers.contains(Modifier.NO_VISIBILITY) -> ""
            else -> throw IllegalArgumentException("No visibility modifier found")
        }
    }
}