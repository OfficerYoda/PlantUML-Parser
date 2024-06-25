abstract class Animal(
    val name: String,
    var age: Int,
) {

    val size: Int = (Math.random() * 100).toInt()
    private var hunger = 0

    abstract fun speak()

    fun eat() {
        hunger = 0
    }
}
