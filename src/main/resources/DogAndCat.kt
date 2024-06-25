enum class PetKindness {
    GoodBoy,
    PlaningWorldDomination;

    fun testKindness(a: Animal): PetKindness {
        if (a is Dog) {
            return GoodBoy
        } else if (a is Cat) {
            return PlaningWorldDomination
        }
        return GoodBoy
    }
}

@Pet
class Dog(
    name: String,
    age: Int,
    var breed: String,
) : Animal(name, age) {

    val kindness: PetKindness = PetKindness.GoodBoy

    override fun speak() {
        println("Woof!")
    }

    fun fetch() {
        println("Fetching...")
    }

    fun play(
        other: Dog,
        evil: Cat) {
        println("Playing with $other and the pure evil $evil")
    }
}

@Pet
class Cat(
    name: String,
    age: Int,
    var color: String,
) : Animal(name, age) {

    val kindness: PetKindness = PetKindness.PlaningWorldDomination

    override fun speak() {
        println("Meow!")
    }

    fun scratch() {
        println("Scratching...")
    }

    private fun planWorldDomination(): String {
        println("Planning world domination...")
        return "The world is mine!"
    }
}
