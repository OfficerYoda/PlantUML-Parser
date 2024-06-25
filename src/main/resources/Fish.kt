@Pet
class Fish(
    name: String,
    age: Int,
    val weight: Double,
) : Animal(name, age), Prey {
    fun swim() {
        println("Swimming")
    }

    fun drown() {
        println("Drowning")
    }

    override fun flee() {
        println("Fish fleeing")
    }

    override fun speak() {
        println("Blub")
    }
}
