package de.officeryoda.plantuml

object PlantUMLParser {

    fun parseKotlinFile(kotlinFile: String): String {
        kotlinFile.trimIndent()

        val className: String = nextWord(kotlinFile, kotlinFile.indexOf("class") + 5)
        val classVisibility: String = wordBefore(kotlinFile, kotlinFile.indexOf("class") - 1)

        val classBlock: String = getBlock(kotlinFile)

        return "$classVisibility $className \n $classBlock"
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

    private fun wordBefore(code: String, end: Int): String {
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

    private fun getBlock(code: String, openChar: Char = '{', closeChar: Char = '}'): String {
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

        return code.substring(start + 1, end)
    }

    private fun isOpeningBracket(char: Char): Boolean {
        return char == '{' || char == '(' || char == '['
    }

    private fun isClosingBracket(char: Char): Boolean {
        return char == '}' || char == ')' || char == ']'
    }
}