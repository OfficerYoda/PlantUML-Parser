@Target(AnnotationTarget.CLASS)
annotation class Pet() {
    companion object {
        fun isPet(animal: Animal): Boolean {
            return animal::class.java.isAnnotationPresent(Pet::class.java)
        }
    }
}
