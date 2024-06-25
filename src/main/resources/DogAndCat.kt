
enum class PetKindness {
    GoodBoy,
    PlaningWorldDomination,
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

    private fun planWorldDomination() {
        println("Planning world domination...")
    }
}
