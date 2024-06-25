package de.officeryoda.plantuml

data class ClassData(
    val name: String,
    val modifiers: Set<Modifier>,
    val fields: List<FieldData>,
    val methods: List<MethodData>,
) {
    override fun toString(): String {
        return "ClassData(name='$name', modifiers=$modifiers, \n\tfields=$fields, \n\tmethods=$methods)"
    }
}

data class FieldData(
    val name: String,
    val modifiers: Set<Modifier>,
    val type: String,
)

data class MethodData(
    val name: String,
    val modifiers: Set<Modifier>,
    val returnType: String,
    val parameters: List<ParameterData>,
)

data class ParameterData(
    val name: String,
    val type: String,
)

enum class Modifier {
    // General
    PUBLIC,
    PRIVATE,
    PROTECTED,
    INTERNAL,
    STATIC, // not used in kotlin
    FINAL,
    OPEN,
    SEALED,

    // Class types
    ABSTRACT,
    INTERFACE,
    ENUM,
    ANNOTATION,
    DATA,
    OBJECT,

    // Method/Field types
    SYNCHRONIZED,
    OVERRIDE,
    OPERATOR,
}