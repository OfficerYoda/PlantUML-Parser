open class Animal(
    val name: String,
    var age: Int,
) {

    private val size = 20
    var hugner = 0

    open fun speak() {
        println("Animal speaking")
    }
}
