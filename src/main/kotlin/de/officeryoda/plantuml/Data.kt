package de.officeryoda.plantuml

data class ClassData(val name: String, val visibility: Visibility, val fields: List<FieldData>, val methods: List<MethodData>)
data class MethodData(val name: String, val visibility: Visibility, val returnType: String, val parameters: List<ParameterData>)
data class ParameterData(val name: String, val type: String)
data class FieldData(val name: String, val visibility: Visibility, val type: String)

enum class Visibility {
    PUBLIC,
    PRIVATE,
    PROTECTED,
    PACKAGE_PRIVATE
}
