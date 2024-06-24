open class Animal(
    val name: String,
    var age: Int,
) {
    open fun speak() {
        println("Animal speaking")
    }
}
