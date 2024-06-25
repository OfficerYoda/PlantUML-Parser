package de.officeryoda.plantuml

import kotlin.math.max

object PlantUMLParser {

    private val DEFAULT_VISIBILITY: Modifier = Modifier.PUBLIC

    fun parseKotlinFile(kotlinFile: String): List<ClassData> {
        val data: MutableList<ClassData> = mutableListOf<ClassData>()
        var trimmedFile: String = kotlinFile.trim()
        var classRange: Pair<Int, Int>

        while (trimmedFile.indexOfAny(listOf("class", "interface", "enum", "object")) != -1) {
            val classStart: Int = lineStart(trimmedFile, trimmedFile.indexOfAny(listOf("class", "interface", "object")))
            val classBody: Pair<Int, Int> = nextBlock(trimmedFile)
            val classBlock: String = trimmedFile.substring(classStart, classBody.second)

            data.add(parseClass(classBlock))

            // Remove the class block from the file
            classRange = nextBlock(trimmedFile, 0, '{', '}')
            if (classRange.first == -1 || classRange.second + 1 >= trimmedFile.length) break
            trimmedFile = trimmedFile.substring(classRange.second + 1, trimmedFile.length).trim()
        }

        return data
    }

    private fun parseClass(classBlock: String): ClassData {
        if (classBlock.isEmpty()) return ClassData("", setOf(), emptyList(), emptyList())

        var classIndex: Int = classBlock.indexOfAny(listOf("class", "interface", "object"))
        classIndex += nextWord(
            classBlock,
            classIndex
        ).length // Include the class keyword for potential modifiers (e.g. interface)

        val className: String = nextWord(classBlock, classIndex)
        val classModifiers: List<String> =
            classBlock.substring(
                lineStart(classBlock, classIndex), max(0, classIndex)
            ).split(" ")
        val modifiers: Set<Modifier> = getModifiers(classModifiers)

        val lines: List<String> = classBlock.split("\n")
        val fields: List<FieldData> =
            if (modifiers.contains(Modifier.ENUM)) parseEnumValues(lines) // Enums need special handling
            else parseFields(lines)
        val methods: List<MethodData> = parseMethods(classBlock)

        return ClassData(className, modifiers, fields, methods)
    }

    private fun parseFields(lines: List<String>): List<FieldData> {
        val fields: MutableList<FieldData> = mutableListOf()

        for (line: String in lines) {
            var fieldIndex: Int = line.indexOf("val")
            if (fieldIndex == -1) fieldIndex = line.indexOf("var")
            if (fieldIndex == -1) continue

            val fieldName: String = nextWord(line, fieldIndex + 4)
            val fieldModifiers: List<String> = line.substring(0, fieldIndex).split(" ")
            val modifiers: Set<Modifier> = getModifiers(fieldModifiers)

            val typeIndex: Int = line.indexOf(":", fieldIndex)
            val fieldType: String = if (typeIndex != -1) nextWord(line, typeIndex + 1) else ""

            fields.add(FieldData(fieldName, modifiers, fieldType))
        }

        return fields
    }

    private fun parseEnumValues(lines: List<String>): List<FieldData> {
        val enumValues: MutableList<FieldData> = mutableListOf()

        for (line: String in lines) {
            // If a line only contains one word (the enum value), it is an enum value
            if (line.trim().split(" ").size == 1
                && !line.contains(Regex("[{}()]"))
                && line.isNotBlank()
            ) {

                val value: String = line.trim().removeSuffix(",").removeSuffix(";")
                enumValues.add(FieldData(value, setOf(Modifier.NO_VISIBILITY), ""))
            }
        }

        return enumValues
    }

    private fun parseMethods(codeBlock: String): List<MethodData> {
        val methods: MutableList<MethodData> = mutableListOf()
        var codePart: String = codeBlock

        while (codePart.contains("fun")) {
            val methodStart: Int = codePart.indexOf("fun")
            val methodBodyIndices: Pair<Int, Int> = nextBlock(codePart, methodStart)
            if (methodBodyIndices.first == -1) break

            // Get the method head
            val methodHead: String =
                codePart.substring(
                    lineStart(codePart, methodStart), methodBodyIndices.first - 1
                ).trim() + " "
            val funIndex: Int = methodHead.indexOf("fun")

            // Get the method name and modifiers
            val methodName: String = nextWord(methodHead, funIndex + 3)
            val methodModifiers: List<String> = methodHead.substring(0, funIndex).split(" ")
            val modifiers: Set<Modifier> = getModifiers(methodModifiers)

            // Get the parameters
            val parameterBlock: Pair<Int, Int> = nextBlock(methodHead, 0, '(', ')')
            val parameters: List<String> =
                methodHead.substring(parameterBlock.first + 1, parameterBlock.second - 1)
                    .split(Regex("[,:]"))
                    .map { it.trim() }
            // remove every uneven index, because they are the parameter names (other: Dog, other: Cat)
            val parameterTypes: List<String> = parameters.filterIndexed { index, _ -> index % 2 == 1 }

            // Get the return type
            val headTail: String = methodHead.substring(methodHead.lastIndexOf(")") + 2)
            val returnType: String = nextWord(headTail, 0)

            methods.add(MethodData(methodName, modifiers, returnType, parameterTypes))

            codePart = codePart.substring(methodBodyIndices.second + 1, codePart.length)
        }

        return methods
    }

    private fun getModifiers(modifiers: List<String>): Set<Modifier> {
        val modifierList: MutableSet<Modifier> = mutableSetOf()

        for (modifier: String in modifiers) {
            try {
                modifierList.add(Modifier.valueOf(modifier.uppercase()))
            } catch (_: Exception) {
            }
        }

        // Add default visibility if no visibility modifier is present
        if (modifierList.none {
                it in setOf(
                    Modifier.PUBLIC,
                    Modifier.PRIVATE,
                    Modifier.PROTECTED,
                    Modifier.INTERNAL
                )
            }) {
            modifierList.add(DEFAULT_VISIBILITY)
        }

        return modifierList
    }

    private fun getVisibility(modifiers: List<String>): Modifier {
        for (modifier: String in modifiers) {
            when (modifier) {
                "public" -> return Modifier.PUBLIC
                "private" -> return Modifier.PRIVATE
                "protected" -> return Modifier.PROTECTED
                "internal" -> return Modifier.INTERNAL
            }
        }

        return DEFAULT_VISIBILITY
    }

    // region =====Util=====

    private fun lineStart(code: String, index: Int): Int {
        var start: Int = index
        while (start > 0 && code[start] != '\n') {
            start--
        }
        return start
    }

    private fun nextWord(code: String, start: Int): String {
        var end: Int = start
        val nonWhitespaceStart: Int = nextNonWhitespaceIndex(code, start)
        if (nonWhitespaceStart == -1) return ""

        for (i: Int in nonWhitespaceStart until code.length) {
            val char: Char = code[i]
            if (isOpeningBracket(char) || char.isWhitespace() || char == ':' || char == ',' || char == '(' || char == '{') {
                break
            }
            end = i
        }

        return code.substring(nonWhitespaceStart, end + 1)
    }

    private fun nextNonWhitespaceIndex(code: String, start: Int): Int {
        var index: Int = start

        while (index in code.indices) {
            if (!code[index].isWhitespace()) return index
            else index++
        }

        return -1
    }

    private fun nextBlock(
        code: String,
        startIndex: Int = 0,
        openChar: Char = '{',
        closeChar: Char = '}',
    ): Pair<Int, Int> {
        val subCode: String = code.substring(startIndex, code.length)
        val start: Int = subCode.indexOf(openChar)
        var end: Int = subCode.length - 1

        var openBrackets = 0

        for (i: Int in start until subCode.length) {
            when (code[i]) {
                openChar -> openBrackets++
                closeChar -> openBrackets--
            }

            if (openBrackets == 0) {
                end = i
                break
            }
        }

        return if (openBrackets != 0) Pair(-1, -1)
        else Pair(start + startIndex, end + startIndex + 1)
    }

    private fun isOpeningBracket(char: Char): Boolean {
        return char == '{' || char == '(' || char == '['
    }

    private fun isClosingBracket(char: Char): Boolean {
        return char == '}' || char == ')' || char == ']'
    }

    // endregion
}