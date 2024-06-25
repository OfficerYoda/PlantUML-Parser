class Pelican(
    name: String,
    age: Int,
    val mouthSize: Int
) : Bird(name, age), Hunter {

    override fun speak() {
        println("Pelican noises")
    }

    override fun hunt() {
        println("Pelican hunting")
    }
}