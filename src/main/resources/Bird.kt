open class Bird(
    name: String,
    age: Int,
) : Animal(name, age) {

    override fun speak() {
        println("Chirp!")
    }
}