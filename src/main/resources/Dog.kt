class Dog(
    name: String,
    age: Int,
    var breed: String,
) : Animal(name, age) {
    override fun speak() {
        println("Woof!")
    }

    fun fetch() {
        println("Fetching...")
    }
}

class Cat(
    name: String,
    age: Int,
    var color: String,
) : Animal(name, age) {
    override fun speak() {
        println("Meow!")
    }

    fun scratch() {
        println("Scratching...")
    }

    private fun planWorldDomination() {
        println("Planning world domination...")
    }
}