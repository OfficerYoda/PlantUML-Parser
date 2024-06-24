class Fish(
    name: String,
    age: Int,
    val weight: Double,
): Animal(name, age) {
    fun swim() {
        println("Swimming")
    }
}