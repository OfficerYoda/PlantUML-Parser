package de.officeryoda.plantuml

fun main() {
    // Load the class files from the resource folder as Strings
    val classFiles: String = ClassLoader.getSystemClassLoader().getResourceAsStream("DogAndCat.kt")!!.bufferedReader().readText()
    val data: List<ClassData> = PlantUMLParser.parseKotlinFile(classFiles)
    println(data)
    PlantUMLGenerator.generate(data)
}