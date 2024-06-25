package de.officeryoda.plantuml

fun main() {
    // Load the class files from the resource folder as Strings
    val classFiles: String = ClassLoader.getSystemClassLoader().getResourceAsStream("Fish.kt")!!.bufferedReader().readText()
    println(PlantUMLParser.parseKotlinFile(classFiles))
}