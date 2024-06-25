package de.officeryoda.plantuml

object PlantUMLParser {

    private val DEFAULT_VISIBILITY: Modifier = Modifier.PUBLIC

    fun parseKotlinFile(kotlinFile: String): String {
        val trimmedFile: String = kotlinFile.trim()

        val classStart: Int = lineStart(trimmedFile, trimmedFile.indexOf("class"))
        val classBody: Pair<Int, Int> = nextBlock(trimmedFile)
        val classBlock: String = trimmedFile.substring(classStart, classBody.second)

        return parseClass(classBlock).toString()
    }

    private fun parseClass(classBlock: String): ClassData {
        val classIndex: Int = classBlock.indexOf("class")
        val className: String = nextWord(classBlock, classIndex + 5)
        val classModifiers: List<String> =
            classBlock.substring(
                lineStart(classBlock, classIndex), classIndex - 1
            ).split(" ")
        val modifiers: Set<Modifier> = getModifiers(classModifiers)

        val fields: List<FieldData> = parseFields(classBlock)
        val methods: List<MethodData> = parseMethods(classBlock)

        return ClassData(className, modifiers, fields, methods)
    }

    private fun parseFields(classBlock: String): List<FieldData> {
        val fields: MutableList<FieldData> = mutableListOf()
        val lines: List<String> = classBlock.split("\n")

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

    private fun parseMethods(classBlock: String): List<MethodData> {
        return emptyList()
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

        for (i: Int in nonWhitespaceStart until code.length) {
            val char: Char = code[i]
            if (isOpeningBracket(char) || char.isWhitespace() || char == ':' || char == ',') {
                end = i
                break
            }
        }

        return code.substring(nonWhitespaceStart, end)
    }

    private fun nextNonWhitespaceIndex(code: String, start: Int): Int {
        var index: Int = start

        while (index in code.indices) {
            if (!code[index].isWhitespace()) return index
            else index++
        }

        return -1
    }

    private fun nextBlock(code: String, openChar: Char = '{', closeChar: Char = '}'): Pair<Int, Int> {
        val start: Int = code.indexOf(openChar)
        var end: Int = code.length - 1

        var openBrackets = 0

        for (i: Int in start until code.length) {
            when (code[i]) {
                openChar -> openBrackets++
                closeChar -> openBrackets--
            }

            if (openBrackets == 0) {
                end = i
                break
            }
        }

        return Pair(start, end + 1)
    }

    private fun isOpeningBracket(char: Char): Boolean {
        return char == '{' || char == '(' || char == '['
    }

    private fun isClosingBracket(char: Char): Boolean {
        return char == '}' || char == ')' || char == ']'
    }

    // endregion
}