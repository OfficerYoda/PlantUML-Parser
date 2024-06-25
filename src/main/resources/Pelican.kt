class Pelican(
    name: String,
    age: Int,
    val mouthSize: Int
) : Bird(name, age), Hunter {

    override fun speak() {
        println("Pelican noises")
    }

    override fun hunt(): Animal {
        println("Pelican hunting")
        return Fish("Fish", 1, 0.1)
    }
}