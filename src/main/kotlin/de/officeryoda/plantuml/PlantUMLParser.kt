package de.officeryoda.plantuml

object PlantUMLParser {

    private val DEFAULT_VISIBILITY: Modifier = Modifier.PUBLIC

    fun parseKotlinFile(kotlinFile: String): String {
        kotlinFile.trimIndent()

        return parseClass(kotlinFile).toString()
    }

    private fun parseClass(classBlock: String): ClassData {
        val classIndex: Int = classBlock.indexOf("class")
        val classModifiers: List<String> =
            classBlock.substring(
                lineStart(classBlock, classIndex), classIndex - 1
            ).split(" ")

        val className: String = nextWord(classBlock, classIndex + 5)
        val modifiers: Set<Modifier> = getModifiers(classModifiers)

        val fields: List<FieldData> = parseFields(classBlock)
        val methods: List<MethodData> = parseMethods(classBlock)

        return ClassData(className, modifiers, fields, methods)
    }

    private fun parseFields(classBlock: String): List<FieldData> {
        return emptyList()
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

    private fun lineStart(code: String, index: Int): Int {
        var start: Int = index
        while (start > 0 && code[start] != '\n') {
            start--
        }
        return start
    }

    private fun nextWord(code: String, start: Int): String {
        var end: Int = start
        val nonWhitespaceStart: Int = indexOfNonWhitespace(code, start, 1)

        for (i: Int in nonWhitespaceStart until code.length) {
            val char: Char = code[i]
            if (isOpeningBracket(char) || char.isWhitespace()) {
                end = i
                break
            }
        }

        return code.substring(nonWhitespaceStart, end)
    }

    private fun previousWord(code: String, end: Int): String {
        var start = 0
        val nonWhitespaceEnd: Int = indexOfNonWhitespace(code, end, -1)

        for (i: Int in nonWhitespaceEnd downTo 0) {
            val char: Char = code[i]
            if (isClosingBracket(char) || char.isWhitespace()) {
                start = i
                break
            }
        }

        return code.substring(start, nonWhitespaceEnd + 1)
    }

    private fun indexOfNonWhitespace(code: String, start: Int, direction: Int): Int {
        var index: Int = start
        while (index in code.indices) {
            if (!code[index].isWhitespace()) {
                return index
            }
            index += direction
        }
        return -1
    }

    private fun nextBlock(code: String, openChar: Char = '{', closeChar: Char = '}'): Pair<Int, Int> {
        val start: Int = code.indexOf(openChar)
        var end: Int = code.length - 1

        var openBrackets = 1

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

        return Pair(start + 1, end)
    }

    private fun isOpeningBracket(char: Char): Boolean {
        return char == '{' || char == '(' || char == '['
    }

    private fun isClosingBracket(char: Char): Boolean {
        return char == '}' || char == ')' || char == ']'
    }
}