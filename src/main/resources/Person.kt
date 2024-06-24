data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int,
) {
    fun greet() {
        println("Hello, my name is $firstName $lastName and I am $age years old.")
    }

    private fun procrastinate() {
        println("Procrastinating...")
    }
}