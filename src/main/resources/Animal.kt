open class Animal(
    val name: String,
    var age: Int,
) {

    private val size = 20
    var hunger = 0

    open fun speak() {
        println("Animal speaking")
    }
}
