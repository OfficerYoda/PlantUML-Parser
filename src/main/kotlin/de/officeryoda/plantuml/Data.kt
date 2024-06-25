package de.officeryoda.plantuml

data class ClassData(
    val name: String,
    val modifiers: Set<Modifier>,
    val fields: List<FieldData>,
    val methods: List<MethodData>,
)

data class FieldData(
    val name: String,
    val modifiers: Set<Modifier>,
    val type: String,
)

data class MethodData(
    val name: String,
    val modifiers: Set<Modifier>,
    val returnType: String,
    val parameters: List<String>,
)

enum class Modifier {
    PUBLIC,
    PRIVATE,
    PROTECTED,
    INTERNAL,
    STATIC, // not used in kotlin
    OVERRIDE,
    ABSTRACT,
    INTERFACE,
    ENUM,
    ANNOTATION,
    DATA,
    OBJECT,
    OPERATOR,

    // Custom
    NO_VISIBILITY // used for enum values
}